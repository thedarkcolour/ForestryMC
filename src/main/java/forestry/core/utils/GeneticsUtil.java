/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.IArmorNaturalist;
import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.IPollinatableSpeciesType;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.capability.IIndividualHandlerItem;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.arboriculture.capabilities.ArmorNaturalist;
import forestry.core.genetics.ItemGE;
import forestry.core.tiles.TileUtil;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public class GeneticsUtil {
	private static String getKeyPrefix(ISpecies<?> allele) {
		if (allele instanceof IBeeSpecies) {
			return "for.bees";
		} else if (allele instanceof ITreeSpecies) {
			return "for.trees";
		} else if (allele instanceof IButterflySpecies) {
			return "for.butterflies";
		}
		throw new IllegalStateException();
	}

	public static Component getAlyzerName(ILifeStage type, ISpecies<?> allele) {
		String customKey = getKeyPrefix(allele) +
				".custom.alyzer." +
				type.getSerializedName() +
				'.' +
				allele.getTranslationKey();
		return Translator.tryTranslate(customKey, allele::getDisplayName);
	}

	public static Component getItemName(ILifeStage type, ISpecies<?> species) {
		String prefix = getKeyPrefix(species);
		String customKey = prefix +
				".custom." +
				type.getSerializedName() +
				'.' +
				species.getTranslationKey();
		return Translator.tryTranslate(customKey, () -> {
			Component speciesName = species.getDisplayName();
			Component typeName = Component.translatable(prefix + ".grammar." + type.getSerializedName() + ".type");
			return Component.translatable(prefix + ".grammar." + type.getSerializedName(), speciesName, typeName);
		});
	}

	public static boolean hasNaturalistEye(Player player) {
		ItemStack armorItemStack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (armorItemStack.isEmpty()) {
			return false;
		}

		final IArmorNaturalist armorNaturalist;
		LazyOptional<IArmorNaturalist> armorCap = armorItemStack.getCapability(ForestryCapabilities.ARMOR_NATURALIST);
		if (armorCap.isPresent()) {
			armorNaturalist = armorCap.orElse(ArmorNaturalist.INSTANCE);
		} else {
			return false;
		}

		return armorNaturalist.canSeePollination(player, armorItemStack, true);
	}

	public static boolean canNurse(IButterfly butterfly, Level world, final BlockPos pos) {
		IButterflyNursery tile = TileUtil.getTile(world, pos, IButterflyNursery.class);
		return tile != null && tile.canNurse(butterfly);
	}

	/**
	 * Returns an ICheckPollinatable that can be checked but not mated.
	 * Used to check for pollination traits without altering the world by changing vanilla leaves to forestry ones.
	 */
	@Nullable
	public static ICheckPollinatable getCheckPollinatable(Level world, final BlockPos pos) {
		IPollinatable tile = TileUtil.getTile(world, pos, IPollinatable.class);
		if (tile != null) {
			return tile;
		}

		IIndividual pollen = GeneticsUtil.getPollen(world, pos);
		if (pollen != null) {
			ISpeciesType<?, ?> type = pollen.getType();
			if (type instanceof IPollinatableSpeciesType speciesRoot) {
				return speciesRoot.createPollinatable(pollen);
			}
		}

		return null;
	}

	/**
	 * Returns an IPollinatable that can be mated. This will convert vanilla leaves to Forestry leaves.
	 */
	@Nullable
	public static IPollinatable getOrCreatePollinatable(@Nullable GameProfile owner, Level world, final BlockPos pos, boolean convertVanilla) {
		IPollinatable pollinatable = TileUtil.getTile(world, pos, IPollinatable.class);
		if (pollinatable == null && convertVanilla) {
			IIndividual pollen = GeneticsUtil.getPollen(world, pos);
			if (pollen != null) {
				ISpeciesType<?, ?> root = pollen.getType();
				if (root instanceof IPollinatableSpeciesType speciesRoot) {
					pollinatable = speciesRoot.tryConvertToPollinatable(owner, world, pos, pollen);
				}
			}
		}
		return pollinatable;
	}

	@Nullable
	public static IButterflyNursery getOrCreateNursery(@Nullable GameProfile gameProfile, LevelAccessor world, BlockPos pos, boolean convertVanilla) {
		IButterflyNursery nursery = getNursery(world, pos);
		if (nursery == null && convertVanilla) {
			IIndividual pollen = GeneticsUtil.getPollen(world, pos);

			if (pollen instanceof ITree tree) {
				if (tree.getSpecies().setLeaves(pollen.getGenome(), world, gameProfile, pos, world.getRandom())) {
					nursery = getNursery(world, pos);
				}
			}
		}
		return nursery;
	}

	public static boolean canCreateNursery(LevelAccessor world, BlockPos pos) {
		IIndividual pollen = GeneticsUtil.getPollen(world, pos);
		return pollen instanceof ITree;
	}

	@Nullable
	public static IButterflyNursery getNursery(LevelAccessor world, BlockPos pos) {
		return TileUtil.getTile(world, pos, IButterflyNursery.class);
	}

	/**
	 * Gets pollen from a location. Does not convert the pollen source to Forestry leaves.
	 */
	@Nullable
	public static IIndividual getPollen(LevelAccessor level, final BlockPos pos) {
		if (!level.hasChunkAt(pos)) {
			return null;
		}

		ICheckPollinatable checkPollinatable = TileUtil.getTile(level, pos, ICheckPollinatable.class);
		if (checkPollinatable != null) {
			return checkPollinatable.getPollen();
		}

		BlockState state = level.getBlockState(pos);

		return SpeciesUtil.TREE_TYPE.get().getVanillaIndividual(state);
	}

	public static ItemStack convertToGeneticEquivalent(ItemStack foreign) {
		IIndividualHandlerItem individual = IIndividualHandlerItem.get(foreign);
		if (individual != null && !individual.isGeneticForm()) {
			ItemStack equivalent = individual.getIndividual().getSpecies().createStack(individual.getStage());
			equivalent.setCount(foreign.getCount());
			return equivalent;
		}
		return foreign;
	}

	public static int getResearchComplexity(ISpecies<?> species) {
		return 1 + getGeneticAdvancement(species, new HashSet<>());
	}

	@SuppressWarnings("unchecked")
	private static int getGeneticAdvancement(ISpecies<?> species, Set<ISpecies<?>> exclude) {
		int highest = 0;
		exclude.add(species);

		ISpeciesType<?, ?> type = species.getType();
		for (IMutation<?> mutation : ((IMutationManager<ISpecies<?>>) type.getMutations()).getMutationsInto(species)) {
			highest = getHighestAdvancement(mutation.getFirstParent(), highest, exclude);
			highest = getHighestAdvancement(mutation.getSecondParent(), highest, exclude);
		}

		return 1 + highest;
	}

	private static int getHighestAdvancement(ISpecies<?> mutationSpecies, int highest, Set<ISpecies<?>> exclude) {
		if (exclude.contains(mutationSpecies)) {
			return highest;
		}

		int otherAdvance = getGeneticAdvancement(mutationSpecies, exclude);
		return Math.max(otherAdvance, highest);
	}

	/**
	 * Creates a translation key for an OBJECT of a TYPE (ex. a SPECIES of a SPECIES TYPE, or an ALLELE of a CHROMOSOME)
	 * The format is as follows:
	 * <ul>
	 *     <li>If {@code typeId} and {@code objectId} share the same namespace, the format is: <br> {@code TYPE.NAMESPACE.TYPEPATH.OBJECTPATH}</li>
	 *     <li>If {@code typeId} and {@code objectId} have different namespaces, the format is: <br> {@code TYPE.TYPENAMESPACE.TYPEPATH.OBJECTNAMESPACE.OBJECTPATH}</li>
	 * </ul>
	 * For example, the Austere bee species from Forestry has the translation key: <br> {@code species.forestry.bee.austere} <br>
	 * and the Creeper bee species from Binnie's Extra Bees has the translation key: <br> {@code species.forestry.bee.extrabees.creeper}
	 *
	 * @param type     The first part of the translation key that describes the type of object this is. Can be empty.
	 * @param typeId   The ID of the type. An example would be the ID of the species type.
	 * @param objectId The ID of the object. An example would be the ID of the species.
	 * @return The translation key.
	 */
	public static String createTranslationKey(String type, ResourceLocation typeId, ResourceLocation objectId) {
		String typeNamespace = typeId.getNamespace();
		StringBuilder translationKey = new StringBuilder(type);
		if (!type.isEmpty()) {
			translationKey.append('.');
		}
		translationKey.append(typeNamespace);
		translationKey.append('.');
		translationKey.append(typeId.getPath());
		translationKey.append('.');

		String speciesNamespace = objectId.getNamespace();
		if (speciesNamespace.equals(typeNamespace)) {
			// for species from the same mod as species type, use the following format:
			// species.forestry.bee.austere
			translationKey.append(objectId.getPath());
		} else {
			// if species type is from another mod, use this format instead:
			// species.forestry.bee.extrabees.creeper
			translationKey.append(speciesNamespace);
			translationKey.append('.');
			translationKey.append(objectId.getPath());
		}

		return translationKey.toString();
	}

	public static String createDescriptionKey(ISpecies<?> id) {
		return createTranslationKey("species", id.getType().id(), id.id()) + ".desc";
	}

	public static Reference2ObjectOpenHashMap<ISpecies<?>, ItemStack> getIconStacks(ILifeStage stage, ISpeciesType<?, ?> type) {
		Reference2ObjectOpenHashMap<ISpecies<?>, ItemStack> map = new Reference2ObjectOpenHashMap<>();
		getIconStacks(map, stage, type);
		return map;
	}

	public static void getIconStacks(Reference2ObjectOpenHashMap<ISpecies<?>, ItemStack> map, ILifeStage stage, ISpeciesType<?, ?> type) {
		ArrayList<ItemStack> itemList = new ArrayList<>(type.getAllSpecies().size());
		ItemGE.addCreativeItems(stage, itemList, false, type);

		for (ItemStack stack : itemList) {
			IIndividualHandlerItem.ifPresent(stack, individual -> {
				ISpecies<?> species = individual.getGenome().getActiveSpecies();
				map.put(species, stack);
			});
		}
	}
}
