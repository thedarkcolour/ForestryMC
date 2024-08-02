package forestry.cultivation.tiles;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.farming.ForestryFarmTypes;
import forestry.cultivation.features.CultivationTiles;

public class TileFarmNether extends TilePlanter {
	public TileFarmNether(BlockPos pos, BlockState state) {
		super(CultivationTiles.NETHER.tileType(), pos, state, ForestryFarmTypes.INFERNAL);
	}

	@Override
	public List<ItemStack> createGermlingStacks() {
		return List.of(
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART)
		);
	}

	@Override
	public List<ItemStack> createResourceStacks() {
		return List.of(
				new ItemStack(Blocks.SOUL_SAND),
				new ItemStack(Blocks.SOUL_SAND),
				new ItemStack(Blocks.SOUL_SAND),
				new ItemStack(Blocks.SOUL_SAND)
		);
	}

	@Override
	public List<ItemStack> createProductionStacks() {
		return List.of(
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART),
				new ItemStack(Items.NETHER_WART)
		);
	}
}
