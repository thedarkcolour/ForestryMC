package forestry.api.core;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ITileTypeProvider<T extends BlockEntity> {
	BlockEntityType<T> tileType();
}
