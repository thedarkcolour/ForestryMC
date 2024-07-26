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
package forestry.lepidopterology;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import forestry.api.IForestryApi;
import forestry.api.arboriculture.ILeafTickHandler;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.lepidopterology.entities.EntityButterfly;

public class ButterflySpawner implements ILeafTickHandler {
	@Override
	public boolean onRandomLeafTick(ITree tree, Level world, RandomSource rand, BlockPos pos, boolean isDestroyed) {
		if (!world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
			return false;
		}

		if (rand.nextFloat() >= tree.getGenome().getActiveValue(TreeChromosomes.SAPPINESS) * tree.getGenome().getActiveValue(TreeChromosomes.YIELD)) {
			return false;
		}

		IButterfly spawn = (IButterfly) IForestryApi.INSTANCE.getGeneticManager().createRandomIndividual(ForestrySpeciesTypes.BUTTERFLY, rand, ButterflyLifeStage.BUTTERFLY);
		float rarity;
		IButterflySpecies activeSpecies = spawn.getGenome().getActiveValue(ButterflyChromosomes.SPECIES);
		if (!ModuleLepidopterology.spawnRaritys.containsKey(activeSpecies.id().getPath())) {
			rarity = activeSpecies.getRarity();
		} else {
			rarity = ModuleLepidopterology.spawnRaritys.get(activeSpecies.id().getPath());
		}

		if (rand.nextFloat() >= rarity * 0.5f) {
			return false;
		}

		if (EntityButterfly.isMaxButterflyCluster(Vec3.atCenterOf(pos), world)) {
			return false;
		}

		if (!spawn.canSpawn(world, pos.getX(), pos.getY(), pos.getZ())) {
			return false;
		}

		if (world.isEmptyBlock(pos.north())) {
			ButterflyUtils.attemptButterflySpawn(world, spawn, pos.north());
		} else if (world.isEmptyBlock(pos.south())) {
			ButterflyUtils.attemptButterflySpawn(world, spawn, pos.south());
		} else if (world.isEmptyBlock(pos.west())) {
			ButterflyUtils.attemptButterflySpawn(world, spawn, pos.west());
		} else if (world.isEmptyBlock(pos.east())) {
			ButterflyUtils.attemptButterflySpawn(world, spawn, pos.east());
		}

		return false;
	}

}
