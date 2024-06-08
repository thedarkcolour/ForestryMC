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
package forestry.modules;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.Forestry;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.IForestryModule;
import forestry.api.modules.IModuleContainer;
import forestry.api.modules.IModuleManager;
import forestry.core.ClientsideCode;
import forestry.core.IPickupHandler;
import forestry.core.IResupplyHandler;
import forestry.core.ISaveEventHandler;

public enum ModuleManager implements IModuleManager {
	INSTANCE;

	public static final List<IPickupHandler> pickupHandlers = Lists.newArrayList();
	public static final List<ISaveEventHandler> saveEventHandlers = Lists.newArrayList();
	public static final List<IResupplyHandler> resupplyHandlers = Lists.newArrayList();

	private static final LinkedHashMap<ResourceLocation, IForestryModule> loadedModules = new LinkedHashMap<>();
	private static final Set<IForestryModule> unloadedModules = new LinkedHashSet<>();
	private static final HashMap<String, IModuleContainer> moduleContainers = new HashMap<>();
	public static final Set<IForestryModule> configDisabledModules = new HashSet<>();
	public static final CommonModuleHandler moduleHandler = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newModuleHandler() : new CommonModuleHandler();

	@Override
	public void registerContainers(IModuleContainer... containers) {
		for (IModuleContainer container : containers) {
			Preconditions.checkNotNull(container);
			moduleContainers.put(container.getID(), container);
		}
	}

	@Override
	public Collection<IModuleContainer> getContainers() {
		return moduleContainers.values();
	}

	// Dependencies appear BEFORE dependents
	public static Collection<IForestryModule> getLoadedModules() {
		return Collections.unmodifiableCollection(loadedModules.values());
	}

	// Alphabetical sorting according to module ID
	public static List<IForestryModule> getSortedModules() {
		Map<IForestryModule, ResourceLocation> reverseLookup = new HashMap<>();
		for (Map.Entry<ResourceLocation, IForestryModule> entry : loadedModules.entrySet()) {
			reverseLookup.put(entry.getValue(), entry.getKey());
		}
		Comparator<IForestryModule> ordering = Comparator.comparing(reverseLookup::get);
		return Ordering.from(ordering).sortedCopy(loadedModules.values());
	}

	@Nullable
	private static IForestryModule getModuleCore(List<IForestryModule> forestryModules) {
		for (IForestryModule module : forestryModules) {
			ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);
			if (module.isAvailable() && info.coreModule()) {
				return module;
			}
		}
		return null;
	}

	private static void configureModules(Map<String, List<IForestryModule>> modules) {
		Locale locale = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		Set<ResourceLocation> toLoad = new HashSet<>();
		Set<IForestryModule> modulesToLoad = new HashSet<>();

		ImmutableList<IForestryModule> allModules = ImmutableList.copyOf(modules.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));

		for (IModuleContainer container : moduleContainers.values()) {
			String containerID = container.getID();
			List<IForestryModule> containerModules = modules.get(containerID);

			IForestryModule coreModule = getModuleCore(containerModules);
			if (coreModule != null) {
				containerModules.remove(coreModule);
				containerModules.add(0, coreModule);
			} else {
				Forestry.LOGGER.debug("Could not find core module for the module container: {}", containerID);
			}

			for (IForestryModule module : containerModules) {
				ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);
				toLoad.add(new ResourceLocation(containerID, info.moduleID()));
				modulesToLoad.add(module);
			}
		}

		//Check Dependencies
		Iterator<IForestryModule> iterator;
		boolean changed;
		do {
			changed = false;
			iterator = modulesToLoad.iterator();
			while (iterator.hasNext()) {
				IForestryModule module = iterator.next();
				Set<ResourceLocation> dependencies = module.getDependencyUids();
				if (!toLoad.containsAll(dependencies)) {
					iterator.remove();
					changed = true;
					ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);
					String moduleId = info.moduleID();
					toLoad.remove(new ResourceLocation(moduleId));
					Forestry.LOGGER.warn("Module {} is missing dependencies: {}", moduleId, dependencies);
				}
			}
		} while (changed);

		//Sort Modules
		do {
			changed = false;
			iterator = modulesToLoad.iterator();
			while (iterator.hasNext()) {
				IForestryModule module = iterator.next();
				if (loadedModules.keySet().containsAll(module.getDependencyUids())) {
					iterator.remove();
					ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);
					loadedModules.put(new ResourceLocation(info.modId(), info.moduleID()), module);
					changed = true;
					break;
				}
			}
		} while (changed);

		unloadedModules.addAll(allModules);
		unloadedModules.removeAll(loadedModules.values());

		for (IModuleContainer container : moduleContainers.values()) {
			Collection<IForestryModule> loadedModules = ModuleManager.loadedModules.values().stream().filter(m -> {
						ForestryModule info = m.getClass().getAnnotation(ForestryModule.class);
						return info.modId().equals(container.getID());
					}
			).collect(Collectors.toList());
			Collection<IForestryModule> unloadedModules = ModuleManager.unloadedModules.stream().filter(m -> {
						ForestryModule info = m.getClass().getAnnotation(ForestryModule.class);
						return info.modId().equals(container.getID());
					}
			).collect(Collectors.toList());
			container.onConfiguredModules(loadedModules, unloadedModules);
		}

		Locale.setDefault(locale);
	}

	public static void runSetup() {
		configureModules(ForestryPluginUtil.getForestryModules());
	}

	public static CommonModuleHandler getModuleHandler() {
		Preconditions.checkNotNull(moduleHandler);
		return moduleHandler;
	}

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		for (IForestryModule module : loadedModules.values()) {
			LiteralArgumentBuilder<CommandSourceStack> rootCommand = module.register();

			if (rootCommand != null) {
				dispatcher.register(rootCommand);
			}
		}
	}
}
