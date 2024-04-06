package forestry.cultivation;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.modules.ForestryModule;
import forestry.core.ClientsideCode;
import forestry.core.config.Constants;
import forestry.cultivation.features.CultivationMenuTypes;
import forestry.cultivation.gui.GuiPlanter;
import forestry.cultivation.proxy.ProxyCultivation;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ISidedModuleHandler;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.CULTIVATION, name = "Cultivation", author = "Nedelosk", url = Constants.URL, unlocalizedDescription = "for.module.cultivation.description")
public class ModuleCultivation extends BlankForestryModule {

	public static final ProxyCultivation PROXY = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newProxyCultivation() : new ProxyCultivation();

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerGuiFactories() {
		MenuScreens.register(CultivationMenuTypes.PLANTER.menuType(), GuiPlanter::new);
	}

	@Override
	public Set<ResourceLocation> getDependencyUids() {
		return ImmutableSet.of(new ResourceLocation(Constants.MOD_ID, ForestryModuleUids.CORE),
			new ResourceLocation(Constants.MOD_ID, ForestryModuleUids.FARMING));
	}

	@Override
	public ISidedModuleHandler getModuleHandler() {
		return PROXY;
	}

}
