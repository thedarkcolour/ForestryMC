package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.network.IContainerFactory;

import net.minecraftforge.registries.RegistryObject;

public class FeatureMenuType<M extends AbstractContainerMenu> extends ModFeature implements IMenuTypeFeature<M> {
	private final RegistryObject<MenuType<M>> menuTypeObject;

	public FeatureMenuType(IFeatureRegistry registry, String moduleID, String identifier, IContainerFactory<M> containerFactory) {
		super(moduleID, registry.getModId(), identifier);
		this.menuTypeObject = registry.getRegistry(Registry.MENU_REGISTRY).register(identifier, () -> new MenuType<>(containerFactory));
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.MENU_REGISTRY;
	}

	@Override
	public MenuType<M> menuType() {
		return menuTypeObject.get();
	}
}
