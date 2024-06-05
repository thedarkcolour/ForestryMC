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

import java.util.function.Consumer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.arboriculture.TreeManager;
import forestry.api.core.IArmorNaturalist;
import forestry.api.modules.ForestryModule;
import forestry.arboriculture.commands.CommandTree;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.arboriculture.genetics.TreeFactory;
import forestry.arboriculture.genetics.TreeMutationFactory;
import forestry.arboriculture.network.PacketRipeningUpdate;
import forestry.arboriculture.proxy.ProxyArboriculture;
import forestry.arboriculture.villagers.RegisterVillager;
import forestry.core.ClientsideCode;
import forestry.core.ModuleCore;
import forestry.core.config.Constants;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ISidedModuleHandler;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.ARBORICULTURE, name = "Arboriculture", author = "Binnie & SirSengir", url = Constants.URL, unlocalizedDescription = "for.module.arboriculture.description", lootTable = "arboriculture")
public class ModuleArboriculture extends BlankForestryModule {

	public static final ProxyArboriculture PROXY = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newProxyArboriculture() : new ProxyArboriculture();
	public static String treekeepingMode = "NORMAL";

	public ModuleArboriculture() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		RegisterVillager.POINTS_OF_INTEREST.register(modEventBus);
		RegisterVillager.PROFESSIONS.register(modEventBus);
		MinecraftForge.EVENT_BUS.addListener(RegisterVillager::villagerTrades);
	}

	@Override
	public void setupAPI() {
		TreeManager.treeFactory = new TreeFactory();
		TreeManager.treeMutationFactory = new TreeMutationFactory();

		TreeManager.woodAccess = WoodAccess.getInstance();
	}

	@Override
	public void disabledSetupAPI() {
		TreeManager.woodAccess = WoodAccess.getInstance();
	}

	@Override
	public void preInit() {
		//TODO: World Gen
		if (TreeConfig.getSpawnRarity() > 0.0F) {
			//MinecraftForge.TERRAIN_GEN_BUS.register(new TreeDecorator());
		}

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
	public boolean processIMCMessage(InterModComms.IMCMessage message) {
		//TODO: IMC
		//		if (message.getMethod().equals("add-fence-block")) {
		//			Supplier<String> blockName = message.getMessageSupplier();
		//			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(message.getMessageSupplier().get()));
		//
		//			if (block != null) {
		//				validFences.add(block);
		//			} else {
		//				IMCUtil.logInvalidIMCMessage(message);
		//			}
		//			return true;
		//		} else if (message.getMethod().equals("blacklist-trees-dimension")) {
		//			String treeUID = message.getNBTValue().getString("treeUID");
		//			int[] dims = message.getNBTValue().getIntArray("dimensions");
		//			for (int dim : dims) {
		//				TreeConfig.blacklistTreeDim(treeUID, dim);
		//			}
		//			return true;
		//		}
		//		return false;
		return false;
	}

	//@SubscribeEvent
	//public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
	//		BlockState state = event.getState();
	//		Block block = state.getBlock();
	//		if (block instanceof LeavesBlock && !(block instanceof BlockForestryLeaves)) {
	//			PlayerEntity player = event.getHarvester();
	//			if (player != null) {
	//				ItemStack harvestingTool = player.getHeldItemMainhand();
	//				if (harvestingTool.getItem() instanceof IToolGrafter) {
	//					if (event.getDrops().isEmpty()) {
	//						World world = event.getWorld();
	//						Item itemDropped = block.getItemDropped(state, world.rand, 3);
	//						if (itemDropped != Items.AIR) {
	//							event.getDrops().add(new ItemStack(itemDropped, 1, block.damageDropped(state)));
	//						}
	//					}
	//
	//					harvestingTool.damageItem(1, player, (entity) -> {
	//						entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
	//					});
	//					if (harvestingTool.isEmpty()) {
	//						net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, harvestingTool, Hand.MAIN_HAND);
	//					}
	//				}
	//			}
	//		}
	//}

	@Override
	public ISidedModuleHandler getModuleHandler() {
		return PROXY;
	}
}
