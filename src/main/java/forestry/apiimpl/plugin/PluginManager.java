package forestry.apiimpl.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import forestry.Forestry;
import forestry.api.IForestryApi;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.circuits.CircuitHolder;
import forestry.api.circuits.ICircuit;
import forestry.api.circuits.ICircuitLayout;
import forestry.api.client.IForestryClientApi;
import forestry.api.client.arboriculture.ILeafSprite;
import forestry.api.client.arboriculture.ILeafTint;
import forestry.api.core.IError;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.ITaxon;
import forestry.api.plugin.IForestryPlugin;
import forestry.apiimpl.ForestryApiImpl;
import forestry.apiimpl.GeneticManager;
import forestry.apiimpl.client.ForestryClientApiImpl;
import forestry.apiimpl.client.TreeClientManager;
import forestry.apiimpl.client.plugin.ClientRegistration;
import forestry.arboriculture.client.FixedLeafTint;
import forestry.core.circuits.CircuitLayout;
import forestry.core.circuits.CircuitManager;
import forestry.core.errors.ErrorManager;
import forestry.core.utils.SpeciesUtil;
import forestry.farming.FarmingManager;
import forestry.plugin.DefaultForestryPlugin;
import forestry.sorting.FilterManager;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class PluginManager {
	private static final ArrayList<IForestryPlugin> LOADED_PLUGINS = new ArrayList<>();

	// Loads all plugins from the service loader.
	public static void loadPlugins() {
		ServiceLoader<IForestryPlugin> serviceLoader = ServiceLoader.load(IForestryPlugin.class);

		serviceLoader.stream().map(ServiceLoader.Provider::get).sorted(Comparator.comparing(IForestryPlugin::id)).forEachOrdered(plugin -> {
			if (plugin.shouldLoad()) {
				if (plugin.getClass() == DefaultForestryPlugin.class) {
					LOADED_PLUGINS.add(0, plugin);
				} else {
					LOADED_PLUGINS.add(plugin);
				}
				Forestry.LOGGER.debug("Registered IForestryPlugin {} with class {}", plugin.id(), plugin.getClass().getName());
			} else {
				Forestry.LOGGER.warn("Detected IForestryPlugin {} with class {} but did not load it because IForestryPlugin.shouldLoad returned false.", plugin.id(), plugin.getClass().getName());
			}
		});

		LOADED_PLUGINS.trimToSize();
	}

	public static void registerErrors() {
		ErrorRegistration registration = new ErrorRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			plugin.registerErrors(registration);
		}

		ArrayList<IError> errors = registration.getErrors();
		int errorCount = errors.size();
		Short2ObjectOpenHashMap<IError> byNumericId = new Short2ObjectOpenHashMap<>(errorCount);
		Object2ShortOpenHashMap<IError> numericIdLookup = new Object2ShortOpenHashMap<>(errorCount);
		ImmutableMap.Builder<ResourceLocation, IError> byId = ImmutableMap.builderWithExpectedSize(errorCount);

		for (int i = 0; i < errors.size(); i++) {
			IError error = errors.get(i);
			byNumericId.put((short) i, error);
			numericIdLookup.put(error, (short) i);
			byId.put(error.getId(), error);
		}

		((ForestryApiImpl) IForestryApi.INSTANCE).setErrorManager(new ErrorManager(byNumericId, numericIdLookup, byId.build()));
	}

	// Runs after all items are registered so that electron tubes and circuit boards are available.
	public static void registerCircuits() {
		CircuitRegistration registration = new CircuitRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			// TODO remove in 1.20 when FMLCommonSetupEvent throws
			// rethrow swallowed exception
			try {
				plugin.registerCircuits(registration);
			} catch (Throwable e) {
				asyncThrown = new RuntimeException("An error was thrown by plugin " + plugin.id() + " during IForestryPlugin.registerCircuits", e);
				Forestry.LOGGER.fatal(asyncThrown);
			}
		}

		ArrayList<CircuitLayout> layouts = registration.getLayouts();
		ImmutableMap.Builder<String, ICircuitLayout> layoutsByIdBuilder = ImmutableMap.builderWithExpectedSize(layouts.size());

		for (CircuitLayout layout : layouts) {
			// Layouts by ID
			layoutsByIdBuilder.put(layout.getId(), layout);
		}

		ImmutableMap<String, ICircuitLayout> layoutsById = layoutsByIdBuilder.build();

		ArrayList<CircuitHolder> circuits = registration.getCircuits();
		ImmutableMultimap.Builder<ICircuitLayout, CircuitHolder> circuitHoldersBuilder = new ImmutableMultimap.Builder<>();
		ImmutableMap.Builder<String, ICircuit> circuitsBuilder = ImmutableMap.builderWithExpectedSize(circuits.size());

		for (CircuitHolder holder : circuits) {
			ICircuitLayout layout = layoutsById.get(holder.layoutId());

			if (layout == null) {
				throw new IllegalStateException("Attempted to register a CircuitHolder but no layout was registered with its layout ID: " + holder);
			}

			// Circuit holders by layout
			circuitHoldersBuilder.put(layout, holder);
			// Circuits by ID
			ICircuit circuit = holder.circuit();
			circuitsBuilder.put(circuit.getId(), circuit);
		}

		try {
			((ForestryApiImpl) IForestryApi.INSTANCE).setCircuitManager(new CircuitManager(circuitHoldersBuilder.build(), layoutsById, circuitsBuilder.buildOrThrow()));
		} catch (IllegalArgumentException exception) {
			Forestry.LOGGER.fatal("Failed to register circuits: two circuits were registered with the same ID");
			throw exception;
		}
	}

	public static void registerGenetics() {
		// registers filter rules and species types
		GeneticRegistration registration = new GeneticRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			plugin.registerGenetics(registration);
		}

		ImmutableMap<ResourceLocation, ISpeciesType<?, ?>> speciesTypes = registration.buildSpeciesTypes();
		ImmutableMap<String, ITaxon> taxa = registration.buildTaxa();

		Forestry.LOGGER.debug("Registered {} species types: {}", speciesTypes.size(), Arrays.toString(speciesTypes.keySet().toArray(new ResourceLocation[0])));

		ForestryApiImpl api = (ForestryApiImpl) IForestryApi.INSTANCE;
		GeneticManager geneticManager = new GeneticManager(taxa, speciesTypes);
		api.setGeneticManager(geneticManager);
		api.setFilterManager(new FilterManager(registration.getFilterRuleTypes()));

		LinkedHashMap<ISpeciesType<?, ?>, ImmutableMap<ResourceLocation, ?>> allSpecies = new LinkedHashMap<>(speciesTypes.size());
		IdentityHashMap<ISpeciesType<?, ?>, IMutationManager<?>> allMutations = new IdentityHashMap<>(speciesTypes.size());

		for (ISpeciesType<?, ?> speciesType : speciesTypes.values()) {
			// species and mutations
			Pair<? extends ImmutableMap<ResourceLocation, ?>, ? extends IMutationManager<?>> pair = speciesType.handleSpeciesRegistration(LOADED_PLUGINS);
			ImmutableMap<ResourceLocation, ?> species = pair.getFirst();
			IMutationManager<?> mutations = pair.getSecond();

			allSpecies.put(speciesType, species);
			allMutations.put(speciesType, mutations);

			Forestry.LOGGER.debug("Registered {} species for species type {}", species.size(), speciesType.id());
			Forestry.LOGGER.debug("Registered {} mutations for species type {}", mutations.getAllMutations().size(), speciesType.id());
		}

		for (Map.Entry<ISpeciesType<?, ?>, ImmutableMap<ResourceLocation, ?>> entry : allSpecies.entrySet()) {
			ISpeciesType<?, ?> speciesType = entry.getKey();

			speciesType.onSpeciesRegistered((ImmutableMap) entry.getValue(), (IMutationManager) allMutations.get(speciesType));

			if (speciesType.getAllSpecies().isEmpty()) {
				throw new IllegalStateException("Failed to register species for type " + speciesType.id());
			}
			// this will throw an exception if mutations aren't populated
			speciesType.getMutations();
		}

		geneticManager.setMutations(ImmutableMap.copyOf(allMutations));
	}

	public static void registerFarming() {
		FarmingRegistration registration = new FarmingRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			try {
				plugin.registerFarming(registration);
			} catch (Throwable t) {
				throw new RuntimeException("An error was thrown by plugin " + plugin.id() + " during IForestryPlugin.registerFarming", t);
			}
		}

		// Defensive copy of fertilizers
		FarmingManager manager = new FarmingManager(new Object2IntOpenHashMap<>(registration.getFertilizers()), registration.buildFarmTypes());

		((ForestryApiImpl) IForestryApi.INSTANCE).setFarmingManager(manager);
	}

	// Todo remove in 1.20 when FMLCommonSetupEvent throws exceptions again
	@Nullable
	@Deprecated
	private static RuntimeException asyncThrown = null;

	@Deprecated
	public static void registerAsyncException(IEventBus modBus) {
		modBus.addListener((FMLLoadCompleteEvent event) -> {
			if (asyncThrown != null) {
				throw asyncThrown;
			}
		});
	}

	// todo make this not tied to the texture stitch event
	public static void registerSprites(TextureStitchEvent.Pre event) {
		ClientRegistration registration = new ClientRegistration();

		for (IForestryPlugin plugin : LOADED_PLUGINS) {
			plugin.registerClient(consumer -> consumer.accept(registration));
		}

		HashMap<ResourceLocation, ILeafSprite> spritesById = registration.getLeafSprites();
		HashMap<ResourceLocation, ILeafTint> tintsById = registration.getTints();

		// Copy everything over to identity maps to minimize Map.get overhead during rendering
		IdentityHashMap<ITreeSpecies, ILeafSprite> sprites = new IdentityHashMap<>();
		IdentityHashMap<ITreeSpecies, ILeafTint> tints = new IdentityHashMap<>();

		for (ITreeSpecies species : SpeciesUtil.TREE_TYPE.get().getAllSpecies()) {
			ResourceLocation id = species.id();

			ILeafSprite sprite = Objects.requireNonNull(spritesById.get(id));
			ILeafTint tint = tintsById.getOrDefault(id, new FixedLeafTint(species.getEscritoireColor()));

			sprites.put(species, sprite);
			tints.put(species, tint);
		}

		((ForestryClientApiImpl) IForestryClientApi.INSTANCE).setTreeManager(new TreeClientManager(sprites, tints));
	}
}
