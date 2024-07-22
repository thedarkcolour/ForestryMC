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

import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.genetics.TreeDefinition;

/**
 * Genetic leaves with no tile entity, used for worldgen trees.
 * Similar to decorative leaves, but these will drop saplings and can be used for pollination.
 */
public class BlockDefaultLeaves extends BlockAbstractLeaves {
	private final ForestryLeafType speciesId;

	public BlockDefaultLeaves(ForestryLeafType speciesId) {
		this.speciesId = speciesId;
	}

	public ResourceLocation getSpeciesId() {
		return speciesId.getSpeciesId();
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
				drops.add(TreeManager.treeRoot.getTypes().createStack(sapling, TreeLifeStage.SAPLING));
			}
		}
	}

	@Override
	protected ITree getTree(BlockGetter world, BlockPos pos) {
		return speciesId.createIndividual();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int colorMultiplier(BlockState state, @Nullable BlockGetter worldIn, @Nullable BlockPos pos, int tintIndex) {
		IGenome genome = speciesId.getGenome();

		ILeafSpriteProvider spriteProvider = genome.getActiveAllele(TreeChromosomes.SPECIES).getLeafSpriteProvider();
		return spriteProvider.getColor(false);
	}
}
