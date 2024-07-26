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
 * @param active   The active allele for the chromosome
 * @param inactive The inactive allele for the chromosome
 * @param <A>      The generic type of allele this pair holds.
 */
public record AllelePair<A extends IAllele>(A active, A inactive) {
	public static final Codec<AllelePair<?>> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			IAllele.CODEC.fieldOf("active").forGetter(AllelePair::active),
			IAllele.CODEC.fieldOf("inactive").forGetter(AllelePair::inactive)
	).apply(instance, AllelePair::new));

	@Nullable
	public static IAllele getActiveAllele(CompoundTag nbt) {
		ResourceLocation alleleUid = ResourceLocation.tryParse(nbt.getString("active"));
		return alleleUid == null ? null : IForestryApi.INSTANCE.getAlleleManager().getAllele(alleleUid);
	}

	// todo are these needed?
	@Nullable
	public static IAllele getInactiveAllele(CompoundTag nbt) {
		ResourceLocation alleleUid = ResourceLocation.tryParse(nbt.getString("inactive"));
		return alleleUid == null ? null : IForestryApi.INSTANCE.getAlleleManager().getAllele(alleleUid);
	}

	/**
	 * Creates an allele pair where both the active and inactive alleles are the same.
	 *
	 * @param allele The allele.
	 * @param <A>    The generic type of the allele class.
	 * @return A new pair with the same value for both its active and inactive allele.
	 */
	public static <A extends IAllele> AllelePair<A> both(A allele) {
		return new AllelePair<>(allele, allele);
	}

	/**
	 * Creates a new chromosome out of the alleles of this chromosome and the other chromosome.
	 * It always uses one allele from this and one from the other chromosome to create the new chromosome.
	 *
	 * @param rand  The instance of random it should uses to figure out which of the two alleles if should use.
	 * @param other The other chromosome that this chromosome uses to create the new one.
	 */
	public AllelePair<A> inheritOther(RandomSource rand, AllelePair<A> other) {
		A firstChoice = rand.nextBoolean() ? this.active() : this.inactive();
		A secondChoice = rand.nextBoolean() ? other.active() : other.inactive();

		if (rand.nextBoolean()) {
			return AllelePair.create(firstChoice, secondChoice);
		} else {
			return AllelePair.create(secondChoice, firstChoice);
		}
	}

	/**
	 * @return {@code true} if the active allele is the same as the inactive allele.
	 */
	public boolean isSameAlleles() {
		return this.active == this.inactive;
	}

	/**
	 * A new chromosome pair with the active allele is the first dominant allele and the inactive allele is the other allele.
	 * THIS IS DIFFERENT THAN THE CONSTRUCTOR.
	 *
	 * @return A chromosome pair of two alleles in order of allele dominance.
	 */
	public static <A extends IAllele> AllelePair<A> create(A firstAllele, A secondAllele) {
		return new AllelePair<>(getActiveAllele(firstAllele, secondAllele), getInactiveAllele(firstAllele, secondAllele));
	}

	private static <A extends IAllele> A getActiveAllele(A firstAllele, A secondAllele) {
		if (firstAllele.dominant()) {
			return firstAllele;
		}
		if (secondAllele.dominant()) {
			return secondAllele;
		}
		// Leaves only the case of both being recessive
		return firstAllele;
	}

	private static <A extends IAllele> A getInactiveAllele(A firstAllele, A secondAllele) {
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
