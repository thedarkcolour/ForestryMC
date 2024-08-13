package forestry.api.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public interface IBlockProvider<B extends Block, I extends Item> extends IItemProvider<I> {
	B block();

	Collection<B> collect();

	default boolean blockEqual(BlockState state) {
		return blockEqual(state.getBlock());
	}

	default boolean blockEqual(Block block) {
		return block() == block;
	}
}
