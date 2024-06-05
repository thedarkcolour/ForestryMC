/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import forestry.api.circuits.ChipsetManager;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.modules.ForestryModule;
import forestry.api.multiblock.MultiblockManager;
import forestry.api.recipes.RecipeManagers;
import forestry.climatology.network.packets.PacketSelectClimateTargeted;
import forestry.core.circuits.CircuitRegistry;
import forestry.core.circuits.GuiSolderingIron;
import forestry.core.circuits.SolderManager;
import forestry.core.commands.CommandModules;
import forestry.core.config.Constants;
import forestry.core.features.CoreFeatures;
import forestry.core.features.CoreMenuTypes;
import forestry.core.genetics.alleles.AlleleFactory;
import forestry.core.gui.GuiAlyzer;
import forestry.core.gui.GuiAnalyzer;
import forestry.core.gui.GuiEscritoire;
import forestry.core.gui.GuiNaturalistInventory;
import forestry.core.multiblock.MultiblockLogicFactory;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.core.network.packets.PacketActiveUpdate;
import forestry.core.network.packets.PacketChipsetClick;
import forestry.core.network.packets.PacketClimateListenerUpdate;
import forestry.core.network.packets.PacketClimateListenerUpdateRequest;
import forestry.core.network.packets.PacketClimateUpdate;
import forestry.core.network.packets.PacketErrorUpdate;
import forestry.core.network.packets.PacketGenomeTrackerSync;
import forestry.core.network.packets.PacketGuiEnergy;
import forestry.core.network.packets.PacketGuiLayoutSelect;
import forestry.core.network.packets.PacketGuiSelectRequest;
import forestry.core.network.packets.PacketGuiStream;
import forestry.core.network.packets.PacketItemStackDisplay;
import forestry.core.network.packets.PacketPipetteClick;
import forestry.core.network.packets.PacketSocketUpdate;
import forestry.core.network.packets.PacketSolderingIronClick;
import forestry.core.network.packets.PacketTankLevelUpdate;
import forestry.core.network.packets.PacketTileStream;
import forestry.core.owner.GameProfileDataSerializer;
import forestry.core.proxy.Proxies;
import forestry.core.recipes.HygroregulatorManager;
import forestry.core.utils.ClimateUtil;
import forestry.core.utils.ForestryModEnvWarningCallable;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ISidedModuleHandler;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.CORE, name = "Core", author = "SirSengir", url = Constants.URL, unlocalizedDescription = "for.module.core.description", coreModule = true)
public class ModuleCore extends BlankForestryModule {
	public static final LiteralArgumentBuilder<CommandSourceStack> rootCommand = LiteralArgumentBuilder.literal("forestry");

	public ModuleCore() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		CoreFeatures.CONFIGURED_FEATURES.register(bus);
		CoreFeatures.PLACED_FEATURES.register(bus);
	}

	@Override
	public Set<ResourceLocation> getDependencyUids() {
		return Collections.emptySet();
	}

	@Override
	public void setupAPI() {
		ChipsetManager.solderManager = new SolderManager();

		ChipsetManager.circuitRegistry = new CircuitRegistry();

		AlleleManager.climateHelper = new ClimateUtil();
		AlleleManager.alleleFactory = new AlleleFactory();

		MultiblockManager.logicFactory = new MultiblockLogicFactory();

		RecipeManagers.hygroregulatorManager = new HygroregulatorManager();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerGuiFactories() {
		MenuScreens.register(CoreMenuTypes.ALYZER.menuType(), GuiAlyzer::new);
		MenuScreens.register(CoreMenuTypes.ANALYZER.menuType(), GuiAnalyzer::new);
		MenuScreens.register(CoreMenuTypes.NATURALIST_INVENTORY.menuType(), GuiNaturalistInventory::new);
		MenuScreens.register(CoreMenuTypes.ESCRITOIRE.menuType(), GuiEscritoire::new);
		MenuScreens.register(CoreMenuTypes.SOLDERING_IRON.menuType(), GuiSolderingIron::new);
	}


	@Override
	public void preInit() {
		GameProfileDataSerializer.register();

		rootCommand.then(CommandModules.register());
	}

	@Nullable
	@Override
	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return rootCommand;
	}

	@Override
	public void doInit() {
		ForestryModEnvWarningCallable.register();

		Proxies.render.initRendering();
	}

	@Override
	public ISaveEventHandler getSaveEventHandler() {
		return new SaveEventHandlerCore();
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.GUI_SELECTION_REQUEST, PacketGuiSelectRequest.class, PacketGuiSelectRequest::decode, PacketGuiSelectRequest::handle);
		registry.serverbound(PacketIdServer.PIPETTE_CLICK, PacketPipetteClick.class, PacketPipetteClick::decode, PacketPipetteClick::handle);
		registry.serverbound(PacketIdServer.CHIPSET_CLICK, PacketChipsetClick.class, PacketChipsetClick::decode, PacketChipsetClick::handle);
		registry.serverbound(PacketIdServer.SOLDERING_IRON_CLICK, PacketSolderingIronClick.class, PacketSolderingIronClick::decode, PacketSolderingIronClick::handle);
		registry.serverbound(PacketIdServer.SELECT_CLIMATE_TARGETED, PacketSelectClimateTargeted.class, PacketSelectClimateTargeted::decode, PacketSelectClimateTargeted::handle);
		registry.serverbound(PacketIdServer.CLIMATE_LISTENER_UPDATE_REQUEST, PacketClimateListenerUpdateRequest.class, PacketClimateListenerUpdateRequest::decode, PacketClimateListenerUpdateRequest::handle);

		registry.clientbound(PacketIdClient.GUI_UPDATE, PacketErrorUpdate.class, PacketErrorUpdate::decode, PacketErrorUpdate::handle);
		registry.clientbound(PacketIdClient.GUI_LAYOUT_SELECT, PacketGuiStream.class, PacketGuiStream::decode, PacketGuiStream::handle);
		registry.clientbound(PacketIdClient.GUI_ENERGY, PacketGuiLayoutSelect.class, PacketGuiLayoutSelect::decode, PacketGuiLayoutSelect::handle);
		registry.clientbound(PacketIdClient.SOCKET_UPDATE, PacketGuiEnergy.class, PacketGuiEnergy::decode, PacketGuiEnergy::handle);
		registry.clientbound(PacketIdClient.TILE_FORESTRY_UPDATE, PacketSocketUpdate.class, PacketSocketUpdate::decode, PacketSocketUpdate::handle);
		registry.clientbound(PacketIdClient.TILE_FORESTRY_ACTIVE, PacketTileStream.class, PacketTileStream::decode, PacketTileStream::handle);
		registry.clientbound(PacketIdClient.ITEMSTACK_DISPLAY, PacketActiveUpdate.class, PacketActiveUpdate::decode, PacketActiveUpdate::handle);
		registry.clientbound(PacketIdClient.FX_SIGNAL, PacketItemStackDisplay.class, PacketItemStackDisplay::decode, PacketItemStackDisplay::handle);
		registry.clientbound(PacketIdClient.GENOME_TRACKER_UPDATE, PacketTankLevelUpdate.class, PacketTankLevelUpdate::decode, PacketTankLevelUpdate::handle);
		registry.clientbound(PacketIdClient.UPDATE_CLIMATE, PacketGenomeTrackerSync.class, PacketGenomeTrackerSync::decode, PacketGenomeTrackerSync::handle);
		registry.clientbound(PacketIdClient.CLIMATE_LISTENER_UPDATE, PacketClimateUpdate.class, PacketClimateUpdate::decode, PacketClimateUpdate::handle);
		registry.clientbound(PacketIdClient.CLIMATE_PLAYER, PacketClimateListenerUpdate.class, PacketClimateListenerUpdate::decode, PacketClimateListenerUpdate::handle);
	}

	@Override
	public boolean processIMCMessage(InterModComms.IMCMessage message) {
		return false;
	}

	@Override
	public IPickupHandler getPickupHandler() {
		return new PickupHandlerCore();
	}

	@Override
	public ISidedModuleHandler getModuleHandler() {
		return Proxies.render;
	}
}
