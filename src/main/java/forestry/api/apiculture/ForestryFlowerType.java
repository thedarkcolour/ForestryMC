package forestry.api.apiculture;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.ForestryTags;

public enum ForestryFlowerType implements IFlowerType {
	VANILLA(ForestryTags.Blocks.VANILLA_FLOWERS),
	NETHER(ForestryTags.Blocks.NETHER_FLOWERS),
	CACTI(ForestryTags.Blocks.CACTI_FLOWERS),
	MUSHROOMS(ForestryTags.Blocks.MUSHROOMS_FLOWERS),
	END(ForestryTags.Blocks.END_FLOWERS) {
		@Override
		public boolean isAcceptableFlower(Level level, BlockPos pos, BlockState state) {
			return level.getBiome(pos).is(ForestryTags.Biomes.THE_END_CATEGORY) || super.isAcceptableFlower(level, pos, state);
		}
	},
	JUNGLE(ForestryTags.Blocks.JUNGLE_FLOWERS),
	SNOW(ForestryTags.Blocks.SNOW_FLOWERS),
	WHEAT(ForestryTags.Blocks.WHEAT_FLOWERS),
	GOURD(ForestryTags.Blocks.GOURD_FLOWERS);

	private final TagKey<Block> acceptableFlowers;

	ForestryFlowerType(TagKey<Block> acceptableFlowers) {
		this.acceptableFlowers = acceptableFlowers;
	}

	@Override
	public boolean isAcceptableFlower(Level level, BlockPos pos, BlockState state) {
		return state.is(this.acceptableFlowers);
	}

	@Override
	public boolean plantRandomFlower(Level level, BlockPos pos, List<BlockState> nearbyFlowers) {
		if (level.hasChunkAt(pos) && level.isEmptyBlock(pos)) {
			for (BlockState state : nearbyFlowers) {
				if (state.is(ForestryTags.Blocks.PLANTABLE_FLOWERS)) {
					if (state.canSurvive(level, pos)) {
						return level.setBlockAndUpdate(pos, state);
					}
				}
			}
		}
		return false;
	}
}
