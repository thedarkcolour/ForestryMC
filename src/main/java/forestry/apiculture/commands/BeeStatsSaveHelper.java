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
package forestry.apiculture.commands;

import java.util.Collection;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import genetics.utils.AlleleUtils;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IApiaristTracker;
import forestry.api.apiculture.genetics.BeeChromosomes;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.genetics.IBreedingTracker;
import forestry.core.commands.IStatsSaveHelper;
import forestry.core.utils.StringUtil;
import forestry.core.utils.Translator;

public class BeeStatsSaveHelper implements IStatsSaveHelper {

	@Override
	public String getUnlocalizedSaveStatsString() {
		return "for.chat.command.forestry.bee.save.stats";
	}

	@Override
	public void addExtraInfo(Collection<String> statistics, IBreedingTracker breedingTracker) {
		IApiaristTracker tracker = (IApiaristTracker) breedingTracker;
		String discoveredLine = Component.translatable("for.chat.command.forestry.stats.save.key.discovered").append(":").getString();
		statistics.add(discoveredLine);
		statistics.add(StringUtil.line(discoveredLine.length()));

		String queen = Component.translatable("for.bees.grammar.queen.type").getString();
		String princess = Component.translatable("for.bees.grammar.princess.type").getString();
		String drone = Component.translatable("for.bees.grammar.drone.type").getString();
		statistics.add(queen + ":\t\t" + tracker.getQueenCount());
		statistics.add(princess + ":\t" + tracker.getPrincessCount());
		statistics.add(drone + ":\t\t" + tracker.getDroneCount());
		statistics.add("");
	}

	@Override
	public Collection<IAlleleBeeSpecies> getSpecies() {
		return AlleleUtils.filteredAlleles(BeeChromosomes.SPECIES);
	}

	@Override
	public String getFileSuffix() {
		return "bees";
	}

	@Override
	public IBreedingTracker getBreedingTracker(Level world, GameProfile gameProfile) {
		//TODO world cast
		return BeeManager.beeRoot.getBreedingTracker(world, gameProfile);
	}

}
