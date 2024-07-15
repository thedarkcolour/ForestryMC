package forestry.api.genetics.alleles;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import forestry.api.IForestryApi;

/**
 * A pair of an active allele and an inactive allele, for use in a {@link forestry.api.genetics.IGenome}.
 *
 * @param type     The chromosome of this pair
 * @param active   The active allele for the chromosome
 * @param inactive The inactive allele for the chromosome
 */
public record ChromosomePair(IChromosome<?> type, IAllele active, IAllele inactive) {
	public static final Codec<ChromosomePair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IChromosome.CODEC.fieldOf("type").forGetter(ChromosomePair::type),
			IAllele.CODEC.fieldOf("active").forGetter(ChromosomePair::active),
			IAllele.CODEC.fieldOf("inactive").forGetter(ChromosomePair::inactive)
	).apply(instance, ChromosomePair::new));

	@Nullable
	public static IAllele getActiveAllele(CompoundTag nbt) {
		ResourceLocation alleleUid = ResourceLocation.tryParse(nbt.getString("active"));
		return alleleUid == null ? null : IForestryApi.INSTANCE.getAlleleFactory().getAllele(alleleUid);
	}

	@Nullable
	public static IAllele getInactiveAllele(CompoundTag nbt) {
		ResourceLocation alleleUid = ResourceLocation.tryParse(nbt.getString("inactive"));
		return alleleUid == null ? null : IForestryApi.INSTANCE.getAlleleFactory().getAllele(alleleUid);
	}

	/**
	 * Creates a new chromosome out of the alleles of this chromosome and the other chromosome.
	 * It always uses one allele from this and one from the other chromosome to create the new chromosome.
	 *
	 * @param rand  The instance of random it should uses to figure out which of the two alleles if should use.
	 * @param other The other chromosome that this chromosome uses to create the new one.
	 */
	public ChromosomePair inheritOther(RandomSource rand, ChromosomePair other) {
		IAllele firstChoice = rand.nextBoolean() ? this.active() : this.inactive();
		IAllele secondChoice = rand.nextBoolean() ? other.active() : other.inactive();

		if (rand.nextBoolean()) {
			return ChromosomePair.create(type, firstChoice, secondChoice);
		} else {
			return ChromosomePair.create(type, secondChoice, firstChoice);
		}
	}

	/**
	 * @return A chromosome pair of two alleles according to Mendelian inheritance.
	 */
	public static ChromosomePair create(IChromosome<?> chromosome, IAllele firstAllele, IAllele secondAllele) {
		return new ChromosomePair(chromosome, getActiveAllele(firstAllele, secondAllele), getInactiveAllele(firstAllele, secondAllele));
	}

	private static IAllele getActiveAllele(IAllele firstAllele, IAllele secondAllele) {
		if (firstAllele.dominant()) {
			return firstAllele;
		}
		if (secondAllele.dominant()) {
			return secondAllele;
		}
		// Leaves only the case of both being recessive
		return firstAllele;
	}

	private static IAllele getInactiveAllele(IAllele firstAllele, IAllele secondAllele) {
		if (!secondAllele.dominant()) {
			return secondAllele;
		}
		if (!firstAllele.dominant()) {
			return firstAllele;
		}
		// Leaves only the case of both being dominant
		return secondAllele;
	}
}
