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
package forestry.farming;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;

import forestry.api.IForestryApi;
import forestry.api.circuits.ForestryCircuitSocketTypes;
import forestry.api.circuits.ICircuitLayout;
import forestry.api.farming.IFarmRegistry;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.ForestryModuleIds;
import forestry.core.circuits.CircuitLayout;
import forestry.core.features.CoreItems;
import forestry.farming.client.FarmingClientHandler;
import forestry.farming.logic.ForestryFarmIdentifier;
import forestry.farming.logic.farmables.FarmableAgingCrop;
import forestry.farming.logic.farmables.FarmableChorus;
import forestry.farming.logic.farmables.FarmableGE;
import forestry.farming.logic.farmables.FarmableGourd;
import forestry.farming.logic.farmables.FarmableSapling;
import forestry.farming.logic.farmables.FarmableStacked;
import forestry.modules.BlankForestryModule;
import forestry.api.client.IClientModuleHandler;

@ForestryModule
public class ModuleFarming extends BlankForestryModule {
	@Override
	public ResourceLocation getId() {
		return ForestryModuleIds.FARMING;
	}

	@Override
	public void preInit() {
		IFarmRegistry registry = IForestryApi.INSTANCE.getFarmRegistry();
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.OAK_SAPLING),
				new ItemStack[]{new ItemStack(Items.APPLE), new ItemStack(Items.STICK)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.BIRCH_SAPLING),
				new ItemStack[]{new ItemStack(Items.STICK)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.SPRUCE_SAPLING),
				new ItemStack[]{new ItemStack(Items.STICK)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.JUNGLE_SAPLING),
				new ItemStack[]{new ItemStack(Items.STICK), new ItemStack(Items.COCOA_BEANS)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.DARK_OAK_SAPLING),
				new ItemStack[]{new ItemStack(Items.APPLE), new ItemStack(Items.STICK)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.ACACIA_SAPLING),
				new ItemStack[]{new ItemStack(Items.STICK)}
		));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
				new ItemStack(Blocks.MANGROVE_PROPAGULE),
				new ItemStack[]{new ItemStack(Items.STICK)}
		));
		// todo 1.20.1
		//registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableSapling(
		//		new ItemStack(Blocks.CHERRY_SAPLING),
		//		new ItemStack[]{new ItemStack(Items.STICK)}
		//));
		registry.registerFarmables(ForestryFarmIdentifier.ARBOREAL, new FarmableGE());

		registry.registerFarmables(ForestryFarmIdentifier.CROPS,
				new FarmableAgingCrop(new ItemStack(Items.WHEAT_SEEDS), Blocks.WHEAT, new ItemStack(Items.WHEAT), CropBlock.AGE, 7, 0),
				new FarmableAgingCrop(new ItemStack(Items.POTATO), Blocks.POTATOES, new ItemStack(Items.POTATO), CropBlock.AGE, 7, 0),
				new FarmableAgingCrop(new ItemStack(Items.CARROT), Blocks.CARROTS, new ItemStack(Items.CARROT), CropBlock.AGE, 7, 0),
				new FarmableAgingCrop(new ItemStack(Items.BEETROOT_SEEDS), Blocks.BEETROOTS, new ItemStack(Items.BEETROOT), BeetrootBlock.AGE, 3, 0));

		/*BlockState plantedBrownMushroom = FarmingBlocks.MUSHROOM.with(BlockMushroom.VARIANT, BlockMushroom.MushroomType.BROWN);
		registry.registerFarmables(ForestryFarmIdentifier.SHROOM, new FarmableVanillaMushroom(new ItemStack(Blocks.BROWN_MUSHROOM), plantedBrownMushroom, Blocks.BROWN_MUSHROOM_BLOCK));

		BlockState plantedRedMushroom = FarmingBlocks.MUSHROOM.with(BlockMushroom.VARIANT, BlockMushroom.MushroomType.RED);
		registry.registerFarmables(ForestryFarmIdentifier.SHROOM, new FarmableVanillaMushroom(new ItemStack(Blocks.RED_MUSHROOM), plantedRedMushroom, Blocks.RED_MUSHROOM_BLOCK));*/

		registry.registerFarmables(ForestryFarmIdentifier.GOURD, new FarmableGourd(new ItemStack(Items.PUMPKIN_SEEDS), Blocks.PUMPKIN_STEM, Blocks.PUMPKIN));
		registry.registerFarmables(ForestryFarmIdentifier.GOURD, new FarmableGourd(new ItemStack(Items.MELON_SEEDS), Blocks.MELON_STEM, Blocks.MELON));

		registry.registerFarmables(ForestryFarmIdentifier.INFERNAL, new FarmableAgingCrop(new ItemStack(Items.NETHER_WART), Blocks.NETHER_WART, NetherWartBlock.AGE, 3));

		registry.registerFarmables(ForestryFarmIdentifier.POALES, new FarmableStacked(new ItemStack(Items.SUGAR_CANE), Blocks.SUGAR_CANE, 3));

		registry.registerFarmables(ForestryFarmIdentifier.SUCCULENTES, new FarmableStacked(new ItemStack(Blocks.CACTUS), Blocks.CACTUS, 3));

		registry.registerFarmables(ForestryFarmIdentifier.ENDER, FarmableChorus.INSTANCE);

		//Forestry fertilizer
		registry.registerFertilizer(Ingredient.of(CoreItems.FERTILIZER_COMPOUND), 500);
	}

	@Override
	public void doInit() {
		FarmDefinition.init();
	}

	@Override
	public void registerClientHandler(Consumer<IClientModuleHandler> registrar) {
		registrar.accept(new FarmingClientHandler());
	}
}
