package forestry.arboriculture.worldgen;

import com.google.common.base.Preconditions;

import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.mojang.authlib.GameProfile;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.genetics.Tree;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.core.tiles.TileUtil;
import forestry.modules.features.FeatureBlockGroup;

import org.jetbrains.annotations.Nullable;

public class DefaultTreeGenerator implements ITreeGenerator {
	private final Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory;
	private final IWoodType woodType;

	public DefaultTreeGenerator(Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory, IWoodType woodType) {
		this.factory = factory;
		this.woodType = Preconditions.checkNotNull(woodType);
	}

	@Override
	public Feature<NoneFeatureConfiguration> getTreeFeature(ITreeGenData tree) {
		return factory.apply(tree);
	}

	@Override
	public boolean setLogBlock(IGenome genome, LevelAccessor world, BlockPos pos, Direction facing) {
		boolean fireproof = genome.getActiveValue(TreeChromosomes.FIREPROOF);
		BlockState logBlock = TreeManager.woodAccess.getBlock(woodType, WoodBlockKind.LOG, fireproof);

		Direction.Axis axis = facing.getAxis();
		return world.setBlock(pos, logBlock.setValue(RotatedPillarBlock.AXIS, axis), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
	}

	@Override
	public boolean setLeaves(IGenome genome, LevelAccessor world, @Nullable GameProfile owner, BlockPos pos, RandomSource rand) {
		if (owner == null && genome.isDefaultGenome()) {
			IFruit fruit = genome.getActiveValue(TreeChromosomes.FRUITS);
			BlockState defaultLeaves;
			FeatureBlockGroup<? extends Block, TreeDefinition> leavesGroup;
			if (fruit.isFruitLeaf(genome, world, pos) && rand.nextFloat() <= fruitProvider.getFruitChance(genome, world, pos)) {
				leavesGroup = ArboricultureBlocks.LEAVES_DEFAULT_FRUIT;
			} else {
				leavesGroup = ArboricultureBlocks.LEAVES_DEFAULT;
			}
			defaultLeaves = leavesGroup.get(this).defaultState();
			return world.setBlock(pos, defaultLeaves, 19);
		} else {
			BlockState leaves = ArboricultureBlocks.LEAVES.defaultState();
			boolean placed = world.setBlock(pos, LeavesBlock.updateDistance(leaves, world, pos), 19);
			if (!placed) {
				return false;
			}

			TileLeaves tileLeaves = TileUtil.getTile(world, pos, TileLeaves.class);
			if (tileLeaves == null) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 19);
				return false;
			}

			if (owner != null) {
				tileLeaves.getOwnerHandler().setOwner(owner);
			}
			tileLeaves.setTree(new Tree(genome));
			return true;
		}
	}
}
