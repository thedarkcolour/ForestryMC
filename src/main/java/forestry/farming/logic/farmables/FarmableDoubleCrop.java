package forestry.farming.logic.farmables;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.farming.ICrop;
import forestry.farming.logic.crops.CropDestroyDouble;

public class FarmableDoubleCrop extends FarmableBase {
	private final BlockState topMatureState;

	public FarmableDoubleCrop(ItemStack germling, BlockState plantedState, BlockState matureState, BlockState topMatureState, boolean replant) {
		super(germling, plantedState, matureState, replant);
		this.topMatureState = topMatureState;
	}

	@Override
	public boolean isSaplingAt(Level level, BlockPos pos, BlockState state) {
		return state.getBlock() == plantedState.getBlock() && state != topMatureState;
	}

	@Override
	public ICrop getCropAt(Level level, BlockPos pos, BlockState state) {
		BlockPos posUp = pos.above();
		BlockState stateUp = level.getBlockState(posUp);
		if (state != matureState || stateUp != topMatureState) {
			return null;
		}
		return new CropDestroyDouble(level, state, stateUp, pos, replant ? plantedState : null);
	}
}
