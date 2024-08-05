package forestry.arboriculture.blocks;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.client.IForestryClientApi;
import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.core.utils.SpeciesUtil;

/**
 * Genetic leaves with no tile entity, used for worldgen trees.
 * Similar to decorative leaves, but these will drop saplings and can be used for pollination.
 */
public class BlockDefaultLeaves extends BlockAbstractLeaves {
	private final ForestryLeafType type;

	public BlockDefaultLeaves(ForestryLeafType type) {
		this.type = type;
	}

	public ResourceLocation getSpeciesId() {
		return this.type.getSpeciesId();
	}

	public ForestryLeafType getType() {
		return this.type;
	}

	@Override
	protected void getLeafDrop(NonNullList<ItemStack> drops, Level world, @Nullable GameProfile playerProfile, BlockPos pos, float saplingModifier, int fortune, LootContext.Builder builder) {
		ITree tree = getTree(world, pos);
		if (tree == null) {
			return;
		}

		// Add saplings
		List<ITree> saplings = tree.getSaplings(world, playerProfile, pos, saplingModifier);
		for (ITree sapling : saplings) {
			if (sapling != null) {
				drops.add(SpeciesUtil.TREE_TYPE.get().createStack(sapling, TreeLifeStage.SAPLING));
			}
		}
	}

	@Override
	protected ITree getTree(BlockGetter world, BlockPos pos) {
		return type.getIndividual();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int colorMultiplier(BlockState state, @Nullable BlockGetter level, @Nullable BlockPos pos, int tintIndex) {
		return IForestryClientApi.INSTANCE.getTreeManager().getTint(type.getIndividual().getSpecies()).get(level, pos);
	}
}
