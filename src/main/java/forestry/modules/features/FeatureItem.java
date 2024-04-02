package forestry.modules.features;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import forestry.core.config.Constants;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FeatureItem<I extends Item> extends ModFeature implements IItemFeature<I> {
	private final RegistryObject<I> itemObject;

	public FeatureItem(IFeatureRegistry registry, String moduleID, String identifier, Supplier<I> constructor) {
		super(moduleID, registry.getModId(), identifier);
		this.itemObject = registry.getRegistry(Registry.ITEM_REGISTRY).register(identifier, constructor);
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.ITEM_REGISTRY;
	}

	@Override
	public I item() {
		return itemObject.get();
	}
}
