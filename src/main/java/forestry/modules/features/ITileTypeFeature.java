package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import forestry.api.core.ITileTypeProvider;
import net.minecraftforge.registries.RegisterEvent;

public interface ITileTypeFeature<T extends BlockEntity> extends IModFeature, ITileTypeProvider<T> {
}
