package forestry.apiculture;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class EndFlowerType extends FlowerType {
	public EndFlowerType(TagKey<Block> acceptableFlowers, boolean dominant) {
		super(acceptableFlowers, dominant);
	}

	@Override
	public boolean isAcceptableFlower(Level level, BlockPos pos) {
		return level.getBiome(pos).is(BiomeTags.IS_END) || super.isAcceptableFlower(level, pos);
	}
}
