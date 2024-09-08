package forestry.apiculture.hives;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HiveGenGroundTag extends HiveGenGround {
	private final TagKey<Block> blocks;

	public HiveGenGroundTag(TagKey<Block> blocks) {
		this.blocks = blocks;
	}

	@Override
	public boolean isValidLocation(WorldGenLevel world, BlockPos pos) {
		BlockState groundBlockState = world.getBlockState(pos.below());
		return groundBlockState.is(blocks);
	}
}
