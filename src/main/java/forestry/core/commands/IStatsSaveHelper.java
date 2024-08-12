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
package forestry.core.commands;

import java.util.Collection;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpecies;

public interface IStatsSaveHelper {
	String getTranslationKey();

	void addExtraInfo(Collection<Component> statistics, IBreedingTracker breedingTracker);

	Collection<? extends ISpecies<?>> getSpecies();

	String getFileSuffix();

	IBreedingTracker getBreedingTracker(Level world, GameProfile gameProfile);
}
