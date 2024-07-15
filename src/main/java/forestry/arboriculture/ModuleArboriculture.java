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
package forestry.arboriculture;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.arboriculture.TreeManager;
import forestry.api.core.IArmorNaturalist;
import forestry.arboriculture.commands.CommandTree;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.arboriculture.genetics.TreeFactory;
import forestry.arboriculture.genetics.TreeMutationFactory;
import forestry.arboriculture.network.PacketRipeningUpdate;
import forestry.arboriculture.client.ProxyArboriculture;
import forestry.arboriculture.villagers.RegisterVillager;
import forestry.core.ClientsideCode;
import forestry.core.ModuleCore;
import forestry.api.modules.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.modules.BlankForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.api.client.IClientModuleHandler;

public class ModuleArboriculture extends BlankForestryModule {

	public static final ProxyArboriculture PROXY = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newProxyArboriculture() : new ProxyArboriculture();
	public static String treekeepingMode = "NORMAL";

	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.ARBORICULTURE;
	}

	@Override
	public void registerEvents(IEventBus modBus) {
		RegisterVillager.POINTS_OF_INTEREST.register(modBus);
		RegisterVillager.PROFESSIONS.register(modBus);
		MinecraftForge.EVENT_BUS.addListener(RegisterVillager::villagerTrades);
	}

	@Override
	public void setupApi() {
		TreeManager.treeFactory = new TreeFactory();
		TreeManager.treeMutationFactory = new TreeMutationFactory();

		TreeManager.woodAccess = WoodAccess.getInstance();
	}

	@Override
	public void setupFallbackApi() {
		TreeManager.woodAccess = WoodAccess.getInstance();
	}

	@Override
	public void preInit() {
		// Init rendering
		PROXY.initializeModels();

		// Commands
		ModuleCore.rootCommand.then(CommandTree.register());

		ArboricultureFilterRuleType.init();
	}

	@Override
	public void registerCapabilities(Consumer<Class<?>> consumer) {
		consumer.accept(IArmorNaturalist.class);
	}

	@Override
	public void doInit() {
		TreeDefinition.initTrees();
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.clientbound(PacketIdClient.RIPENING_UPDATE, PacketRipeningUpdate.class, PacketRipeningUpdate::decode, PacketRipeningUpdate::handle);
	}

	@Override
	public @Nullable Supplier<IClientModuleHandler> getClientHandler() {
		return PROXY;
	}
}
