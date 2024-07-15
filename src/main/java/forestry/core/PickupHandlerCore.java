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
package forestry.core;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IForestrySpeciesType;

import genetics.api.individual.IIndividual;
import genetics.utils.RootUtils;

public class PickupHandlerCore {
	public static void onItemPickup(Player player, ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();

		if (!stack.isEmpty()) {
			IForestrySpeciesType<IIndividual> root = RootUtils.getRoot(stack);
			if (root != null) {
				IIndividual individual = root.create(stack);

				if (individual != null) {
					IBreedingTracker tracker = root.getBreedingTracker(itemEntity.level, player.getGameProfile());
					tracker.registerPickup(individual);
				}
			}
		}
	}

}
