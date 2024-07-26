package forestry.farming.logic.crops;

import javax.annotation.Nullable;

import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.core.utils.BlockUtil;

public class CropDestroyDouble extends Crop {
	protected final BlockState blockState;
	protected final BlockState blockStateUp;
	@Nullable
	protected final BlockState replantState;

	public CropDestroyDouble(Level world, BlockState blockState, BlockState blockStateUp, BlockPos position, @Nullable BlockState replantState) {
		super(world, position);
		this.blockState = blockState;
		this.blockStateUp = blockStateUp;
		this.replantState = replantState;
	}

	@Override
	protected boolean isCrop(Level world, BlockPos pos) {
		return world.getBlockState(pos) == blockState;
	}

	@Override
	protected List<ItemStack> harvestBlock(Level level, BlockPos pos) {
		Block block = blockState.getBlock();
		Block blockUp = blockStateUp.getBlock();
		NonNullList<ItemStack> harvested = NonNullList.create();
		//		block.getDrops(harvested, level, pos, blockState, 0);
		//		blockUp.getDrops(harvested, level, pos.up(), blockStateUp, 0);
		//TODO getDrops. Loot tables?
		BlockUtil.sendDestroyEffects(level, pos, blockState);

		level.removeBlock(pos.above(), false);
		if (replantState != null) {
			level.setBlock(pos, replantState, Block.UPDATE_CLIENTS);
		} else {
			level.removeBlock(pos, false);
		}

		return harvested;
	}

	@Override
	public String toString() {
		return String.format("CropDestroyDouble [ position: [ %s ]; block: %s ]", position.toString(), blockState);
	}
}
