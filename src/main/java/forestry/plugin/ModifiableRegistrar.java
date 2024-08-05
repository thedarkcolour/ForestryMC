package forestry.plugin;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Variant of {@link Registrar} that supports modifying objects that have already been created.
 * If a modification is applied to an object that does not exist, an exception is thrown.
 *
 * @param <K> The type of key object used to uniquely identify a builder.
 * @param <I> The interface type of the builder object. If no interface exists, can be the same as T.
 * @param <V> The concrete type of the builder object.
 */
public class ModifiableRegistrar<K, I, V extends I> extends Registrar<K, I, V> {
	private final HashMap<K, Consumer<I>> modifications = new HashMap<>();

	public ModifiableRegistrar(Class<I> interfaceType) {
		super(interfaceType);
	}

	public void modify(K key, Consumer<I> action) {
		this.modifications.merge(key, action, Consumer::andThen);
	}

	@Override
	public LinkedHashMap<K, V> getValues() {
		for (Map.Entry<K, Consumer<I>> entry : this.modifications.entrySet()) {
			V value = this.values.get(entry.getKey());

			// todo what if plugins want to optionally modify a type? what if plugins want to modify all types?
			if (value == null) {
				throw new IllegalStateException("Tried to modify non-existent " + this.interfaceType.getSimpleName() + " with ID " + entry.getKey());
			}

			entry.getValue().accept(value);
		}

		return this.values;
	}
}
