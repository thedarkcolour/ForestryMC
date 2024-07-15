package forestry.api.genetics.alleles;

import javax.annotation.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

/**
 * Used to retrieve interned instances of alleles.
 * Store alleles obtained from these method calls in a static class like Forestry does.
 * This class is not thread-safe.
 *
 * @see ForestryAlleles For preexisting alleles. Use those whenever possible, copying them to other fields is fine.
 */
public interface IAlleleRegistry {
	default IIntegerAllele intAllele(int value) {
		return intAllele(value, false);
	}

	IIntegerAllele intAllele(int value, boolean dominant);

	default IFloatAllele floatAllele(float value) {
		return floatAllele(value, false);
	}

	IFloatAllele floatAllele(float value, boolean dominant);

	default <V extends INamedValue> IValueAllele<V> valueAllele(V value) {
		return valueAllele(value, false);
	}

	default <V extends INamedValue> IValueAllele<V> valueAllele(V value, boolean dominant) {
		return valueAllele(value, dominant, V::id);
	}

	default <V> IValueAllele<V> valueAllele(V value, Function<V, ResourceLocation> naming) {
		return valueAllele(value, false, naming);
	}

	<V> IValueAllele<V> valueAllele(V value, boolean dominant, Function<V, ResourceLocation> naming);

	/**
	 * @return A new boolean allele.
	 */
	IBooleanAllele booleanAllele(boolean value, boolean dominant);

	/**
	 * @return The allele codec, which serializes/deserializes using the allele's ID.
	 */
	Codec<IAllele> byNameCodec();

	/**
	 * @return An allele with the given ID, or {@code null} if no allele has been created with that ID.
	 */
	@Nullable
	IAllele getAllele(ResourceLocation id);
}
