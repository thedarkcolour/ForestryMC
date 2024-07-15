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

import java.util.Collection;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import forestry.api.ForestryConstants;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IForestrySpeciesType;
import forestry.apiculture.ApiaristAI;
import forestry.apiculture.villagers.RegisterVillager;

import genetics.api.GeneticsAPI;
import forestry.api.genetics.ISpeciesType;

@Mod.EventBusSubscriber(modid = ForestryConstants.MOD_ID)
public class EventHandlerCore {
	@SubscribeEvent
	public static void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		syncBreedingTrackers(player);
	}

	@SubscribeEvent
	public static void handlePlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		Player player = event.getEntity();
		syncBreedingTrackers(player);
	}

	private static void syncBreedingTrackers(Player player) {
		Collection<ISpeciesType<?>> speciesRoots = GeneticsAPI.apiInstance.getRoots().values();
		for (ISpeciesType<?> speciesRoot : speciesRoots) {
			if (speciesRoot instanceof IForestrySpeciesType<?> root) {
				IBreedingTracker breedingTracker = root.getBreedingTracker(player.getCommandSenderWorld(), player.getGameProfile());
				breedingTracker.synchToPlayer(player);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Villager villager) {
			if (villager.getVillagerData().getProfession().equals(RegisterVillager.PROF_BEEKEEPER.get())) {
				villager.goalSelector.addGoal(6, new ApiaristAI(villager, 0.6));
			}
		}
	}
}
