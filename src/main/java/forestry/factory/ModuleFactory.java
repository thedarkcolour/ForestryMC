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
package forestry.factory;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidStack;

import forestry.api.circuits.ChipsetManager;
import forestry.api.circuits.CircuitSocketType;
import forestry.api.circuits.ICircuitLayout;
import forestry.api.fuels.FermenterFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.MoistenerFuel;
import forestry.api.fuels.RainSubstrate;
import forestry.api.recipes.RecipeManagers;
import forestry.core.circuits.CircuitLayout;
import forestry.core.circuits.Circuits;
import forestry.core.config.Constants;
import forestry.core.config.Preference;
import forestry.core.features.CoreItems;
import forestry.core.fluids.ForestryFluids;
import forestry.core.items.definitions.EnumCraftingMaterial;
import forestry.core.items.definitions.EnumElectronTube;
import forestry.api.modules.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.core.utils.datastructures.DummyMap;
import forestry.core.utils.datastructures.ItemStackMap;
import forestry.factory.circuits.CircuitSpeedUpgrade;
import forestry.factory.features.FactoryMenuTypes;
import forestry.factory.gui.GuiBottler;
import forestry.factory.gui.GuiCarpenter;
import forestry.factory.gui.GuiCentrifuge;
import forestry.factory.gui.GuiFabricator;
import forestry.factory.gui.GuiFermenter;
import forestry.factory.gui.GuiMoistener;
import forestry.factory.gui.GuiRaintank;
import forestry.factory.gui.GuiSqueezer;
import forestry.factory.gui.GuiStill;
import forestry.factory.network.packets.PacketRecipeTransferRequest;
import forestry.factory.network.packets.PacketRecipeTransferUpdate;
import forestry.factory.recipes.CarpenterRecipeManager;
import forestry.factory.recipes.CentrifugeRecipeManager;
import forestry.factory.recipes.FabricatorRecipeManager;
import forestry.factory.recipes.FabricatorSmeltingRecipeManager;
import forestry.factory.recipes.FermenterRecipeManager;
import forestry.factory.recipes.MoistenerRecipeManager;
import forestry.factory.recipes.SqueezerContainerRecipeManager;
import forestry.factory.recipes.SqueezerRecipeManager;
import forestry.factory.recipes.StillRecipeManager;
import forestry.modules.BlankForestryModule;
import forestry.api.modules.ForestryModuleIds;

public class ModuleFactory extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.FACTORY;
	}

	@Override
	public void setupApi() {
		RecipeManagers.carpenterManager = machineEnabled() ? new CarpenterRecipeManager() : new DummyManagers.DummyCarpenterManager();
		RecipeManagers.centrifugeManager = machineEnabled() ? new CentrifugeRecipeManager() : new DummyManagers.DummyCentrifugeManager();
		RecipeManagers.fabricatorManager = machineEnabled() ? new FabricatorRecipeManager() : new DummyManagers.DummyFabricatorManager();
		RecipeManagers.fabricatorSmeltingManager = machineEnabled() ? new FabricatorSmeltingRecipeManager() : new DummyManagers.DummyFabricatorSmeltingManager();
		RecipeManagers.fermenterManager = machineEnabled() ? new FermenterRecipeManager() : new DummyManagers.DummyFermenterManager();
		RecipeManagers.moistenerManager = machineEnabled() ? new MoistenerRecipeManager() : new DummyManagers.DummyMoistenerManager();
		RecipeManagers.squeezerManager = machineEnabled() ? new SqueezerRecipeManager() : new DummyManagers.DummySqueezerManager();
		RecipeManagers.squeezerContainerManager = machineEnabled() ? new SqueezerContainerRecipeManager() : new DummyManagers.DummySqueezerContainerManager();
		RecipeManagers.stillManager = machineEnabled() ? new StillRecipeManager() : new DummyManagers.DummyStillManager();

		setupFuelManager();
	}

	@Override
	public void setupFallbackApi() {
		RecipeManagers.carpenterManager = new DummyManagers.DummyCarpenterManager();
		RecipeManagers.centrifugeManager = new DummyManagers.DummyCentrifugeManager();
		RecipeManagers.fabricatorManager = new DummyManagers.DummyFabricatorManager();
		RecipeManagers.fabricatorSmeltingManager = new DummyManagers.DummyFabricatorSmeltingManager();
		RecipeManagers.fermenterManager = new DummyManagers.DummyFermenterManager();
		RecipeManagers.moistenerManager = new DummyManagers.DummyMoistenerManager();
		RecipeManagers.squeezerManager = new DummyManagers.DummySqueezerManager();
		RecipeManagers.stillManager = new DummyManagers.DummyStillManager();

		setupFuelManager();
	}

	private static void setupFuelManager() {
		FuelManager.fermenterFuel = machineEnabled() ? new ItemStackMap<>() : new DummyMap<>();
		FuelManager.moistenerResource = machineEnabled() ? new ItemStackMap<>() : new DummyMap<>();
		FuelManager.rainSubstrate = machineEnabled() ? new ItemStackMap<>() : new DummyMap<>();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerMenuScreens() {
		MenuScreens.register(FactoryMenuTypes.BOTTLER.menuType(), GuiBottler::new);
		MenuScreens.register(FactoryMenuTypes.CARPENTER.menuType(), GuiCarpenter::new);
		MenuScreens.register(FactoryMenuTypes.CENTRIFUGE.menuType(), GuiCentrifuge::new);
		MenuScreens.register(FactoryMenuTypes.FABRICATOR.menuType(), GuiFabricator::new);
		MenuScreens.register(FactoryMenuTypes.FERMENTER.menuType(), GuiFermenter::new);
		MenuScreens.register(FactoryMenuTypes.MOISTENER.menuType(), GuiMoistener::new);
		MenuScreens.register(FactoryMenuTypes.RAINTANK.menuType(), GuiRaintank::new);
		MenuScreens.register(FactoryMenuTypes.SQUEEZER.menuType(), GuiSqueezer::new);
		MenuScreens.register(FactoryMenuTypes.STILL.menuType(), GuiStill::new);
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.RECIPE_TRANSFER_REQUEST, PacketRecipeTransferRequest.class, PacketRecipeTransferRequest::decode, PacketRecipeTransferRequest::handle);
		registry.clientbound(PacketIdClient.RECIPE_TRANSFER_UPDATE, PacketRecipeTransferUpdate.class, PacketRecipeTransferUpdate::decode, PacketRecipeTransferUpdate::handle);
	}

	@Override
	public void preInit() {
		// Set fuels and resources for the fermenter
		ItemStack fertilizerCompound = CoreItems.FERTILIZER_COMPOUND.stack();
		FuelManager.fermenterFuel.put(fertilizerCompound, new FermenterFuel(fertilizerCompound,
				Preference.FERMENTED_CYCLE_FERTILIZER, Preference.FERMENTATION_DURATION_FERTILIZER));

		int cyclesCompost = Preference.FERMENTATION_DURATION_COMPOST;
		int valueCompost = Preference.FERMENTED_CYCLE_COMPOST;
		ItemStack fertilizerBio = CoreItems.COMPOST.stack();
		ItemStack mulch = CoreItems.MULCH.stack();
		FuelManager.fermenterFuel.put(fertilizerBio, new FermenterFuel(fertilizerBio, valueCompost, cyclesCompost));
		FuelManager.fermenterFuel.put(mulch, new FermenterFuel(mulch, valueCompost, cyclesCompost));

		// Add moistener resources
		ItemStack wheat = new ItemStack(Items.WHEAT);
		ItemStack mouldyWheat = CoreItems.MOULDY_WHEAT.stack();
		ItemStack decayingWheat = CoreItems.DECAYING_WHEAT.stack();
		FuelManager.moistenerResource.put(wheat, new MoistenerFuel(wheat, mouldyWheat, 0, 300));
		FuelManager.moistenerResource.put(mouldyWheat, new MoistenerFuel(mouldyWheat, decayingWheat, 1, 600));
		FuelManager.moistenerResource.put(decayingWheat, new MoistenerFuel(decayingWheat, mulch, 2, 900));

		// Set rain substrates
		ItemStack iodineCharge = CoreItems.IODINE_CHARGE.stack();
		ItemStack dissipationCharge = CoreItems.CRAFTING_MATERIALS.stack(EnumCraftingMaterial.DISSIPATION_CHARGE, 1);
		FuelManager.rainSubstrate.put(iodineCharge, new RainSubstrate(iodineCharge, Constants.RAINMAKER_RAIN_DURATION_IODINE, 0.01f));
		FuelManager.rainSubstrate.put(dissipationCharge, new RainSubstrate(dissipationCharge, 0.075f));

		ICircuitLayout layoutMachineUpgrade = new CircuitLayout("machine.upgrade", CircuitSocketType.MACHINE);
		ChipsetManager.circuitRegistry.registerLayout(layoutMachineUpgrade);

	}

	@Override
	public void registerObjects() {
		Circuits.machineSpeedUpgrade1 = new CircuitSpeedUpgrade("machine.speed.boost.1", 0.125f, 0.05f);
		Circuits.machineSpeedUpgrade2 = new CircuitSpeedUpgrade("machine.speed.boost.2", 0.250f, 0.10f);
		Circuits.machineEfficiencyUpgrade1 = new CircuitSpeedUpgrade("machine.efficiency.1", 0, -0.10f);

		ICircuitLayout layout = ChipsetManager.circuitRegistry.getLayout("forestry.machine.upgrade");

		// / Solder Manager
		if (layout != null) {
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.EMERALD, 1), Circuits.machineSpeedUpgrade1);
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.BLAZE, 1), Circuits.machineSpeedUpgrade2);
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.GOLD, 1), Circuits.machineEfficiencyUpgrade1);
		}
	}

	@Override
	public void registerRecipes() {
		// / FABRICATOR
		String[] dyes = {"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime",
				"dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};

		FluidStack liquidGlassBucket = ForestryFluids.GLASS.getFluid(FluidType.BUCKET_VOLUME);
		FluidStack liquidGlassX4 = ForestryFluids.GLASS.getFluid(FluidType.BUCKET_VOLUME * 4);

		if (!liquidGlassBucket.isEmpty() && !liquidGlassX4.isEmpty()) {
			for (int i = 0; i < 16; i++) {
				//TODO - needs tag loop or tag match in recipe
				//					RecipeManagers.fabricatorManager.addRecipe(beeItems.waxCast.getWildcard(), liquidGlassBucket, new ItemStack(Blocks.STAINED_GLASS, 4, 15 - i), new Object[]{
				//						"#", "X",
				//						'#', dyes[i],
				//						'X', beeItems.propolis.getWildcard()});
			}
			//				RecipeManagers.fabricatorManager.addRecipe(beeItems.waxCast.getWildcard(), liquidGlassX4, new ItemStack(Blocks.GLASS), new Object[]{
			//					"X",
			//					'X', beeItems.propolis.getWildcard()});	//TODO needs tag
		}

		ICircuitLayout layout = ChipsetManager.circuitRegistry.getLayout("forestry.machine.upgrade");

		// / Solder Manager
		if (layout != null) {
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.EMERALD, 1), Circuits.machineSpeedUpgrade1);
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.BLAZE, 1), Circuits.machineSpeedUpgrade2);
			ChipsetManager.solderManager.addRecipe(layout, CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.GOLD, 1), Circuits.machineEfficiencyUpgrade1);
		}
	}

	public static boolean machineEnabled() {
		return true;
	}
}
