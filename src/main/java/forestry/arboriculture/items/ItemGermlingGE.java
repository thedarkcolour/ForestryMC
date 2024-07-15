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
package forestry.arboriculture.items;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.ItemGroups;
import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.recipes.IVariableFermentable;
import forestry.arboriculture.genetics.TreeHelper;
import forestry.core.genetics.ItemGE;
import forestry.core.items.definitions.IColoredItem;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.GeneticsUtil;

import genetics.api.GeneticHelper;
import forestry.api.genetics.ILifeStage;

public class ItemGermlingGE extends ItemGE implements IVariableFermentable, IColoredItem {
	private final TreeLifeStage type;

	public ItemGermlingGE(TreeLifeStage type) {
		super(new Item.Properties().tab(ItemGroups.tabArboriculture));
		this.type = type;
	}

	@Override
	protected final ILifeStage getType() {
		return type;
	}

	@Override
	protected IAlleleTreeSpecies getSpecies(ItemStack itemStack) {
		return GeneticHelper.getOrganism(itemStack).getAllele(TreeChromosomes.SPECIES, true);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return GeneticHelper.createOrganism(stack, type, TreeHelper.getRoot().getDefinition());
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> subItems) {
		if (allowedIn(tab)) {
			addCreativeItems(this, subItems, true, TreeHelper.getRoot());
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int renderPass) {
		return getSpecies(itemstack).getGermlingColour(type, renderPass);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		BlockHitResult traceResult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.ANY);
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (traceResult.getType() == HitResult.Type.BLOCK) {
			ITree tree = TreeManager.treeRoot.create(stack);

			if (tree != null) {
				BlockPos pos = traceResult.getBlockPos();

				if (type == TreeLifeStage.SAPLING) {
					BlockPlaceContext context = new BlockPlaceContext(new UseOnContext(playerIn, handIn, traceResult));

					return onItemRightClickSapling(stack, worldIn, playerIn, pos, tree, context);
				} else if (type == TreeLifeStage.POLLEN) {
					return onItemRightClickPollen(stack, worldIn, playerIn, pos, tree);
				}
			}
		}

		return InteractionResultHolder.pass(stack);
	}

	private static InteractionResultHolder<ItemStack> onItemRightClickPollen(ItemStack stack, Level level, Player player, BlockPos pos, ITree tree) {
		ICheckPollinatable checkPollinatable = GeneticsUtil.getCheckPollinatable(level, pos);
		if (checkPollinatable == null || !checkPollinatable.canMateWith(tree)) {
			return InteractionResultHolder.fail(stack);
		}

		IPollinatable pollinatable = GeneticsUtil.getOrCreatePollinatable(player.getGameProfile(), level, pos, true);
		if (pollinatable == null || !pollinatable.canMateWith(tree)) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide) {
			pollinatable.mateWith(tree);

			BlockUtil.sendDestroyEffects(level, pos, level.getBlockState(pos));

			if (!player.isCreative()) {
				stack.shrink(1);
			}
		}
		return InteractionResultHolder.success(stack);
	}

	private static InteractionResultHolder<ItemStack> onItemRightClickSapling(ItemStack stack, Level worldIn, Player player, BlockPos pos, ITree tree, BlockPlaceContext context) {
		// x, y, z are the coordinates of the block "hit", can thus either be the soil or tall grass, etc.
		BlockState hitBlock = worldIn.getBlockState(pos);
		if (!hitBlock.canBeReplaced(context)) {
			if (!worldIn.isEmptyBlock(pos.above())) {
				return InteractionResultHolder.fail(stack);
			}
			pos = pos.above();
		}

		if (tree.canStay(worldIn, pos)) {
			if (TreeManager.treeRoot.plantSapling(worldIn, tree, player.getGameProfile(), pos)) {
				if (!player.isCreative()) {
					stack.shrink(1);
				}
				return InteractionResultHolder.success(stack);
			}
		}
		return InteractionResultHolder.fail(stack);
	}

	@Override
	public float getFermentationModifier(ItemStack itemstack) {
		itemstack = GeneticsUtil.convertToGeneticEquivalent(itemstack);
		ITree tree = TreeManager.treeRoot.create(itemstack);
		if (tree == null) {
			return 1.0f;
		}
		return tree.getGenome().getActiveValue(TreeChromosomes.SAPPINESS) * 10;
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return 100;
	}
}
