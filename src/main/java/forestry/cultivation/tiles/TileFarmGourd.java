package forestry.cultivation.tiles;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.cultivation.features.CultivationTiles;
import forestry.farming.logic.ForestryFarmIdentifier;

public class TileFarmGourd extends TilePlanter {
	public TileFarmGourd(BlockPos pos, BlockState state) {
		super(CultivationTiles.GOURD.tileType(), pos, state, ForestryFarmIdentifier.GOURD);
	}

	@Override
	public List<ItemStack> createGermlingStacks() {
		return List.of();
	}

	@Override
	public List<ItemStack> createResourceStacks() {
		return List.of();
	}

	@Override
	public List<ItemStack> createProductionStacks() {
		return List.of(
				new ItemStack(Blocks.MELON),
				new ItemStack(Blocks.PUMPKIN),
				new ItemStack(Blocks.PUMPKIN),
				new ItemStack(Blocks.MELON)
		);
	}
}
