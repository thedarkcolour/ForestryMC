package forestry.core.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public interface ForestryTicker<T extends TileForestry> extends BlockEntityTicker<T> {
	@Override
	default void tick(Level level, BlockPos pos, BlockState state, T tile) {
		tile.tickHelper.onTick();

		tick(tile, level, pos, state);

		if (tile.needsNetworkUpdate) {
			tile.needsNetworkUpdate = false;
			tile.sendNetworkUpdate();
		}
	}

	void tick(T tile, Level level, BlockPos pos, BlockState state);
}
