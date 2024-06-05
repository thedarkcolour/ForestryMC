package forestry.farming.logic.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import forestry.core.utils.BlockUtil;

public class CropChorusFlower extends Crop {
	private static final BlockState BLOCK_STATE = Blocks.CHORUS_FLOWER.defaultBlockState();

	public CropChorusFlower(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	protected boolean isCrop(Level world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.CHORUS_FLOWER;
	}

	@Override
	protected NonNullList<ItemStack> harvestBlock(Level level, BlockPos pos) {
		NonNullList<ItemStack> harvested = NonNullList.create();
		harvested.add(new ItemStack(Blocks.CHORUS_FLOWER));
		//TODO: Fix dropping
		//float chance = ForgeEventFactory.fireBlockHarvesting(harvested, level, pos, BLOCK_STATE, 0, 1.0F, false, null);
		float chance = 1.0F;
		harvested.removeIf(next -> level.random.nextFloat() > chance);

		BlockUtil.sendDestroyEffects(level, pos, BLOCK_STATE);
		level.removeBlock(pos, false);

		return harvested;
	}
}
