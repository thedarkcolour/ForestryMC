package forestry.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutationCondition;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IMutationBuilder;
import forestry.api.plugin.IMutationsRegistration;
import forestry.core.genetics.mutations.Mutation;
import forestry.core.genetics.mutations.MutationConditionBiome;
import forestry.core.genetics.mutations.MutationConditionDaytime;
import forestry.core.genetics.mutations.MutationConditionHumidity;
import forestry.core.genetics.mutations.MutationConditionRequiresResource;
import forestry.core.genetics.mutations.MutationConditionTemperature;
import forestry.core.genetics.mutations.MutationConditionTimeLimited;

public class MutationsRegistration implements IMutationsRegistration {
	private final HashMap<MutationPair, MutationBuilder> mutations = new HashMap<>();
	private final ResourceLocation speciesId;

	public MutationsRegistration(ResourceLocation speciesId) {
		this.speciesId = speciesId;
	}

	@Override
	public IMutationBuilder add(ResourceLocation firstParent, ResourceLocation secondParent, int chance) {
		Preconditions.checkArgument(!firstParent.equals(secondParent), "Cannot have a mutation between two of the same species");

		MutationPair pair = new MutationPair(firstParent, secondParent);

		// order does not matter in mutations
		if (mutations.get(pair) == null && mutations.get(new MutationPair(secondParent, firstParent)) == null) {
			MutationBuilder mutation = new MutationBuilder(pair, this.speciesId);
			mutation.setChance(chance);
			mutations.put(pair, mutation);
			return mutation;
		} else {
			throw new IllegalStateException("A mutation with the given parents was already registered, use IMutationsRegistration#get instead: " + pair);
		}
	}

	@Override
	public IMutationBuilder get(ResourceLocation firstParent, ResourceLocation secondParent) {
		Preconditions.checkArgument(!firstParent.equals(secondParent), "Cannot have a mutation between two of the same species");

		MutationBuilder mutation = mutations.get(new MutationPair(firstParent, secondParent));
		if (mutation == null) {
			mutation = mutations.get(new MutationPair(secondParent, firstParent));
		}
		return mutation;
	}

	private record MutationPair(ResourceLocation first, ResourceLocation second) {
	}

	public static class MutationBuilder implements IMutationBuilder {
		private final ArrayList<IMutationCondition> conditions = new ArrayList<>();
		private final MutationPair pair;
		private final ResourceLocation result;
		private final ImmutableMap.Builder<IChromosome<?>, IAllele> extraAlleles = new ImmutableMap.Builder<>();
		private int chance = -1;

		private MutationBuilder(MutationPair pair, ResourceLocation result) {
			this.pair = pair;
			this.result = result;
		}

		@Override
		public IMutationBuilder restrictTemperature(TemperatureType temperature) {
			this.conditions.add(new MutationConditionTemperature(temperature, temperature));
			return this;
		}

		@Override
		public IMutationBuilder restrictTemperature(TemperatureType minTemperature, TemperatureType maxTemperature) {
			this.conditions.add(new MutationConditionTemperature(minTemperature, maxTemperature));
			return this;
		}

		@Override
		public IMutationBuilder restrictHumidity(HumidityType humidity) {
			this.conditions.add(new MutationConditionHumidity(humidity, humidity));
			return this;
		}

		@Override
		public IMutationBuilder restrictHumidity(HumidityType minHumidity, HumidityType maxHumidity) {
			this.conditions.add(new MutationConditionHumidity(minHumidity, maxHumidity));
			return this;
		}

		@Override
		public IMutationBuilder restrictBiomeType(TagKey<Biome> types) {
			this.conditions.add(new MutationConditionBiome(types));
			return this;
		}

		@Override
		public IMutationBuilder restrictDateRange(int startMonth, int startDay, int endMonth, int endDay) {
			this.conditions.add(new MutationConditionTimeLimited(startMonth, startDay, endMonth, endDay));
			return this;
		}

		@Override
		public IMutationBuilder requireDay() {
			this.conditions.add(new MutationConditionDaytime(true));
			return this;
		}

		@Override
		public IMutationBuilder requireNight() {
			this.conditions.add(new MutationConditionDaytime(false));
			return this;
		}

		@Override
		public IMutationBuilder requireResource(BlockState... acceptedBlockStates) {
			this.conditions.add(new MutationConditionRequiresResource(acceptedBlockStates));
			return this;
		}

		@Override
		public IMutationBuilder addMutationCondition(IMutationCondition condition) {
			this.conditions.add(condition);
			return this;
		}

		@Override
		public <A extends IAllele> IMutationBuilder addSpecialAllele(IChromosome<A> chromosome, A allele) {
			this.extraAlleles.put(chromosome, allele);
			return this;
		}

		@Override
		public IMutationBuilder setChance(int chance) {
			this.chance = chance;
			return this;
		}

		@Override
		public <S extends ISpecies<?>> Mutation<S> build(ISpeciesType<S, ?> speciesType) {
			return new Mutation<>(speciesType, speciesType.getSpecies(this.pair.first), speciesType.getSpecies(this.pair.second), speciesType.getSpecies(this.result), this.extraAlleles.build(), this.chance, this.conditions);
		}
	}
}
