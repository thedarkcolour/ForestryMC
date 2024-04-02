package forestry.modules.features;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import net.minecraftforge.registries.RegistryObject;

public class FeatureEntityType<T extends Entity> extends ModFeature implements IEntityTypeFeature<T> {
	protected final UnaryOperator<EntityType.Builder<T>> consumer;
	protected final Supplier<AttributeSupplier.Builder> attributes;
	protected final EntityType.EntityFactory<T> factory;
	protected final MobCategory classification;
	private final RegistryObject<EntityType<T>> entityTypeObject;

	public FeatureEntityType(IFeatureRegistry registry, String moduleID, String identifier, UnaryOperator<EntityType.Builder<T>> consumer, EntityType.EntityFactory<T> factory, MobCategory classification, Supplier<AttributeSupplier.Builder> attributes) {
		super(moduleID, registry.getModId(), identifier);
		this.consumer = consumer;
		this.factory = factory;
		this.attributes = attributes;
		this.classification = classification;
		this.entityTypeObject = registry.getRegistry(Registry.ENTITY_TYPE_REGISTRY).register(identifier, () -> EntityType.Builder.of(factory, classification).build(this.modId + ":" + identifier));
	}

	@Override
	public AttributeSupplier.Builder createAttributes() {
		return attributes.get();
	}

	@Override
	public EntityType<T> entityType() {
		return entityTypeObject.get();
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistry() {
		return Registry.ENTITY_TYPE_REGISTRY;
	}
}
