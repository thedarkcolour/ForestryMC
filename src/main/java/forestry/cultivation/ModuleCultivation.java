package forestry.cultivation;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.modules.ForestryModuleIds;
import forestry.cultivation.features.CultivationMenuTypes;
import forestry.cultivation.gui.GuiPlanter;
import forestry.cultivation.proxy.CultivationClientHandler;
import forestry.modules.BlankForestryModule;
import forestry.api.client.IClientModuleHandler;

public class ModuleCultivation extends BlankForestryModule {

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CULTIVATION;
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new CultivationClientHandler());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerMenuScreens() {
		MenuScreens.register(CultivationMenuTypes.PLANTER.menuType(), GuiPlanter::new);
	}

	@Override
	public List<ResourceLocation> getModuleDependencies() {
		return List.of(ForestryModuleIds.CORE, ForestryModuleIds.FARMING);
	}
}
