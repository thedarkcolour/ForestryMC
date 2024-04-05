package forestry.storage.proxy;

import java.util.List;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.registries.RegistryObject;

import forestry.core.config.Constants;
import forestry.modules.IClientModuleHandler;
import forestry.modules.features.ModFeatureRegistry;
import forestry.storage.ModuleBackpacks;
import forestry.storage.items.ItemBackpack;
import forestry.storage.models.FilledCrateModel;

@OnlyIn(Dist.CLIENT)
public class ProxyStorageClient extends ProxyStorage implements IClientModuleHandler {
	public static final ModelResourceLocation FILLED_CRATE_MODEL = new ModelResourceLocation(Constants.MOD_ID, "filled_crate", "inventory");

	public ProxyStorageClient() {
		ModFeatureRegistry.get(ModuleBackpacks.class).addRegistryListener(Registry.ITEM_REGISTRY, event -> {
			ItemPropertyFunction itemPropertyFunction = (stack, clientLevel, holder, idk) -> ItemBackpack.getMode(stack).ordinal();

			for (RegistryObject<Item> entry : ModFeatureRegistry.get(ModuleBackpacks.class).getRegistry(Registry.ITEM_REGISTRY).getEntries()) {
				if (entry.get() instanceof ItemBackpack) {
					ItemProperties.register(entry.get(), new ResourceLocation("mode"), itemPropertyFunction);
				}
			}
		});
	}

	@Override
	public void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		event.register(FILLED_CRATE_MODEL);
	}

	@Override
	public void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register("filled_crate", FilledCrateModel.Loader.INSTANCE);
	}

	@Override
	public void bakeModels(ModelEvent.BakingCompleted event) {
		FilledCrateModel.cachedBaseModel = null;
		FilledCrateModel.transforms = null;
		FilledCrateModel.particle = null;
	}
}
