package forestry.cultivation.tiles;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.cultivation.features.CultivationTiles;
import forestry.farming.logic.ForestryFarmIdentifier;

public class TileFarmEnder extends TilePlanter {
	public TileFarmEnder(BlockPos pos, BlockState state) {
		super(CultivationTiles.ENDER.tileType(), pos, state, ForestryFarmIdentifier.ENDER);
	}

	@Override
	public List<ItemStack> createGermlingStacks() {
		return List.of(
				new ItemStack(Blocks.CHORUS_FLOWER),
				new ItemStack(Blocks.CHORUS_FLOWER),
				new ItemStack(Blocks.CHORUS_FLOWER),
				new ItemStack(Blocks.CHORUS_FLOWER)
		);
	}

	@Override
	public List<ItemStack> createResourceStacks() {
		return List.of(
				new ItemStack(Blocks.END_STONE),
				new ItemStack(Blocks.END_STONE),
				new ItemStack(Blocks.END_STONE),
				new ItemStack(Blocks.END_STONE)
		);
	}

	@Override
	public List<ItemStack> createProductionStacks() {
		return List.of(
				new ItemStack(Blocks.CHORUS_FLOWER),
				new ItemStack(Items.CHORUS_FRUIT),
				new ItemStack(Items.CHORUS_FRUIT),
				new ItemStack(Blocks.CHORUS_FLOWER)
		);
	}
}
