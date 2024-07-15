/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.genetics.mutations;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.climate.IClimateProvider;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IMutationBuilder;
import forestry.api.genetics.IMutationCondition;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleSpecies;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

public abstract class Mutation implements IMutation, IMutationBuilder {
	private final IAlleleForestrySpecies firstParent;
	private final IAlleleForestrySpecies secondParent;
	private final IAllele[] template;
	private final int chance;

	private final List<IMutationCondition> mutationConditions = new ArrayList<>();
	private final List<Component> specialConditions = new ArrayList<>();

	private boolean isSecret = false;

	protected Mutation(IAlleleForestrySpecies firstParent, IAlleleForestrySpecies secondParent, IAllele[] template, int chance) {
		this.firstParent = firstParent;
		this.secondParent = secondParent;
		this.template = template;
		this.chance = chance;

		Preconditions.checkArgument(template[0] instanceof IAlleleSpecies);
	}

	@Override
	public Collection<Component> getSpecialConditions() {
		return specialConditions;
	}

	@Override
	public Mutation setIsSecret() {
		this.isSecret = true;
		return this;
	}

	@Override
	public Mutation restrictTemperature(TemperatureType temperature) {
		return restrictTemperature(temperature, temperature);
	}

	@Override
	public Mutation restrictTemperature(TemperatureType minTemperature, TemperatureType maxTemperature) {
		IMutationCondition mutationCondition = new MutationConditionTemperature(minTemperature, maxTemperature);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation restrictHumidity(HumidityType humidity) {
		return restrictHumidity(humidity, humidity);
	}

	@Override
	public Mutation restrictHumidity(HumidityType minHumidity, HumidityType maxHumidity) {
		IMutationCondition mutationCondition = new MutationConditionHumidity(minHumidity, maxHumidity);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation restrictBiomeType(TagKey<Biome> types) {
		IMutationCondition mutationCondition = new MutationConditionBiome(types);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation requireDay() {
		IMutationCondition mutationCondition = new MutationConditionDaytime(true);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation requireNight() {
		IMutationCondition mutationCondition = new MutationConditionDaytime(false);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation restrictDateRange(int startMonth, int startDay, int endMonth, int endDay) {
		IMutationCondition mutationCondition = new MutationConditionTimeLimited(startMonth, startDay, endMonth, endDay);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation requireResource(BlockState... acceptedBlockStates) {
		IMutationCondition mutationCondition = new MutationConditionRequiresResource(acceptedBlockStates);
		return addMutationCondition(mutationCondition);
	}

	@Override
	public Mutation addMutationCondition(IMutationCondition mutationCondition) {
		mutationConditions.add(mutationCondition);
		specialConditions.add(mutationCondition.getDescription());
		return this;
	}

	protected float getChance(Level world, BlockPos pos, IAllele firstParent, IAllele secondParent, IGenome firstGenome, IGenome secondGenome, IClimateProvider climate) {
		float mutationChance = chance;
		for (IMutationCondition mutationCondition : mutationConditions) {
			mutationChance *= mutationCondition.getChance(world, pos, firstParent, secondParent, firstGenome, secondGenome, climate);
			if (mutationChance == 0) {
				return 0;
			}
		}
		return mutationChance;
	}

	@Override
	public IAlleleSpecies getFirstParent() {
		return firstParent;
	}

	@Override
	public IAlleleSpecies getSecondParent() {
		return secondParent;
	}

	@Override
	public IAlleleSpecies getResultingSpecies() {
		return (IAlleleSpecies) template[0];
	}

	@Override
	public int getBaseChance() {
		return this.chance;
	}

	@Override
	public IAllele[] getTemplate() {
		return template;
	}

	@Override
	public boolean isPartner(IAllele allele) {
		return firstParent.getId().equals(allele.id()) || secondParent.getId().equals(allele.id());
	}

	@Override
	public IAllele getPartner(IAllele allele) {
		if (firstParent.getId().equals(allele.id())) {
			return secondParent;
		} else if (secondParent.getId().equals(allele.id())) {
			return firstParent;
		} else {
			throw new IllegalArgumentException("Tried to get partner for allele that is not part of this mutation.");
		}
	}

	@Override
	public boolean isSecret() {
		return isSecret;
	}

	@Override
	public String toString() {
		MoreObjects.ToStringHelper stringHelper = MoreObjects.toStringHelper(this)
				.add("first", firstParent)
				.add("second", secondParent)
				.add("result", template[0]);
		if (!specialConditions.isEmpty()) {
			stringHelper.add("conditions", getSpecialConditions());
		}
		return stringHelper.toString();
	}
}
