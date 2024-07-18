package forestry.api.genetics.alleles;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import forestry.api.genetics.ISpecies;

/**
 * Used to retrieve interned instances of alleles. Also exposes methods for creating chromosomes.
 * Store alleles obtained from these method calls in a static class like Forestry does in {@link ForestryAlleles}.
 * This class is not thread-safe.
 *
 * @see ForestryAlleles For preexisting alleles. Use those whenever possible, copying them to other fields is fine.
 */
public interface IAlleleManager {
	/**
	 * @return A recessive integer allele with the given value.
	 */
	default IIntegerAllele intAllele(int value) {
		return intAllele(value, false);
	}

	/**
	 * Creates a new allele for the given value, or retrieves a pre-existing allele if one has already been made.
	 *
	 * @param value    The value of this allele.
	 * @param dominant If this allele is dominant.
	 * @return A new allele, or an existing one if this allele has already been created elsewhere.
	 */
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

	default <V> IValueAllele<V> valueAllele(V value, IAlleleNaming<V> naming) {
		return valueAllele(value, false, naming);
	}

	<V> IValueAllele<V> valueAllele(V value, boolean dominant, IAlleleNaming<V> naming);

	/**
	 * @return A new boolean allele.
	 */
	IBooleanAllele booleanAllele(boolean value, boolean dominant);

	/**
	 * @return The allele codec, which serializes/deserializes using the allele's ID.
	 */
	Codec<IAllele> alleleCodec();

	/**
	 * @return The chromosome codec, which serializes/deserializes using the chromosome's ID.
	 */
	Codec<IChromosome<?>> chromosomeCodec();

	/**
	 * @return An allele with the given ID, or {@code null} if no allele has been created with that ID.
	 */
	@Nullable
	IAllele getAllele(ResourceLocation id);

	IFloatChromosome floatChromosome(ResourceLocation id);

	IIntegerChromosome intChromosome(ResourceLocation id);

	IBooleanChromosome booleanChromosome(ResourceLocation id);

	<V> IValueChromosome<V> valueChromosome(ResourceLocation id, Class<V> valueClass);

	<S extends ISpecies<?>> ISpeciesChromosome<S> speciesChromosome(ResourceLocation id, Class<S> speciesClass);
}
