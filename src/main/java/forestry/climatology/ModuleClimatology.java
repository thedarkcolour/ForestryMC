/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************//*

package forestry.climatology;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import forestry.api.climate.IClimateListener;
import forestry.api.climate.IClimateTransformer;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.modules.IPacketRegistry;
import forestry.climatology.network.packets.PacketSelectClimateTargeted;
import forestry.climatology.proxy.ClimatologyClimateHandler;
import forestry.core.network.PacketIdServer;
import forestry.modules.BlankForestryModule;
import forestry.api.client.IClientModuleHandler;

public class ModuleClimatology extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.CLIMATOLOGY;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		modBus.addListener(ModuleClimatology::registerCapabilities);
		MinecraftForge.EVENT_BUS.addListener(ClimateHandlerServer::onPlayerTick);
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		//registrar.accept(new ClimatologyClimateHandler());
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IClimateListener.class);
		event.register(IClimateTransformer.class);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		//registry.serverbound(PacketIdServer.SELECT_CLIMATE_TARGETED, PacketSelectClimateTargeted.class, PacketSelectClimateTargeted::decode, PacketSelectClimateTargeted::handle);
	}
}
*/
