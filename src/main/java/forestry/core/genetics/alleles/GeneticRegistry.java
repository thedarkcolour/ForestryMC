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
package forestry.core.genetics.alleles;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.item.ItemStack;

import com.mojang.authlib.GameProfile;

import forestry.api.ForestryConstants;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.IGeneticRegistry;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.api.genetics.alleles.IAlleleHandler;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.core.features.CoreItems;
import forestry.core.genetics.ItemResearchNote.EnumNoteType;

import genetics.api.alleles.CategorizedValueAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.alleles.IValueAllele;

import forestry.api.genetics.IClassificationRegistry;
import forestry.api.genetics.IMutation;

public class GeneticRegistry implements IGeneticRegistry {

	/* ALLELES */
	private final LinkedHashMap<String, IFruitFamily> fruitMap = new LinkedHashMap<>(64);

	/*
	 * Internal Set of all alleleHandlers, which trigger when an allele or branch is registered
	 */
	private final Set<IAlleleHandler> alleleHandlers = new HashSet<>();

	public void registerAlleles(IAlleleRegistry registry) {
		registry.registerAlleles(SpeedAllele.values(),
				BeeChromosomes.SPEED,
				ButterflyChromosomes.SPEED
		);
		registry.registerAlleles(LifespanAllele.values(),
				BeeChromosomes.LIFESPAN,
				ButterflyChromosomes.LIFESPAN
		);
		registry.registerAlleles(ToleranceAllele.values(),
				BeeChromosomes.TEMPERATURE_TOLERANCE,
				BeeChromosomes.HUMIDITY_TOLERANCE,
				ButterflyChromosomes.TEMPERATURE_TOLERANCE,
				ButterflyChromosomes.HUMIDITY_TOLERANCE
		);
		registry.registerAlleles(FlowerTypeAllele.values(),
				BeeChromosomes.FLOWER_TYPE,
				ButterflyChromosomes.FLOWER_PROVIDER);

		for (int i = 1; i <= 10; i++) {
			registry.registerAllele("i", i + "d", i, true,
					TreeChromosomes.GIRTH,
					ButterflyChromosomes.FERTILITY
			);
		}

		Map<Boolean, IValueAllele<Boolean>> booleans = new HashMap<>();
		booleans.put(true, new CategorizedValueAllele<>(ForestryConstants.MOD_ID, "bool", "true", true, false));
		booleans.put(false, new CategorizedValueAllele<>(ForestryConstants.MOD_ID, "bool", "false", false, false));
		for (IValueAllele<Boolean> alleleBoolean : booleans.values()) {
			registry.registerAllele(alleleBoolean,
					BeeChromosomes.NEVER_SLEEPS,
					BeeChromosomes.TOLERATES_RAIN,
					BeeChromosomes.CAVE_DWELLING,
					ButterflyChromosomes.NEVER_SLEEPS,
					ButterflyChromosomes.TOLERATES_RAIN,
					ButterflyChromosomes.FIRE_RESISTANT
			);
		}
	}

	/* FRUIT FAMILIES */
	@Override
	public void registerFruitFamily(IFruitFamily family) {
		fruitMap.put(family.getUID(), family);
		for (IAlleleHandler handler : this.alleleHandlers) {
			handler.onRegisterFruitFamily(family);
		}
	}

	@Override
	public Map<String, IFruitFamily> getRegisteredFruitFamilies() {
		return Collections.unmodifiableMap(fruitMap);
	}

	@Override
	public IFruitFamily getFruitFamily(String uid) {
		return fruitMap.get(uid);
	}

	/* ALLELE HANDLERS */
	@Override
	public void registerAlleleHandler(IAlleleHandler handler) {
		this.alleleHandlers.add(handler);
	}

	/* RESEARCH */
	@Override
	public ItemStack getSpeciesNoteStack(GameProfile researcher, IAlleleForestrySpecies species) {
		return EnumNoteType.createSpeciesNoteStack(CoreItems.RESEARCH_NOTE.item(), researcher, species);
	}

	@Override
	public ItemStack getMutationNoteStack(GameProfile researcher, IMutation mutation) {
		return EnumNoteType.createMutationNoteStack(CoreItems.RESEARCH_NOTE.item(), researcher, mutation);
	}
}
