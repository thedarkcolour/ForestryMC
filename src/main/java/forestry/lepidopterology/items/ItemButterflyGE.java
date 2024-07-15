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
package forestry.lepidopterology.items;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import forestry.api.client.ISpriteRegister;
import forestry.api.core.ItemGroups;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.IButterflyNursery;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.config.Config;
import forestry.core.genetics.ItemGE;
import forestry.core.items.definitions.IColoredItem;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.EntityUtil;
import forestry.core.utils.GeneticsUtil;
import forestry.lepidopterology.entities.EntityButterfly;
import forestry.lepidopterology.features.LepidopterologyEntities;
import forestry.lepidopterology.genetics.ButterflyHelper;

import genetics.api.GeneticHelper;
import forestry.api.genetics.alleles.IAlleleSpecies;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;
import genetics.utils.AlleleUtils;

public class ItemButterflyGE extends ItemGE implements ISpriteRegister, IColoredItem {
	public static final String NBT_AGE = "Age";
	public static final int MAX_AGE = 3;

	private final ButterflyLifeStage type;

	public ItemButterflyGE(ButterflyLifeStage type) {
		super(new Properties().tab(ItemGroups.tabLepidopterology));
		this.type = type;
	}

	@Override
	protected IAlleleForestrySpecies getSpecies(ItemStack itemStack) {
		return GeneticHelper.getOrganism(itemStack).getAllele(ButterflyChromosomes.SPECIES, true);
	}

	@Override
	protected ILifeStage getType() {
		return type;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return GeneticHelper.createOrganism(stack, type, ButterflyHelper.getRoot().getDefinition());
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> subItems) {
		if (this.allowedIn(tab)) {
			addCreativeItems(subItems, true);
		}
	}

	public void addCreativeItems(NonNullList<ItemStack> subItems, boolean hideSecrets) {
		if (type == ButterflyLifeStage.COCOON) {
			for (int age = 0; age < MAX_AGE; age++) {
				for (IButterfly individual : ButterflyManager.butterflyRoot.getIndividualTemplates()) {
					// Don't show secret butterflies unless ordered to.
					if (hideSecrets && individual.isSecret() && !Config.isDebug) {
						continue;
					}

					ItemStack butterfly = ButterflyManager.butterflyRoot.getTypes().createStack(individual, type);

					ItemButterflyGE.setAge(butterfly, age);

					subItems.add(butterfly);
				}
			}
		} else {
			for (IButterfly individual : ButterflyManager.butterflyRoot.getIndividualTemplates()) {
				// Don't show secret butterflies unless ordered to.
				if (hideSecrets && individual.isSecret() && !Config.isDebug) {
					continue;
				}

				subItems.add(ButterflyManager.butterflyRoot.getTypes().createStack(individual, type));
			}
		}
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		if (type != ButterflyLifeStage.BUTTERFLY) {
			return false;
		}
		Level level = entityItem.level;
		if (level.isClientSide || entityItem.tickCount < 80) {
			return false;
		}
		if (level.random.nextInt(24) != 0) {
			return false;
		}

		IButterfly butterfly = ButterflyManager.butterflyRoot.getTypes().createIndividual(entityItem.getItem());
		if (butterfly == null) {
			return false;
		}

		if (!butterfly.canTakeFlight(level, entityItem.getX(), entityItem.getY(), entityItem.getZ())) {
			return false;
		}

		EntityUtil.spawnEntity(entityItem.level, EntityButterfly.create(LepidopterologyEntities.BUTTERFLY.entityType(), entityItem.level, butterfly, entityItem.blockPosition()), entityItem.getX(), entityItem.getY(), entityItem.getZ());
		if (!entityItem.getItem().isEmpty()) {
			entityItem.getItem().shrink(1);
		} else {
			entityItem.remove(Entity.RemovalReason.DISCARDED);
		}
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		if (level.isClientSide) {
			return InteractionResult.PASS;
		}

		ItemStack stack = player.getItemInHand(context.getHand());

		IButterfly flutter = ButterflyManager.butterflyRoot.getTypes().createIndividual(stack);

		BlockState blockState = level.getBlockState(pos);
		if (type == ButterflyLifeStage.COCOON) {
			pos = ButterflyManager.butterflyRoot.plantCocoon(level, pos, flutter, player.getGameProfile(), getAge(stack), true);
			if (pos != BlockPos.ZERO) {
				BlockUtil.sendPlaceSound(level, pos, blockState);

				if (!player.isCreative()) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.PASS;
			}
		} else if (type == ButterflyLifeStage.CATERPILLAR) {
			IButterflyNursery nursery = GeneticsUtil.getOrCreateNursery(player.getGameProfile(), level, pos, true);
			if (nursery != null) {
				if (!nursery.canNurse(flutter)) {
					return InteractionResult.PASS;
				}

				nursery.setCaterpillar(flutter);

				BlockUtil.sendDestroyEffects(level, pos, blockState);

				if (!player.isCreative()) {
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		} else {
			return InteractionResult.PASS;
		}
	}

	public static void setAge(ItemStack cocoon, int age) {
		if (cocoon.isEmpty()) {
			return;
		}
		if (ButterflyManager.butterflyRoot.getTypes().getType(cocoon) != ButterflyLifeStage.COCOON) {
			return;
		}
		CompoundTag tagCompound = cocoon.getTag();
		if (tagCompound == null) {
			cocoon.setTag(tagCompound = new CompoundTag());
		}
		tagCompound.putInt(NBT_AGE, age);
	}

	public static int getAge(ItemStack cocoon) {
		if (cocoon.isEmpty()) {
			return 0;
		}
		if (ButterflyManager.butterflyRoot.getTypes().getType(cocoon) != ButterflyLifeStage.COCOON) {
			return 0;
		}
		CompoundTag tagCompound = cocoon.getTag();
		if (tagCompound == null) {
			return 0;
		}
		return Mth.clamp(tagCompound.getInt(NBT_AGE), 0, 2);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerSprites(Consumer<ResourceLocation> registry) {
		AlleleUtils.forEach(ButterflyChromosomes.SPECIES, (allele) -> allele.registerSprites(registry));
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		if (stack.hasTag()) {
			IIndividual individual = GeneticHelper.getIndividual(stack);
			if (individual != null) {
				IAlleleSpecies species = individual.getGenome().getPrimarySpecies();
				return ((IAlleleForestrySpecies) species).getSpriteColour(tintIndex);
			}
		}
		return 0xffffff;
	}
}
