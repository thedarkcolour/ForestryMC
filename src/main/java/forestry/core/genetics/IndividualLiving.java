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
package forestry.core.genetics;

import net.minecraft.world.level.Level;

import forestry.api.genetics.IIndividualLiving;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IIntegerChromosome;

public abstract class IndividualLiving<S extends ISpecies<I>, I extends IIndividualLiving, T extends ISpeciesType<S, I>> extends Individual<S, I, T> implements IIndividualLiving {
	private static final String NBT_HEALTH = "Health";
	private static final String NBT_MAX_HEALTH = "MaxH";

	protected int health;
	protected int maxHealth;

	protected IndividualLiving(IGenome genome) {
		super(genome);

		int health = genome.getActiveValue(getLifespanChromosome());
		this.health = health;
		this.maxHealth = health;
	}

	protected abstract IIntegerChromosome getLifespanChromosome();

	/* GENERATION */
	@Override
	public boolean isAlive() {
		return health > 0;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public final void setHealth(int health) {
		if (health < 0) {
			health = 0;
		} else if (health > getMaxHealth()) {
			health = getMaxHealth();
		}

		this.health = health;
	}

	@Override
	public int getMaxHealth() {
		return this.maxHealth;
	}

	@Override
	public void age(Level level, float lifespanModifier) {
		if (lifespanModifier < 0.001f) {
			setHealth(0);
			return;
		}

		float ageModifier = 1.0f / lifespanModifier;

		while (ageModifier > 1.0f) {
			decreaseHealth();
			ageModifier--;
		}
		if (level.random.nextFloat() < ageModifier) {
			decreaseHealth();
		}
	}

	@Override
	public I copy() {
		I individual = super.copy();
		individual.setHealth(this.getHealth());
		((IndividualLiving<?, ?, ?>) individual).maxHealth = this.maxHealth;
		return individual;
	}

	private void decreaseHealth() {
		if (health > 0) {
			setHealth(health - 1);
		}
	}
}
