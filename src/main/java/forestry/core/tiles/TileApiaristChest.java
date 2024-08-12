package forestry.core.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import forestry.core.features.CoreTiles;
import forestry.core.utils.SpeciesUtil;

public class TileApiaristChest extends TileNaturalistChest {
	public TileApiaristChest(BlockPos pos, BlockState state) {
		super(CoreTiles.APIARIST_CHEST.tileType(), pos, state, SpeciesUtil.BEE_TYPE.get());
	}
}
