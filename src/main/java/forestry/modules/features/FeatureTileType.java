package forestry.modules.features;

import java.util.Collection;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.registries.RegistryObject;

public class FeatureTileType<T extends BlockEntity> extends ModFeature implements ITileTypeFeature<T> {
	private final RegistryObject<BlockEntityType<T>> blockEntityObject;

	public FeatureTileType(IFeatureRegistry registry, String moduleID, String identifier, BlockEntityType.BlockEntitySupplier<T> constructorTileEntity, Supplier<Collection<? extends Block>> validBlocks) {
		super(moduleID, registry.getModId(), identifier);
		this.blockEntityObject = registry.getRegistry(Registry.BLOCK_ENTITY_TYPE_REGISTRY).register(identifier, () -> {
			return BlockEntityType.Builder.of(constructorTileEntity, validBlocks.get().toArray(Block[]::new)).build(null);
		});
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.BLOCK_ENTITY_TYPE_REGISTRY;
	}

	@Override
	public BlockEntityType<T> tileType() {
		return blockEntityObject.get();
	}
}
