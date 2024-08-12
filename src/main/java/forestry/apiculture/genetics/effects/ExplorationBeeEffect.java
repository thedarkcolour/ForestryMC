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
package forestry.apiculture.genetics.effects;

import java.util.List;

import net.minecraft.world.entity.player.Player;

import forestry.api.genetics.IGenome;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;

public class ExplorationBeeEffect extends ThrottledBeeEffect {

	public ExplorationBeeEffect() {
		super(false, 80, true, false);
	}

	@Override
	public IEffectData doEffectThrottled(IGenome genome, IEffectData storedData, IBeeHousing housing) {
		List<Player> players = ThrottledBeeEffect.getEntitiesInRange(genome, housing, Player.class);
		for (Player player : players) {
			player.giveExperiencePoints(2);
		}

		return storedData;
	}

}
