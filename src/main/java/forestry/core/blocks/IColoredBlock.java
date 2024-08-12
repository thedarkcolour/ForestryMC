package forestry.core.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IColoredBlock {
	@OnlyIn(Dist.CLIENT)
	int colorMultiplier(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex);
}
