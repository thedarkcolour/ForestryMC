package forestry.cultivation.tiles;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.farming.ForestryFarmTypes;
import forestry.core.features.CoreBlocks;
import forestry.core.features.CoreItems;
import forestry.cultivation.features.CultivationTiles;

public class TileBog extends TilePlanter {
	public TileBog(BlockPos pos, BlockState state) {
		super(CultivationTiles.BOG.tileType(), pos, state, ForestryFarmTypes.PEAT);
	}

	@Override
	public List<ItemStack> createGermlingStacks() {
		return List.of();
	}

	@Override
	public List<ItemStack> createResourceStacks() {
		return List.of(
				CoreBlocks.BOG_EARTH.stack(),
				CoreBlocks.BOG_EARTH.stack(),
				CoreBlocks.BOG_EARTH.stack(),
				CoreBlocks.BOG_EARTH.stack()
		);
	}

	@Override
	public List<ItemStack> createProductionStacks() {
		return List.of(
				CoreItems.PEAT.stack(),
				CoreItems.PEAT.stack(),
				CoreItems.PEAT.stack(),
				CoreItems.PEAT.stack()
		);
	}
}
