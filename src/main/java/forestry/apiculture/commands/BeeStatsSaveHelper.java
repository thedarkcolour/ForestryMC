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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.IApiaristTracker;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpecies;
import forestry.core.commands.IStatsSaveHelper;
import forestry.core.utils.SpeciesUtil;
import forestry.core.utils.StringUtil;

public class BeeStatsSaveHelper implements IStatsSaveHelper {
	@Override
	public String getTranslationKey() {
		return "for.chat.command.forestry.bee.save.stats";
	}

	@Override
	public void addExtraInfo(Collection<Component> statistics, IBreedingTracker breedingTracker) {
		IApiaristTracker tracker = (IApiaristTracker) breedingTracker;
		Component discoveredLine = Component.translatable("for.chat.command.forestry.stats.save.key.discovered").append(":");
		statistics.add(discoveredLine);
		// todo lines
		//statistics.add(StringUtil.line(discoveredLine.length()));

		MutableComponent queen = Component.translatable("for.bees.grammar.queen.type");
		MutableComponent princess = Component.translatable("for.bees.grammar.princess.type");
		MutableComponent drone = Component.translatable("for.bees.grammar.drone.type");
		statistics.add(queen.append(":\t\t" + tracker.getQueenCount()));
		// why does this one only have 1 tab?
		statistics.add(princess.append(":\t" + tracker.getPrincessCount()));
		statistics.add(drone.append(":\t\t" + tracker.getDroneCount()));
		statistics.add(Component.literal(""));
	}

	@Override
	public Collection<? extends ISpecies<?>> getSpecies() {
		return SpeciesUtil.getAllBeeSpecies();
	}

	@Override
	public String getFileSuffix() {
		return "bees";
	}

	@Override
	public IBreedingTracker getBreedingTracker(Level world, GameProfile gameProfile) {
		//TODO world cast
		return SpeciesUtil.BEE_TYPE.get().getBreedingTracker(world, gameProfile);
	}

}
