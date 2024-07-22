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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateProvider;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.IMutationCondition;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

public abstract class Mutation<S extends ISpecies<?>> implements IMutation<S> {
	private final S firstParent;
	private final S secondParent;
	private final S result;
	private final IAllele[] template;
	private final int chance;

	private final List<IMutationCondition> mutationConditions = new ArrayList<>();
	private final List<Component> specialConditions = new ArrayList<>();

	protected Mutation(S firstParent, S secondParent, IAllele[] template, int chance) {
		this.firstParent = firstParent;
		this.secondParent = secondParent;
		this.result = ((IValueAllele<S>) template[0]).value();
		this.template = template;
		this.chance = chance;
	}

	@Override
	public Collection<Component> getSpecialConditions() {
		return specialConditions;
	}

	protected float getChance(Level world, BlockPos pos, IAllele firstParent, IAllele secondParent, IGenome firstGenome, IGenome secondGenome, ClimateState climate) {
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
	public S getFirstParent() {
		return this.firstParent;
	}

	@Override
	public S getSecondParent() {
		return this.secondParent;
	}

	@Override
	public S getResult() {
		return this.result;
	}

	@Override
	public int getBaseChance() {
		return this.chance;
	}

	@Override
	public boolean isPartner(ISpecies<?> other) {
		return this.firstParent.id().equals(other.id()) || this.secondParent.id().equals(other.id());
	}

	@Override
	public ISpecies<?> getPartner(ISpecies<?> allele) {
		if (firstParent.id().equals(allele.id())) {
			return secondParent;
		} else if (secondParent.id().equals(allele.id())) {
			return firstParent;
		} else {
			throw new IllegalArgumentException("Tried to get partner for allele that is not part of this mutation.");
		}
	}

	@Override
	public boolean isSecret() {
		return this.result.isSecret();
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
