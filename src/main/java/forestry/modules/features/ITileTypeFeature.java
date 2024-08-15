package forestry.modules.features;

import net.minecraft.world.level.block.entity.BlockEntity;

import forestry.api.core.ITileTypeProvider;

public interface ITileTypeFeature<T extends BlockEntity> extends IModFeature, ITileTypeProvider<T> {
}
