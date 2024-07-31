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

package forestry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import net.minecraftforge.fml.common.Mod;

import forestry.api.ForestryConstants;
import forestry.api.IForestryApi;
import forestry.api.client.IForestryClientApi;
import forestry.arboriculture.loot.CountBlockFunction;
import forestry.arboriculture.loot.GrafterLootModifier;
import forestry.core.EventHandlerCore;
import forestry.core.data.ForestryAdvancementProvider;
import forestry.core.data.ForestryBackpackTagProvider;
import forestry.core.data.ForestryBlockTagsProvider;
import forestry.core.data.ForestryFluidTagsProvider;
import forestry.core.data.ForestryItemTagsProvider;
import forestry.core.data.ForestryLootModifierProvider;
import forestry.core.data.ForestryLootTableProvider;
import forestry.core.data.ForestryMachineRecipeProvider;
import forestry.core.data.ForestryRecipeProvider;
import forestry.core.data.models.ForestryBlockStateProvider;
import forestry.core.data.models.ForestryItemModelProvider;
import forestry.core.data.models.ForestryWoodModelProvider;
import forestry.core.loot.ConditionLootModifier;
import forestry.core.loot.OrganismFunction;
import forestry.core.models.ModelBlockCached;
import forestry.core.network.NetworkHandler;
import forestry.core.utils.ForgeUtils;
import forestry.modules.ForestryModuleManager;
import forestry.modules.features.ModFeatureRegistry;
import forestry.plugin.PluginManager;

/**
 * Forestry Minecraft Mod
 *
 * @author SirSengir
 */
@Mod(ForestryConstants.MOD_ID)
public class Forestry {
	public static final Logger LOGGER = LogManager.getLogger(ForestryConstants.MOD_ID);

	public Forestry() {
		ForestryModuleManager moduleManager = (ForestryModuleManager) IForestryApi.INSTANCE.getModuleManager();
		moduleManager.init();
		NetworkHandler.register();
		IEventBus modBus = ForgeUtils.modBus();
		modBus.addListener(Forestry::clientSetupRenderers);
		modBus.addListener(Forestry::gatherData);
		modBus.addListener(Forestry::register);
		modBus.addListener(EventPriority.LOWEST, Forestry::postItemRegistry);
		modBus.addListener(Forestry::handleTextureRemap);
		MinecraftForge.EVENT_BUS.register(EventHandlerCore.class);

		PluginManager.loadPlugins();
		PluginManager.registerErrors();
	}

	private static void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (ModFeatureRegistry value : ModFeatureRegistry.getRegistries().values()) {
			value.clientSetupRenderers(event);
		}
	}

	private static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		ForestryBlockTagsProvider blockTagsProvider = new ForestryBlockTagsProvider(generator, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTagsProvider);
		generator.addProvider(event.includeServer(), new ForestryAdvancementProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryBackpackTagProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryFluidTagsProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryLootTableProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryMachineRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryLootModifierProvider(generator));

		generator.addProvider(event.includeClient(), new ForestryBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new ForestryWoodModelProvider(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new ForestryItemModelProvider(generator, existingFileHelper));
	}

	private static void register(RegisterEvent event) {
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
			PluginManager.registerCircuits();
		});
	}

	private static void handleTextureRemap(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
			IForestryClientApi.INSTANCE.getTextureManager().registerSprites(event);
			ModelBlockCached.clear();
		}
	}
}
