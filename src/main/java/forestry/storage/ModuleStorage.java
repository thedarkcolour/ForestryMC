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

package forestry.storage;

import java.awt.Color;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.ForestryTags;
import forestry.api.client.IClientModuleHandler;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.storage.IBackpackInterface;
import forestry.core.config.Config;
import forestry.modules.BlankForestryModule;
import forestry.storage.client.StorageClientHandler;

@ForestryModule
public class ModuleStorage extends BlankForestryModule {
	public static final IBackpackInterface BACKPACK_INTERFACE = new BackpackInterface();

	// todo should there be an arborist backpack?
	public static final BackpackDefinition APIARIST = new BackpackDefinition(new Color(0xc4923d), Color.WHITE, BACKPACK_INTERFACE.createNaturalistBackpackFilter(ForestrySpeciesTypes.BEE));
	public static final BackpackDefinition LEPIDOPTERIST = new BackpackDefinition(new Color(0x995b31), Color.WHITE, BACKPACK_INTERFACE.createNaturalistBackpackFilter(ForestrySpeciesTypes.BUTTERFLY));
	public static final BackpackDefinition MINER = new BackpackDefinition(new Color(0x36187d), Color.WHITE, new BackpackFilter(ForestryTags.Items.MINER_ALLOW, ForestryTags.Items.MINER_REJECT));
	public static final BackpackDefinition DIGGER = new BackpackDefinition(new Color(0x363cc5), Color.WHITE, new BackpackFilter(ForestryTags.Items.DIGGER_ALLOW, ForestryTags.Items.DIGGER_REJECT));
	public static final BackpackDefinition FORESTER = new BackpackDefinition(new Color(0x347427), Color.WHITE, new BackpackFilter(ForestryTags.Items.FORESTER_ALLOW, ForestryTags.Items.FORESTER_REJECT));
	public static final BackpackDefinition HUNTER = new BackpackDefinition(new Color(0x412215), Color.WHITE, new BackpackFilter(ForestryTags.Items.HUNTER_ALLOW, ForestryTags.Items.HUNTER_REJECT));
	public static final BackpackDefinition ADVENTURER = new BackpackDefinition(new Color(0x7fb8c2), Color.WHITE, new BackpackFilter(ForestryTags.Items.ADVENTURER_ALLOW, ForestryTags.Items.ADVENTURER_REJECT));
	public static final BackpackDefinition BUILDER = new BackpackDefinition(new Color(0xdd3a3a), Color.WHITE, new BackpackFilter(ForestryTags.Items.BUILDER_ALLOW, ForestryTags.Items.BUILDER_REJECT));

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.STORAGE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		MinecraftForge.EVENT_BUS.addListener(ModuleStorage::onItemPickup);
		MinecraftForge.EVENT_BUS.addListener(ModuleStorage::onLevelTick);
	}

	private static void onLevelTick(TickEvent.LevelTickEvent event) {
		if (Config.enableBackpackResupply) {
			if (event.phase == TickEvent.Phase.END) {
				for (Player player : event.level.players()) {
					BackpackResupplyHandler.resupply(player);
				}
			}
		}
	}

	private static void onItemPickup(EntityItemPickupEvent event) {
		if (event.isCanceled() || event.getResult() == Event.Result.ALLOW) {
			return;
		}

		if (PickupHandlerStorage.onItemPickup(event.getEntity(), event.getItem())) {
			event.setResult(Event.Result.ALLOW);
		}
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new StorageClientHandler());
	}
}
