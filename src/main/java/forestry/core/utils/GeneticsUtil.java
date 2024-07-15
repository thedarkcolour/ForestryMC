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
import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.IArmorNaturalist;
import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.IPollinatableSpeciesType;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.arboriculture.capabilities.ArmorNaturalist;
import forestry.core.genetics.ItemGE;
import forestry.core.tiles.TileUtil;

import genetics.api.GeneticHelper;
import genetics.api.GeneticsAPI;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.IMutation;
import genetics.api.mutation.IMutationContainer;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IRootDefinition;
import genetics.api.root.components.ComponentKeys;
import genetics.utils.AlleleUtils;
import genetics.utils.RootUtils;

public class GeneticsUtil {
	private static String getKeyPrefix(IAllele allele) {
		if (allele instanceof IAlleleBeeSpecies) {
			return "for.bees";
		} else if (allele instanceof IAlleleTreeSpecies) {
			return "for.trees";
		} else if (allele instanceof IAlleleButterflySpecies) {
			return "for.butterflies";
		}
		throw new IllegalStateException();
	}

	public static Component getAlyzerName(ILifeStage type, IAlleleForestrySpecies allele) {
		String customKey = getKeyPrefix(allele) +
				".custom.alyzer." +
				type.getName() +
				'.' +
				allele.getSpeciesIdentifier();
		return Translator.tryTranslate(customKey, allele::getDisplayName);
	}

	public static Component getItemName(ILifeStage type, IAlleleForestrySpecies allele) {
		String prefix = getKeyPrefix(allele);
		String customKey = prefix +
				".custom." +
				type.getName() +
				'.' +
				allele.getSpeciesIdentifier();
		return Translator.tryTranslate(customKey, () -> {
			Component speciesName = allele.getDisplayName();
			Component typeName = Component.translatable(prefix + ".grammar." + type.getName() + ".type");
			return Component.translatable(prefix + ".grammar." + type.getName(), speciesName, typeName);
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
			ISpeciesType<?> root = pollen.getRoot();
			if (root instanceof IPollinatableSpeciesType<?> speciesRoot) {
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
				ISpeciesType<?> root = pollen.getRoot();
				if (root instanceof IPollinatableSpeciesType<?> speciesRoot) {
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
			if (pollen != null) {
				if (pollen instanceof ITree treeLeave) {
					if (treeLeave.setLeaves(world, gameProfile, pos, world.getRandom())) {
						nursery = getNursery(world, pos);
					}
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
	 * Gets pollen from a location. Does not affect the pollen source.
	 */
	@Nullable
	public static IIndividual getPollen(LevelAccessor world, final BlockPos pos) {
		if (!world.hasChunkAt(pos)) {
			return null;
		}

		ICheckPollinatable checkPollinatable = TileUtil.getTile(world, pos, ICheckPollinatable.class);
		if (checkPollinatable != null) {
			return checkPollinatable.getPollen();
		}

		BlockState blockState = world.getBlockState(pos);

		for (IRootDefinition<?> definition : GeneticsAPI.apiInstance.getRoots().values()) {
			ISpeciesType<IIndividual> root = definition.cast();
			IIndividual individual = root.translateMember(blockState);
			if (individual != null) {
				return individual;
			}
		}

		return null;
	}

	@Nullable
	public static <I extends IIndividual> I getGeneticEquivalent(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemGE) {
			return GeneticHelper.getIndividual(stack);
		}

		for (IRootDefinition<?> definition : GeneticsAPI.apiInstance.getRoots().values()) {
			if (!definition.isPresent()) {
				continue;
			}
			ISpeciesType<I> root = definition.cast();
			I individual = root.translateMember(stack);
			if (individual != null) {
				return individual;
			}
		}

		return null;
	}

	//unfortunately quite a few unchecked casts
	public static ItemStack convertToGeneticEquivalent(ItemStack foreign) {
		if (!RootUtils.hasRoot(foreign)) {
			IIndividual individual = getGeneticEquivalent(foreign);

			if (individual != null) {
				ISpeciesType<IIndividual> root = individual.getRoot().cast();
				ILifeStage type = root.getLifeStage(foreign);
				if (type != null) {
					ItemStack equivalent = root.createStack(individual, type);
					equivalent.setCount(foreign.getCount());
					return equivalent;
				}
				return ItemStack.EMPTY;
			} else {
				return foreign;
			}
		}
		return foreign;
	}

	public static int getResearchComplexity(IAlleleSpecies species, IChromosome speciesChromosome) {
		return 1 + getGeneticAdvancement(species, new HashSet<>(), speciesChromosome);
	}

	private static int getGeneticAdvancement(IAlleleSpecies species, Set<IAlleleSpecies> exclude, IChromosome speciesChromosome) {
		int highest = 0;
		exclude.add(species);

		ISpeciesType<IIndividual> root = species.getSpecies().cast();
		IMutationContainer<IIndividual, ? extends IMutation> container = root.getComponent(ComponentKeys.MUTATIONS);
		for (IMutation mutation : container.getPaths(species, speciesChromosome)) {
			highest = getHighestAdvancement(mutation.getFirstParent(), highest, exclude, speciesChromosome);
			highest = getHighestAdvancement(mutation.getSecondParent(), highest, exclude, speciesChromosome);
		}

		return 1 + highest;
	}

	private static int getHighestAdvancement(IAlleleSpecies mutationSpecies, int highest, Set<IAlleleSpecies> exclude, IChromosome speciesChromosome) {
		if (exclude.contains(mutationSpecies) || AlleleUtils.isBlacklisted(mutationSpecies.getId().toString())) {
			return highest;
		}

		int otherAdvance = getGeneticAdvancement(mutationSpecies, exclude, speciesChromosome);
		return Math.max(otherAdvance, highest);
	}
}
