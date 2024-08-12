package forestry.core.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import forestry.core.features.CoreTiles;
import forestry.core.utils.SpeciesUtil;

public class TileArboristChest extends TileNaturalistChest {
	public TileArboristChest(BlockPos pos, BlockState state) {
		super(CoreTiles.ARBORIST_CHEST.tileType(), pos, state, SpeciesUtil.TREE_TYPE.get());
	}
}
