package forestry.plugin;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutationCondition;
import forestry.api.plugin.IMutationBuilder;
import forestry.api.plugin.IMutationsRegistration;
import forestry.core.genetics.mutations.Mutation;

public class MutationsRegistration implements IMutationsRegistration {
	private final HashMap<MutationPair, MutationBuilder> mutations = new HashMap<>();

	@Override
	public IMutationBuilder add(ResourceLocation firstParent, ResourceLocation secondParent, int chance) {
		Preconditions.checkArgument(!firstParent.equals(secondParent), "Cannot have a mutation between two of the same species.");

		MutationPair pair = new MutationPair(firstParent, secondParent);

		// order does not matter in mutations
		if (mutations.get(pair) == null && mutations.get(new MutationPair(secondParent, firstParent)) == null) {
			MutationBuilder mutation = new MutationBuilder(pair);
			mutation.setChance(chance);
			mutations.put(pair, mutation);
			return mutation;
		} else {
			throw new IllegalStateException("A mutation with the given parents was already registered, use IMutationsRegistration#get instead: " + pair);
		}
	}

	@Override
	public IMutationBuilder get(ResourceLocation firstParent, ResourceLocation secondParent) {
		Preconditions.checkArgument(!firstParent.equals(secondParent), "Cannot have a mutation between two of the same species.");

		mutations
	}

	private record MutationPair(ResourceLocation first, ResourceLocation second) {
	}

	private static class MutationBuilder implements IMutationBuilder {
		private final ArrayList<IMutationCondition> conditions = new ArrayList<>();
		private final MutationPair pair;
		private int chance = -1;

		private MutationBuilder(MutationPair pair) {
			this.pair = pair;
		}

		@Override
		public IMutationBuilder restrictTemperature(TemperatureType temperature) {
			return this;
		}

		@Override
		public IMutationBuilder restrictTemperature(TemperatureType minTemperature, TemperatureType maxTemperature) {
			return this;
		}

		@Override
		public IMutationBuilder restrictHumidity(HumidityType humidity) {
			return this;
		}

		@Override
		public IMutationBuilder restrictHumidity(HumidityType minHumidity, HumidityType maxHumidity) {
			return this;
		}

		@Override
		public IMutationBuilder restrictBiomeType(TagKey<Biome> types) {
			return this;
		}

		@Override
		public IMutationBuilder restrictDateRange(int startMonth, int startDay, int endMonth, int endDay) {
			return this;
		}

		@Override
		public IMutationBuilder requireDay() {
			return this;
		}

		@Override
		public IMutationBuilder requireNight() {
			return this;
		}

		@Override
		public IMutationBuilder requireResource(BlockState... acceptedBlockStates) {
			return this;
		}

		@Override
		public IMutationBuilder addMutationCondition(IMutationCondition condition) {
			this.conditions.add(condition);
			return this;
		}

		@Override
		public IMutationBuilder setChance(int chance) {
			this.chance = chance;
			return this;
		}

		public Mutation build() {
			return new Mutation();
		}
	}
}
