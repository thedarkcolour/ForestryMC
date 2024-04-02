package forestry.modules.features;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import forestry.core.utils.ForgeUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import forestry.api.core.IBlockSubtype;
import forestry.api.core.IItemSubtype;
import forestry.api.modules.ForestryModule;
import forestry.api.storage.EnumBackpackType;
import forestry.api.storage.IBackpackDefinition;
import forestry.core.config.Constants;
import forestry.core.blocks.BlockBase;
import forestry.modules.ForestryModuleUids;
import forestry.storage.ModuleBackpacks;

public class ModFeatureRegistry {
	// Maps mod id to feature (needed because of Binnie)
	private static final LinkedHashMap<String, ModFeatureRegistry> modRegistries = new LinkedHashMap<>();

	public static ModFeatureRegistry get(String modId) {
		return modRegistries.computeIfAbsent(modId, ModFeatureRegistry::new);
	}

	public static Map<String, ModFeatureRegistry> getRegistries() {
		return modRegistries;
	}

	public static IFeatureRegistry get(Class<?> clazz) {
		ForestryModule module = clazz.getAnnotation(ForestryModule.class);
		return get(Constants.MOD_ID).getRegistry(module == null ? ForestryModuleUids.CORE : module.moduleID());
	}

	public IFeatureRegistry getRegistry(String moduleID) {
		return modules.computeIfAbsent(moduleID, id -> new ModuleFeatures(modId, id));
	}

	private final HashMap<String, ModuleFeatures> modules = new LinkedHashMap<>();
	private final String modId;

	private ModFeatureRegistry(String modId) {
		this.modId = modId;
	}

	public void register(IModFeature feature) {
		getRegistry(feature.getModuleId()).register(feature);
	}

	public Collection<IModFeature> getFeatures(ResourceKey<? extends Registry<?>> type) {
		return modules.values().stream()
				.flatMap((module) -> module.getFeatures(type).stream())
				.collect(Collectors.toSet());
	}

	public void onRegister(RegisterEvent event) {
		for (ModuleFeatures features : modules.values()) {
			features.onRegister(event);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (ModuleFeatures features : modules.values()) {
			features.clientSetupRenderers(event);
		}
	}

	private static class ModuleFeatures implements IFeatureRegistry {
		private final List<IModFeature> features = new ArrayList<>();
		private final Multimap<ResourceKey<? extends Registry<?>>, IModFeature> featureByRegistry = ArrayListMultimap.create();
		@SuppressWarnings("rawtypes")
		private final Map<ResourceKey, DeferredRegister> registries = new HashMap<>();
		private final Multimap<ResourceKey<? extends Registry<?>>, Consumer<RegisterEvent>> registryListeners = LinkedListMultimap.create();
		private final String modId;
		private final String moduleID;

		public ModuleFeatures(String modId, String moduleID) {
			this.modId = modId;
			this.moduleID = moduleID;
		}

		@Override
		public String getModId() {
			return modId;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <V> DeferredRegister<V> getRegistry(ResourceKey<? extends Registry<V>> registryKey) {
			return registries.computeIfAbsent(registryKey, key -> {
				DeferredRegister<V> registry = DeferredRegister.create(key, modId);
				// todo use different mod bus depending on registered mod?
				registry.register(ForgeUtils.modBus());
				return registry;
			});
		}

		@Override
		public <B extends Block, I extends BlockItem> FeatureBlock<B, I> block(Supplier<B> constructor, String identifier) {
			return block(constructor, null, identifier);
		}

		@Override
		public <B extends Block, I extends BlockItem> FeatureBlock<B, I> block(Supplier<B> constructor, @Nullable Function<B, I> itemConstructor, String identifier) {
			return register(new FeatureBlock<>(this, moduleID, identifier, constructor, itemConstructor));
		}

		@Override
		public <B extends Block, S extends IBlockSubtype> FeatureBlockGroup.Builder<B, S> blockGroup(Function<S, B> constructor, Class<? extends S> typeClass) {
			return new FeatureBlockGroup.Builder<>(this, constructor);
		}

		@Override
		public <B extends Block, S extends IBlockSubtype> FeatureBlockGroup.Builder<B, S> blockGroup(Function<S, B> constructor, Collection<S> types) {
			return (FeatureBlockGroup.Builder<B, S>) new FeatureBlockGroup.Builder<>(this, constructor).types(types);
		}

		@Override
		public <B extends Block, S extends IBlockSubtype> FeatureBlockGroup.Builder<B, S> blockGroup(Function<S, B> constructor, S[] types) {
			return (FeatureBlockGroup.Builder<B, S>) new FeatureBlockGroup.Builder<>(this, constructor).types(types);
		}

		@Override
		public <I extends Item> FeatureItem<I> item(Supplier<I> constructor, String identifier) {
			return register(new FeatureItem<>(this, moduleID, identifier, constructor));
		}

		@Override
		public FeatureItem<Item> backpack(IBackpackDefinition definition, EnumBackpackType type, String identifier) {
			return item(() -> ModuleBackpacks.BACKPACK_INTERFACE.createBackpack(definition, type), identifier);
		}

		@Override
		public FeatureItem<Item> naturalistBackpack(IBackpackDefinition definition, String rootUid, CreativeModeTab tab, String identifier) {
			return item(() -> ModuleBackpacks.BACKPACK_INTERFACE.createNaturalistBackpack(definition, rootUid, tab), identifier);
		}

		@Override
		public <I extends Item, S extends IItemSubtype> FeatureItemGroup<I, S> itemGroup(Function<S, I> constructor, String identifier, S[] subTypes) {
			return itemGroup(constructor, subTypes).identifier(identifier).create();
		}

		@Override
		public <I extends Item, S extends IItemSubtype> FeatureItemGroup.Builder<I, S> itemGroup(Function<S, I> constructor, S[] subTypes) {
			return (FeatureItemGroup.Builder<I, S>) new FeatureItemGroup.Builder<>(this, constructor).types(subTypes);
		}

		@Override
		public <I extends Item, R extends IItemSubtype, C extends IItemSubtype> FeatureItemTable<I, R, C> itemTable(BiFunction<R, C, I> constructor, R[] rowTypes, C[] columnTypes, String identifier) {
			return itemTable(constructor, rowTypes, columnTypes).identifier(identifier).create();
		}

		@Override
		public <I extends Item, R extends IItemSubtype, C extends IItemSubtype> FeatureItemTable.Builder<I, R, C> itemTable(BiFunction<R, C, I> constructor, R[] rowTypes, C[] columnTypes) {
			return (FeatureItemTable.Builder<I, R, C>) new FeatureItemTable.Builder<>(this, constructor).rowTypes(rowTypes).columnTypes(columnTypes);
		}

		@Override
		public <B extends Block, R extends IBlockSubtype, C extends IBlockSubtype> FeatureBlockTable.Builder<B, R, C> blockTable(BiFunction<R, C, B> constructor, R[] rowTypes, C[] columnTypes) {
			return (FeatureBlockTable.Builder<B, R, C>) new FeatureBlockTable.Builder<>(this, constructor).rowTypes(rowTypes).columnTypes(columnTypes);
		}

		@Override
		public FeatureFluid.Builder fluid(String identifier) {
			return new FeatureFluid.Builder(this, moduleID, identifier);
		}

		@Override
		public void addRegistryListener(ResourceKey<? extends Registry<?>> type, Consumer<RegisterEvent> listener) {
			registryListeners.put(type, listener);
		}

		public <F extends IModFeature> F register(F feature) {
			features.add(feature);
			featureByRegistry.put(feature.getRegistry(), feature);
			return feature;
		}

		@Override
		public <T extends BlockEntity> FeatureTileType<T> tile(BlockEntityType.BlockEntitySupplier<T> constructor, String identifier, Supplier<Collection<? extends Block>> validBlocks) {
			return register(new FeatureTileType<>(this, moduleID, identifier, constructor, validBlocks));
		}

		@Override
		public <C extends AbstractContainerMenu> FeatureMenuType<C> container(IContainerFactory<C> factory, String identifier) {
			return register(new FeatureMenuType<>(this, moduleID, identifier, factory));
		}

		@Override
		public <E extends Entity> FeatureEntityType<E> entity(EntityType.EntityFactory<E> factory, MobCategory classification, String identifier) {
			return entity(factory, classification, identifier, (builder) -> builder);
		}

		@Override
		public <E extends Entity> FeatureEntityType<E> entity(EntityType.EntityFactory<E> factory, MobCategory classification, String identifier, UnaryOperator<EntityType.Builder<E>> consumer) {
			return entity(factory, classification, identifier, consumer, LivingEntity::createLivingAttributes);
		}

		@Override
		public <E extends Entity> FeatureEntityType<E> entity(EntityType.EntityFactory<E> factory, MobCategory classification, String identifier, UnaryOperator<EntityType.Builder<E>> consumer, Supplier<AttributeSupplier.Builder> attributes) {
			return register(new FeatureEntityType<>(this, moduleID, identifier, consumer, factory, classification, attributes));
		}

		@Override
		public Collection<IModFeature> getFeatures() {
			return features;
		}

		@Override
		public Collection<IModFeature> getFeatures(ResourceKey<? extends Registry<?>> type) {
			return featureByRegistry.get(type);
		}

		public void onRegister(RegisterEvent event) {
			for (Consumer<RegisterEvent> listener : registryListeners.get(event.getRegistryKey())) {
				listener.accept(event);
			}
		}

		public void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
			for (RegistryObject<Block> feature : getRegistry(Registry.BLOCK_REGISTRY).getEntries()) {
				if (feature.get() instanceof BlockBase<?> block) {
					block.clientSetupRenderers(event);
				}
			}
		}
	}
}
