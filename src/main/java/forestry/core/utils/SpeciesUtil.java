package forestry.core.utils;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;

import net.minecraftforge.common.util.Lazy;

import forestry.api.IForestryApi;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.climate.IClimateProvider;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.genetics.alleles.IRegistryAllele;
import forestry.api.genetics.alleles.IRegistryChromosome;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.api.plugin.IGenomeBuilder;
import forestry.core.config.Config;

public class SpeciesUtil {
	public static final Lazy<IBeeSpeciesType> BEE_TYPE = Lazy.of(() -> IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.BEE, IBeeSpeciesType.class));
	public static final Lazy<ITreeSpeciesType> TREE_TYPE = Lazy.of(() -> IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.TREE, ITreeSpeciesType.class));
	public static final Lazy<IButterflySpeciesType> BUTTERFLY_TYPE = Lazy.of(() -> IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.BUTTERFLY, IButterflySpeciesType.class));

	public static ITreeSpecies getTreeSpecies(ResourceLocation id) {
		return TREE_TYPE.get().getSpecies(id);
	}

	public static List<ITreeSpecies> getAllTreeSpecies() {
		return TREE_TYPE.get().getAllSpecies();
	}

	public static IBeeSpecies getBeeSpecies(ResourceLocation id) {
		return BEE_TYPE.get().getSpecies(id);
	}

	public static List<IBeeSpecies> getAllBeeSpecies() {
		return BEE_TYPE.get().getAllSpecies();
	}

	public static IButterflySpecies getButterflySpecies(ResourceLocation id) {
		return BUTTERFLY_TYPE.get().getSpecies(id);
	}

	public static List<IButterflySpecies> getAllButterflySpecies() {
		return BUTTERFLY_TYPE.get().getAllSpecies();
	}

	// Retrieves a species of an arbitrary type based on its allele. Does not null check.
	@Nullable
	public static ISpecies<?> getAnySpecies(ResourceLocation id) {
		@SuppressWarnings("unchecked")
		IRegistryAllele<ISpecies<?>> allele = ((IRegistryAllele<ISpecies<?>>) ForestryAlleles.REGISTRY.getAllele(id));
		return allele == null ? null : allele.value();
	}

	@Nullable
	public static <I extends IIndividual> Tag serializeIndividual(I individual) {
		@SuppressWarnings("unchecked")
		Codec<I> individualCodec = (Codec<I>) individual.getType().getIndividualCodec();
		return individualCodec.encodeStart(NbtOps.INSTANCE, individual).result().orElse(null);
	}

	public static IButterfly deserializeButterfly(Tag nbt) {
		return deserializeIndividual(BUTTERFLY_TYPE.get(), nbt);
	}

	public static <I extends IIndividual> I deserializeIndividual(ISpeciesType<?, I> type, Tag nbt) {
		return type.getIndividualCodec().decode(NbtOps.INSTANCE, nbt).result().orElseThrow().getFirst();
	}

	@Nullable
	public static <S extends ISpecies<?>> ImmutableList<AllelePair<?>> mutateSpecies(Level level, BlockPos pos, @Nullable GameProfile profile, IGenome parent1, IGenome parent2, IRegistryChromosome<S> speciesChromosome, IMutationChanceGetter<S> chanceGetter) {
		IGenome firstGenome;
		IGenome secondGenome;

		S firstParent;
		S secondParent;

		if (level.random.nextBoolean()) {
			firstParent = parent1.getActiveValue(speciesChromosome);
			secondParent = parent2.getInactiveValue(speciesChromosome);

			firstGenome = parent1;
			secondGenome = parent2;
		} else {
			firstParent = parent2.getActiveValue(speciesChromosome);
			secondParent = parent1.getInactiveValue(speciesChromosome);

			firstGenome = parent2;
			secondGenome = parent1;
		}

		ISpeciesType<?, ?> speciesType = parent1.getActiveValue(speciesChromosome).getType();
		IBreedingTracker tracker = profile == null ? null : speciesType.getBreedingTracker(level, profile);
		IClimateProvider climate = IForestryApi.INSTANCE.getClimateManager().getDefaultClimate(level, pos);

		for (IMutation<?> mutation : speciesType.getMutations().getAllMutations(level.random)) {
			float chance = chanceGetter.getChance(mutation, level, pos, firstParent, secondParent, firstGenome, secondGenome, climate);
			if (chance <= 0) {
				continue;
			}
			if (tracker != null && tracker.isResearched(mutation)) {
				float mutationBoost = chance * (Config.researchMutationBoostMultiplier - 1.0f);
				mutationBoost = Math.min(Config.maxResearchMutationBoostPercent, mutationBoost);
				chance += mutationBoost;
			}
			if (chance > level.random.nextFloat() * 100) {
				if (tracker != null) {
					tracker.registerMutation(mutation);
				}
				return mutation.getResult().getDefaultGenome().getAllelePairs();
			}
		}

		return null;
	}

	@FunctionalInterface
	public interface IMutationChanceGetter<S extends ISpecies<?>> {
		float getChance(IMutation<?> mutation, Level level, BlockPos pos, S firstParent, S secondParent, IGenome firstGenome, IGenome secondGenome, IClimateProvider climate);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <I extends IIndividual> I createOffspring(RandomSource rand, IGenome self, IGenome mate, ISpeciesMutator mutator, Function<IGenome, I> individualFactory) {
		IKaryotype karyotype = self.getKaryotype();
		IGenomeBuilder genome = karyotype.createGenomeBuilder();
		ImmutableList<AllelePair<?>> parent1 = self.getAllelePairs();
		ImmutableList<AllelePair<?>> parent2 = mate.getAllelePairs();

		// Check for mutation. Replace one of the parents with the mutation
		// template if mutation occurred.
		ImmutableList<AllelePair<?>> mutated1 = mutator.mutateSpecies(self, mate);
		if (mutated1 != null) {
			parent1 = mutated1;
		}
		ImmutableList<AllelePair<?>> mutated2 = mutator.mutateSpecies(mate, self);
		if (mutated2 != null) {
			parent2 = mutated2;
		}
		ImmutableList<IChromosome<?>> chromosomes = karyotype.getChromosomes();
		for (int i = 0; i < chromosomes.size(); i++) {
			IChromosome<?> chromosome = chromosomes.get(i);
			// unchecked due to generics being a pain
			AllelePair parent = parent1.get(i);
			genome.setUnchecked(chromosome, parent.inheritOther(rand, parent2.get(i)));
		}

		return individualFactory.apply(genome.build());
	}

	@FunctionalInterface
	public interface ISpeciesMutator {
		@Nullable
		ImmutableList<AllelePair<?>> mutateSpecies(IGenome p1, IGenome p2);
	}
}
