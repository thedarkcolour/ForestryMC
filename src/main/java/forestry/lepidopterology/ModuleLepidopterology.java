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
package forestry.lepidopterology;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import forestry.api.client.IClientModuleHandler;
import forestry.api.modules.ForestryModuleIds;
import forestry.core.ModuleCore;
import forestry.core.utils.SpeciesUtil;
import forestry.lepidopterology.commands.CommandButterfly;
import forestry.lepidopterology.entities.EntityButterfly;
import forestry.lepidopterology.features.LepidopterologyEntities;
import forestry.lepidopterology.features.LepidopterologyFeatures;
import forestry.lepidopterology.proxy.LepidopterologyClientHandler;
import forestry.modules.BlankForestryModule;

public class ModuleLepidopterology extends BlankForestryModule {
	public static int maxDistance = 64;
	private static boolean allowPollination = true;
	public static final Map<String, Float> spawnRaritys = Maps.newHashMap();
	private static boolean spawnButterflysFromLeaves = true;
	private static boolean generateCocoons = false;
	private static float generateCocoonsAmount = 1.0f;
	private static float serumChance = 0.55f;
	private static float secondSerumChance = 0;

	public ModuleLepidopterology() {
		MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onEntityTravelToDimension);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onAttributeCreate);

		if (generateCocoons) {
			if (generateCocoonsAmount > 0.0) {
				IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
				LepidopterologyFeatures.FEATURES.register(modEventBus);
				LepidopterologyFeatures.CONFIGURED_FEATURES.register(modEventBus);
				LepidopterologyFeatures.PLACED_FEATURES.register(modEventBus);
			}
		}
	}

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.LEPIDOPTEROLOGY;
	}

	@Override
	public void preInit() {
		LepidopterologyFilterRule.init();
		LepidopterologyFilterRuleType.init();
	}

	@Override
	public List<ResourceLocation> getModuleDependencies() {
		return List.of(ForestryModuleIds.CORE, ForestryModuleIds.ARBORICULTURE);
	}

	@Override
	public void doInit() {
		ModuleCore.rootCommand.then(CommandButterfly.register());

		if (spawnButterflysFromLeaves) {
			SpeciesUtil.TREE_TYPE.get().registerLeafTickHandler(new ButterflySpawner());
		}
	}

	public static boolean isPollinationAllowed() {
		return allowPollination;
	}

	public static boolean isSpawnButterflysFromLeaves() {
		return spawnButterflysFromLeaves;
	}

	public static boolean isGenerateCocoons() {
		return generateCocoons;
	}

	public static float getGenerateCocoonsAmount() {
		return generateCocoonsAmount;
	}

	public static float getSerumChance() {
		return serumChance;
	}

	public static float getSecondSerumChance() {
		return secondSerumChance;
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new LepidopterologyClientHandler());
	}

	private static class ForgeEvents {
		public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
			if (event.getEntity() instanceof EntityButterfly) {
				event.setCanceled(true);
			}
		}

		public static void onAttributeCreate(EntityAttributeCreationEvent event) {
			event.put(LepidopterologyEntities.BUTTERFLY.entityType(), LepidopterologyEntities.BUTTERFLY.createAttributes().build());
		}
	}
}
