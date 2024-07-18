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
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.IForestryApi;
import forestry.api.circuits.ChipsetManager;
import forestry.api.core.ForestryError;
import forestry.api.core.IError;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IPacketRegistry;
import forestry.api.multiblock.MultiblockManager;
import forestry.api.recipes.RecipeManagers;
import forestry.core.blocks.TileStreamUpdateTracker;
import forestry.core.circuits.CircuitRegistry;
import forestry.core.circuits.SolderManager;
import forestry.core.climate.ForestryClimateManager;
import forestry.core.commands.CommandModules;
import forestry.core.features.CoreFeatures;
import forestry.core.genetics.alleles.AlleleManager;
import forestry.core.multiblock.MultiblockLogicFactory;
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
import forestry.core.proxy.CoreClientHandler;
import forestry.core.proxy.Proxies;
import forestry.core.recipes.HygroregulatorManager;
import forestry.modules.BlankForestryModule;
import forestry.modules.ModuleUtil;
import forestry.api.client.IClientModuleHandler;

public class ModuleCore extends BlankForestryModule {
	public static final LiteralArgumentBuilder<CommandSourceStack> rootCommand = LiteralArgumentBuilder.literal("forestry");

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CORE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		CoreFeatures.CONFIGURED_FEATURES.register(modBus);
		CoreFeatures.PLACED_FEATURES.register(modBus);

		ItemGroupForestry.initTabs();
		ModuleUtil.loadFeatureProviders();
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onItemPickup);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onLevelTick);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onTagsUpdated);
	}

	private static void onItemPickup(EntityItemPickupEvent event) {
		if (event.isCanceled() || event.getResult() == Event.Result.ALLOW) {
			return;
		}
		PickupHandlerCore.onItemPickup(event.getEntity(), event.getItem());
	}

	private static void onLevelTick(TickEvent.LevelTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			TileStreamUpdateTracker.syncVisualUpdates();
		}
	}

	private static void onTagsUpdated(TagsUpdatedEvent event) {
		if (event.shouldUpdateStaticData()) {
			event.getRegistryAccess().registry(Registry.BIOME_REGISTRY).ifPresent(registry -> ((ForestryClimateManager) IForestryApi.INSTANCE.getClimateManager()).onBiomesReloaded(registry));
		}
	}

	@Override
	public boolean isCore() {
		return true;
	}

	@Override
	public List<ResourceLocation> getModuleDependencies() {
		return List.of();
	}

	@Override
	public void setupApi() {
		ChipsetManager.solderManager = new SolderManager();

		ChipsetManager.circuitRegistry = new CircuitRegistry();

		AlleleManager.alleleRegistry = new genetics.alleles.AlleleRegistry();
		AlleleManager.alleleFactory = new AlleleManager();

		MultiblockManager.logicFactory = new MultiblockLogicFactory();

		RecipeManagers.hygroregulatorManager = new HygroregulatorManager();

		for (IError code : ForestryError.values()) {
			IForestryApi.INSTANCE.getErrorManager().registerError(code);
		}
	}

	@Override
	public void preInit() {
		EntityDataSerializers.registerSerializer(GameProfileDataSerializer.INSTANCE);

		rootCommand.then(CommandModules.register());
	}

	@Nullable
	@Override
	public LiteralArgumentBuilder<CommandSourceStack> register() {
		// todo register this
		return rootCommand;
	}

	@Override
	public void doInit() {
		Proxies.render.initRendering();
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.GUI_SELECTION_REQUEST, PacketGuiSelectRequest.class, PacketGuiSelectRequest::decode, PacketGuiSelectRequest::handle);
		registry.serverbound(PacketIdServer.PIPETTE_CLICK, PacketPipetteClick.class, PacketPipetteClick::decode, PacketPipetteClick::handle);
		registry.serverbound(PacketIdServer.CHIPSET_CLICK, PacketChipsetClick.class, PacketChipsetClick::decode, PacketChipsetClick::handle);
		registry.serverbound(PacketIdServer.SOLDERING_IRON_CLICK, PacketSolderingIronClick.class, PacketSolderingIronClick::decode, PacketSolderingIronClick::handle);
		//registry.serverbound(PacketIdServer.SELECT_CLIMATE_TARGETED, PacketSelectClimateTargeted.class, PacketSelectClimateTargeted::decode, PacketSelectClimateTargeted::handle);
		registry.serverbound(PacketIdServer.CLIMATE_LISTENER_UPDATE_REQUEST, PacketClimateListenerUpdateRequest.class, PacketClimateListenerUpdateRequest::decode, PacketClimateListenerUpdateRequest::handle);

		registry.clientbound(PacketIdClient.ERROR_UPDATE, PacketErrorUpdate.class, PacketErrorUpdate::decode, PacketErrorUpdate::handle);
		registry.clientbound(PacketIdClient.GUI_UPDATE, PacketGuiStream.class, PacketGuiStream::decode, PacketGuiStream::handle);
		registry.clientbound(PacketIdClient.GUI_LAYOUT_SELECT, PacketGuiLayoutSelect.class, PacketGuiLayoutSelect::decode, PacketGuiLayoutSelect::handle);
		registry.clientbound(PacketIdClient.GUI_ENERGY, PacketGuiEnergy.class, PacketGuiEnergy::decode, PacketGuiEnergy::handle);
		registry.clientbound(PacketIdClient.SOCKET_UPDATE, PacketSocketUpdate.class, PacketSocketUpdate::decode, PacketSocketUpdate::handle);
		registry.clientbound(PacketIdClient.TILE_FORESTRY_UPDATE, PacketTileStream.class, PacketTileStream::decode, PacketTileStream::handle);
		registry.clientbound(PacketIdClient.TILE_FORESTRY_ACTIVE, PacketActiveUpdate.class, PacketActiveUpdate::decode, PacketActiveUpdate::handle);
		registry.clientbound(PacketIdClient.ITEMSTACK_DISPLAY, PacketItemStackDisplay.class, PacketItemStackDisplay::decode, PacketItemStackDisplay::handle);
		registry.clientbound(PacketIdClient.GENOME_TRACKER_UPDATE, PacketTankLevelUpdate.class, PacketTankLevelUpdate::decode, PacketTankLevelUpdate::handle);
		registry.clientbound(PacketIdClient.TANK_LEVEL_UPDATE, PacketGenomeTrackerSync.class, PacketGenomeTrackerSync::decode, PacketGenomeTrackerSync::handle);
		registry.clientbound(PacketIdClient.UPDATE_CLIMATE, PacketClimateUpdate.class, PacketClimateUpdate::decode, PacketClimateUpdate::handle);
		registry.clientbound(PacketIdClient.CLIMATE_LISTENER_UPDATE, PacketClimateListenerUpdate.class, PacketClimateListenerUpdate::decode, PacketClimateListenerUpdate::handle);
		registry.clientbound(PacketIdClient.CLIMATE_PLAYER, PacketClimateListenerUpdate.class, PacketClimateListenerUpdate::decode, PacketClimateListenerUpdate::handle);
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new CoreClientHandler());
	}
}
