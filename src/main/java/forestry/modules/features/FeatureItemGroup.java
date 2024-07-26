package forestry.modules.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import net.minecraft.world.item.Item;

import forestry.api.core.IItemSubtype;

public class FeatureItemGroup<I extends Item, S extends IItemSubtype> extends FeatureGroup<FeatureItemGroup.Builder<I, S>, FeatureItem<I>, S> {
	public FeatureItemGroup(Builder<I, S> builder) {
		super(builder);
	}

	public Collection<I> getItems() {
		ArrayList<I> items = new ArrayList<>(featureByType.size());
		for (FeatureItem<I> value : featureByType.values()) {
			items.add(value.item());
		}
		return items;
	}

	public Item[] itemArray() {
		return getItems().toArray(new Item[0]);
	}

	@Override
	protected FeatureItem<I> createFeature(Builder<I, S> builder, S type) {
		return builder.registry.item(() -> builder.constructor.apply(type), builder.getIdentifier(type));
	}

	public I item(S variant) {
		return get(variant).item();
	}

	public static class Builder<I extends Item, S extends IItemSubtype> extends FeatureGroup.Builder<S, FeatureItemGroup<I, S>> {
		private final IFeatureRegistry registry;
		private final Function<S, I> constructor;

		public Builder(IFeatureRegistry registry, Function<S, I> constructor) {
			super(registry);
			this.registry = registry;
			this.constructor = constructor;
		}

		@Override
		public FeatureItemGroup<I, S> create() {
			return new FeatureItemGroup<>(this);
		}
	}
}
