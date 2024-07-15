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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.ForestryConstants;
import forestry.api.IForestryApi;
import forestry.api.client.IForestryClientApi;
import forestry.api.core.ISetupListener;
import forestry.api.recipes.ICarpenterRecipe;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.IFabricatorSmeltingRecipe;
import forestry.api.recipes.IFermenterRecipe;
import forestry.api.recipes.IHygroregulatorRecipe;
import forestry.api.recipes.IMoistenerRecipe;
import forestry.api.recipes.ISolderRecipe;
import forestry.api.recipes.ISqueezerContainerRecipe;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.IStillRecipe;
import forestry.arboriculture.loot.CountBlockFunction;
import forestry.arboriculture.loot.GrafterLootModifier;
import forestry.core.ClientsideCode;
import forestry.core.EventHandlerCore;
import forestry.core.circuits.CircuitRecipe;
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
import forestry.core.proxy.Proxies;
import forestry.core.proxy.ProxyCommon;
import forestry.core.recipes.HygroregulatorRecipe;
import forestry.core.render.ForestryTextureManager;
import forestry.core.utils.ForgeUtils;
import forestry.apiculture.villagers.VillagerJigsaw;
import forestry.factory.recipes.CarpenterRecipe;
import forestry.factory.recipes.CentrifugeRecipe;
import forestry.factory.recipes.FabricatorRecipe;
import forestry.factory.recipes.FabricatorSmeltingRecipe;
import forestry.factory.recipes.FermenterRecipe;
import forestry.factory.recipes.MoistenerRecipe;
import forestry.factory.recipes.SqueezerContainerRecipe;
import forestry.factory.recipes.SqueezerRecipe;
import forestry.factory.recipes.StillRecipe;
import forestry.modules.ForestryModuleManager;
import forestry.modules.features.ModFeatureRegistry;

import genetics.Genetics;
import forestry.api.genetics.alleles.IAllele;
import genetics.utils.AlleleUtils;

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
		IEventBus modEventBus = ForgeUtils.modBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::registerCapabilities);
		modEventBus.addListener(this::clientSetupRenderers);
		modEventBus.addListener(this::gatherData);
		MinecraftForge.EVENT_BUS.register(EventHandlerCore.class);
		MinecraftForge.EVENT_BUS.register(this);
		Proxies.common = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newProxyCommon() : new ProxyCommon();
		// Modules must be set up before Genetics API
		Genetics.initGenetics(modEventBus);
	}

	public void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (ModFeatureRegistry value : ModFeatureRegistry.getRegistries().values()) {
			value.clientSetupRenderers(event);
		}
	}

	private void setup(FMLCommonSetupEvent event) {
		// Forestry's villager houses
		event.enqueueWork(VillagerJigsaw::init);

		// Register event handler
		callSetupListeners(true);
		ForestryModuleManager.getModuleHandler().runPreInit();
		ForestryModuleManager.getModuleHandler().runInit();
		callSetupListeners(false);
		ForestryModuleManager.getModuleHandler().runPostInit();
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		ForestryModuleManager.getModuleHandler().registerCapabilities(event::register);
	}

	private void callSetupListeners(boolean start) {
		for (IAllele allele : AlleleUtils.getAlleles()) {
			if (allele instanceof ISetupListener listener) {
				if (start) {
					listener.onStartSetup();
				} else {
					listener.onFinishSetup();
				}
			}
		}
	}

	private void gatherData(GatherDataEvent event) {
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

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ForestryConstants.MOD_ID)
	public static class RegistryEvents {
		// should honestly go in Common
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void createObjects(RegisterEvent event) {
			ForestryModuleManager.getModuleHandler().postRegistry(event);
		}

		@SubscribeEvent
		public static void register(RegisterEvent event) {
			event.register(Registry.RECIPE_SERIALIZER_REGISTRY, helper -> {
				helper.register(new ResourceLocation(ICarpenterRecipe.TYPE.toString()), new CarpenterRecipe.Serializer());
				helper.register(new ResourceLocation(ICentrifugeRecipe.TYPE.toString()), new CentrifugeRecipe.Serializer());
				helper.register(new ResourceLocation(IFabricatorRecipe.TYPE.toString()), new FabricatorRecipe.Serializer());
				helper.register(new ResourceLocation(IFabricatorSmeltingRecipe.TYPE.toString()), new FabricatorSmeltingRecipe.Serializer());
				helper.register(new ResourceLocation(IFermenterRecipe.TYPE.toString()), new FermenterRecipe.Serializer());
				helper.register(new ResourceLocation(IHygroregulatorRecipe.TYPE.toString()), new HygroregulatorRecipe.Serializer());
				helper.register(new ResourceLocation(IMoistenerRecipe.TYPE.toString()), new MoistenerRecipe.Serializer());
				helper.register(new ResourceLocation(ISqueezerRecipe.TYPE.toString()), new SqueezerRecipe.Serializer());
				helper.register(new ResourceLocation(ISqueezerContainerRecipe.TYPE.toString()), new SqueezerContainerRecipe.Serializer());
				helper.register(new ResourceLocation(IStillRecipe.TYPE.toString()), new StillRecipe.Serializer());
				helper.register(new ResourceLocation(ISolderRecipe.TYPE.toString()), new CircuitRecipe.Serializer());
			});

			event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, helper -> {
				helper.register(new ResourceLocation(ForestryConstants.MOD_ID, "condition_modifier"), ConditionLootModifier.CODEC);
				helper.register(new ResourceLocation(ForestryConstants.MOD_ID, "grafter_modifier"), GrafterLootModifier.CODEC);

				OrganismFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(ForestryConstants.MOD_ID, "set_species_nbt"), new LootItemFunctionType(new OrganismFunction.Serializer()));
				CountBlockFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(ForestryConstants.MOD_ID, "count_from_block"), new LootItemFunctionType(new CountBlockFunction.Serializer()));
			});
		}

		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void handleTextureRemap(TextureStitchEvent.Pre event) {
			if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
				((ForestryTextureManager) IForestryClientApi.INSTANCE.getTextureManager()).registerSprites(event::addSprite);
				ModelBlockCached.clear();
			}
		}
	}
}
