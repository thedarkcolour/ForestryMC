package forestry.plugin;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility class for registering objects with unique identifiers.
 * Objects are stored in registration order, which should be consistent as plugins are always loaded in the same order.
 * If two objects with the same key are registered, an exception is thrown.
 *
 * @param <K> The type of key object used to uniquely identify an object. Typically ResourceLocation.
 * @param <I> The interface type of the object. If no interface exists, can be the same as T.
 * @param <V> The concrete type of the object. If there is more than one concrete type, can be the same as I.
 */
public class Registrar<K, I, V extends I> {
	protected final LinkedHashMap<K, V> values = new LinkedHashMap<>();
	protected final Class<I> interfaceType;

	public Registrar(Class<I> interfaceType) {
		this.interfaceType = interfaceType;
	}

	public I create(K key, V value) {
		if (this.values.containsKey(key)) {
			throw new IllegalStateException("A " + this.interfaceType.getSimpleName() + " already exists with key " + key + ": " + this.values.get(key));
		} else {
			this.values.put(key, value);
			return value;
		}
	}

	protected LinkedHashMap<K, V> getValues() {
		return this.values;
	}

	public ImmutableMap<K, V> build() {
		return ImmutableMap.copyOf(this.values);
	}

	public <T> ImmutableMap<K, T> build(Function<V, T> build) {
		return build((key, value) -> build.apply(value));
	}

	@SuppressWarnings("UnstableApiUsage")
	public <T> ImmutableMap<K, T> build(BiFunction<K, V, T> build) {
		HashMap<K, V> values = getValues();
		ImmutableMap.Builder<K, T> builder = ImmutableMap.builderWithExpectedSize(values.size());

		for (Map.Entry<K, V> entry : values.entrySet()) {
			K key = entry.getKey();
			builder.put(key, build.apply(key, entry.getValue()));
		}

		return builder.buildOrThrow();
	}
}
