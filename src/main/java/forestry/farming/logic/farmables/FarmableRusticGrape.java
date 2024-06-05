package forestry.farming.logic.farmables;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import forestry.farming.logic.crops.CropDestroy;

public class FarmableRusticGrape implements IFarmable {
	public static final BooleanProperty GRAPES = BooleanProperty.create("grapes");

	private final Block cropBlock;

	public FarmableRusticGrape(Block cropBlock) {
		Preconditions.checkNotNull(cropBlock);

		this.cropBlock = cropBlock;
	}

	@Override
	public boolean isSaplingAt(Level level, BlockPos pos, BlockState state) {
		return state.getBlock() == cropBlock;
	}

	@Override
	@Nullable
	public ICrop getCropAt(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() != cropBlock) {
			return null;
		}

		if (!state.getValue(GRAPES)) {
			return null;
		}

		BlockState replantState = getReplantState(state);
		return new CropDestroy(level, state, pos, replantState);
	}

	@Nullable
	protected BlockState getReplantState(BlockState blockState) {
		return blockState.setValue(GRAPES, false);
	}

	@Override
	public boolean isGermling(ItemStack stack) {
		return false;
	}

	@Override
	public boolean plantSaplingAt(Player player, ItemStack germling, Level level, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isWindfall(ItemStack stack) {
		return false;
	}
}
