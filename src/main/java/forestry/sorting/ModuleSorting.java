package forestry.sorting;

import java.util.function.Consumer;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.filter.IFilterLogic;
import forestry.api.modules.ForestryModule;
import forestry.core.config.Constants;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.sorting.features.SortingMenuTypes;
import forestry.sorting.gui.GuiGeneticFilter;
import forestry.sorting.network.packets.PacketFilterChangeGenome;
import forestry.sorting.network.packets.PacketFilterChangeRule;
import forestry.sorting.network.packets.PacketGuiFilterUpdate;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.SORTING, name = "Sorting", author = "Nedelosk", url = Constants.URL, unlocalizedDescription = "for.module.sorting.description")
public class ModuleSorting extends BlankForestryModule {
	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.FILTER_CHANGE_RULE, PacketFilterChangeRule.class, PacketFilterChangeRule::decode, PacketFilterChangeRule::handle);
		registry.serverbound(PacketIdServer.FILTER_CHANGE_GENOME, PacketFilterChangeGenome.class, PacketFilterChangeGenome::decode, PacketFilterChangeGenome::handle);

		registry.clientbound(PacketIdClient.GUI_UPDATE_FILTER, PacketGuiFilterUpdate.class, PacketGuiFilterUpdate::decode, PacketGuiFilterUpdate::handle);
	}

	@Override
	public void setupAPI() {
		AlleleManager.filterRegistry = new FilterRegistry();
	}

	@Override
	public void disabledSetupAPI() {
		AlleleManager.filterRegistry = new DummyFilterRegistry();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerGuiFactories() {
		MenuScreens.register(SortingMenuTypes.GENETIC_FILTER.menuType(), GuiGeneticFilter::new);
	}

	@Override
	public void preInit() {
		DefaultFilterRuleType.init();
	}

	@Override
	public void registerCapabilities(Consumer<Class<?>> consumer) {
		consumer.accept(IFilterLogic.class);
	}

	@Override
	public void doInit() {
		((FilterRegistry) AlleleManager.filterRegistry).init();
	}
}
