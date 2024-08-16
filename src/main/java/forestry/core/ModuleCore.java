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

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import forestry.api.ForestryConstants;
import forestry.api.IForestryApi;
import forestry.api.client.IClientModuleHandler;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IForestryModule;
import forestry.api.modules.IPacketRegistry;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.ItemPollenCluster;
import forestry.apiimpl.plugin.PluginManager;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.arboriculture.loot.CountBlockFunction;
import forestry.arboriculture.loot.GrafterLootModifier;
import forestry.core.blocks.TileStreamUpdateTracker;
import forestry.core.client.CoreClientHandler;
import forestry.core.climate.ForestryClimateManager;
import forestry.core.commands.DiagnosticsCommand;
import forestry.core.features.CoreItems;
import forestry.core.loot.ConditionLootModifier;
import forestry.core.loot.OrganismFunction;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.core.network.packets.PacketActiveUpdate;
import forestry.core.network.packets.PacketChipsetClick;
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
import forestry.core.network.packets.RecipeCachePacket;
import forestry.core.owner.GameProfileDataSerializer;
import forestry.core.recipes.RecipeManagers;
import forestry.core.utils.NetworkUtil;
import forestry.core.worldgen.VillagerJigsaw;
import forestry.lepidopterology.features.LepidopterologyItems;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleManager;
import forestry.modules.ModuleUtil;
import forestry.modules.features.FeatureItem;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;

@ForestryModule
public class ModuleCore extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CORE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ModuleCore::onCommonSetup);
		modBus.addListener(ModuleCore::registerGlobalLootModifiers);
		modBus.addListener(EventPriority.LOWEST, ModuleCore::postItemRegistry);

		ItemGroupForestry.initTabs();
		ModuleUtil.loadFeatureProviders();
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onItemPickup);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onLevelTick);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::onTagsUpdated);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::registerReloadListeners);
		MinecraftForge.EVENT_BUS.addListener(ModuleCore::registerCommands);

		PluginManager.registerAsyncException(modBus);
	}

	private static void onCommonSetup(FMLCommonSetupEvent event) {
		// Forestry's villager houses
		event.enqueueWork(() -> {
			VillagerJigsaw.init();
			((ForestryModuleManager) IForestryApi.INSTANCE.getModuleManager()).setupApi();
			PluginManager.registerCircuits();
			EntityDataSerializers.registerSerializer(GameProfileDataSerializer.INSTANCE);
			registerComposts();
		});
	}

	private static void registerComposts() {
		Object2FloatMap<ItemLike> composts = ComposterBlock.COMPOSTABLES;

		for (FeatureItem<?> fruit : CoreItems.FRUITS.getFeatures()) {
			composts.put(fruit, 0.65f);
		}
		composts.put(CoreItems.MOULDY_WHEAT, 0.65f);
		composts.put(CoreItems.DECAYING_WHEAT, 0.65f);
		composts.put(CoreItems.MULCH, 0.65f);
		composts.put(CoreItems.ASH, 0.65f);
		composts.put(CoreItems.WOOD_PULP, 0.65f);
		composts.put(CoreItems.PEAT, 0.75f);
		composts.put(CoreItems.COMPOST, 1f);
		for (ItemPollenCluster pollen : ApicultureItems.POLLEN_CLUSTER.getItems()) {
			composts.put(pollen, 0.3f);
		}
		composts.put(ArboricultureItems.SAPLING, 0.3f);
		composts.put(ArboricultureItems.POLLEN_FERTILE, 0.3f);
		for (BlockItem leaves : ArboricultureBlocks.LEAVES_DECORATIVE.getItems()) {
			composts.put(leaves, 0.3f);
		}
		composts.put(LepidopterologyItems.COCOON_GE, 0.3f);
	}

	private static void registerGlobalLootModifiers(RegisterEvent event) {
		event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, helper -> {
			helper.register(ForestryConstants.forestry("condition_modifier"), ConditionLootModifier.CODEC);
			helper.register(ForestryConstants.forestry("grafter_modifier"), GrafterLootModifier.CODEC);

			OrganismFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, ForestryConstants.forestry("set_species_nbt"), new LootItemFunctionType(new OrganismFunction.Serializer()));
			CountBlockFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, ForestryConstants.forestry("count_from_block"), new LootItemFunctionType(new CountBlockFunction.Serializer()));
		});
	}

	// Lowest priority
	private static void postItemRegistry(RegisterEvent event) {
		event.register(Registry.ITEM_REGISTRY, helper -> {
			PluginManager.registerGenetics();
			PluginManager.registerFarming();
		});
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

	private static void registerReloadListeners(AddReloadListenerEvent event) {
		event.addListener((prepBarrier, resourceManager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
			return prepBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
				RecipeManagers.invalidateCaches();
				NetworkUtil.sendToAllPlayers(new RecipeCachePacket());
			});
		});
	}

	private static void registerCommands(RegisterCommandsEvent event) {
		LiteralArgumentBuilder<CommandSourceStack> forestryCommand = LiteralArgumentBuilder.literal("forestry");

		forestryCommand.then(DiagnosticsCommand.register());

		for (IForestryModule module : IForestryApi.INSTANCE.getModuleManager().getModulesForMod(ForestryConstants.MOD_ID)) {
			if (module instanceof BlankForestryModule forestryModule) {
				forestryModule.addToRootCommand(forestryCommand);
			}
		}

		event.getDispatcher().register(forestryCommand);
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
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.GUI_SELECTION_REQUEST, PacketGuiSelectRequest.class, PacketGuiSelectRequest::decode, PacketGuiSelectRequest::handle);
		registry.serverbound(PacketIdServer.PIPETTE_CLICK, PacketPipetteClick.class, PacketPipetteClick::decode, PacketPipetteClick::handle);
		registry.serverbound(PacketIdServer.CHIPSET_CLICK, PacketChipsetClick.class, PacketChipsetClick::decode, PacketChipsetClick::handle);
		registry.serverbound(PacketIdServer.SOLDERING_IRON_CLICK, PacketSolderingIronClick.class, PacketSolderingIronClick::decode, PacketSolderingIronClick::handle);

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
		registry.clientbound(PacketIdClient.RECIPE_CACHE, RecipeCachePacket.class, RecipeCachePacket::decode, RecipeCachePacket::handle);
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new CoreClientHandler());
	}
}
