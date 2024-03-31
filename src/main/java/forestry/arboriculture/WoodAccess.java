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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodAccess;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.modules.features.FeatureBlockGroup;

public class WoodAccess implements IWoodAccess {
	@Nullable
	private static WoodAccess INSTANCE;

	public static WoodAccess getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WoodAccess();
		}
		return INSTANCE;
	}

	private final Map<WoodBlockKind, WoodMap> woodMaps = new EnumMap<>(WoodBlockKind.class);
	private final List<IWoodType> registeredWoodTypes = new ArrayList<>();

	private WoodAccess() {
		for (WoodBlockKind woodBlockKind : WoodBlockKind.values()) {
			woodMaps.put(woodBlockKind, new WoodMap(woodBlockKind));
		}
		registerVanilla();
	}

	public <T extends Block & IWoodTyped> void registerFeatures(FeatureBlockGroup<? extends T, ? extends IWoodType> featureGroup, WoodBlockKind kind) {
		for (T block : featureGroup.getBlocks()) {
			registerWithoutVariants(block, kind);
		}
	}

	private void registerVanilla() {
		register(EnumVanillaWoodType.OAK, WoodBlockKind.LOG, false, Blocks.OAK_LOG.defaultBlockState(), () -> Items.OAK_LOG);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.LOG, false, Blocks.SPRUCE_LOG.defaultBlockState(), () -> Items.SPRUCE_LOG);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.LOG, false, Blocks.BIRCH_LOG.defaultBlockState(), () -> Items.BIRCH_LOG);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.LOG, false, Blocks.JUNGLE_LOG.defaultBlockState(), () -> Items.JUNGLE_LOG);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.LOG, false, Blocks.ACACIA_LOG.defaultBlockState(), () -> Items.ACACIA_LOG);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.LOG, false, Blocks.DARK_OAK_LOG.defaultBlockState(), () -> Items.DARK_OAK_LOG);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.PLANKS, false, Blocks.OAK_PLANKS.defaultBlockState(), () -> Items.OAK_PLANKS);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.PLANKS, false, Blocks.SPRUCE_PLANKS.defaultBlockState(), () -> Items.SPRUCE_PLANKS);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.PLANKS, false, Blocks.BIRCH_PLANKS.defaultBlockState(), () -> Items.BIRCH_PLANKS);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.PLANKS, false, Blocks.JUNGLE_PLANKS.defaultBlockState(), () -> Items.JUNGLE_PLANKS);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.PLANKS, false, Blocks.ACACIA_PLANKS.defaultBlockState(), () -> Items.ACACIA_PLANKS);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.PLANKS, false, Blocks.DARK_OAK_PLANKS.defaultBlockState(), () -> Items.DARK_OAK_PLANKS);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.SLAB, false, Blocks.OAK_SLAB.defaultBlockState(), () -> Items.OAK_SLAB);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.SLAB, false, Blocks.SPRUCE_SLAB.defaultBlockState(), () -> Items.SPRUCE_SLAB);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.SLAB, false, Blocks.BIRCH_SLAB.defaultBlockState(), () -> Items.BIRCH_SLAB);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.SLAB, false, Blocks.JUNGLE_SLAB.defaultBlockState(), () -> Items.JUNGLE_SLAB);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.SLAB, false, Blocks.ACACIA_SLAB.defaultBlockState(), () -> Items.ACACIA_SLAB);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.SLAB, false, Blocks.DARK_OAK_SLAB.defaultBlockState(), () -> Items.DARK_OAK_SLAB);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.LOG, false, Blocks.OAK_LOG.defaultBlockState(), () -> Items.OAK_LOG);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.LOG, false, Blocks.SPRUCE_LOG.defaultBlockState(), () -> Items.SPRUCE_LOG);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.LOG, false, Blocks.BIRCH_LOG.defaultBlockState(), () -> Items.BIRCH_LOG);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.LOG, false, Blocks.JUNGLE_LOG.defaultBlockState(), () -> Items.JUNGLE_LOG);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.LOG, false, Blocks.ACACIA_LOG.defaultBlockState(), () -> Items.ACACIA_LOG);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.LOG, false, Blocks.DARK_OAK_LOG.defaultBlockState(), () -> Items.DARK_OAK_LOG);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.FENCE, false, Blocks.OAK_FENCE.defaultBlockState(), () -> Items.OAK_FENCE);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.FENCE, false, Blocks.SPRUCE_FENCE.defaultBlockState(), () -> Items.SPRUCE_FENCE);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.FENCE, false, Blocks.BIRCH_FENCE.defaultBlockState(), () -> Items.BIRCH_FENCE);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.FENCE, false, Blocks.JUNGLE_FENCE.defaultBlockState(), () -> Items.JUNGLE_FENCE);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.FENCE, false, Blocks.ACACIA_FENCE.defaultBlockState(), () -> Items.ACACIA_FENCE);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.FENCE, false, Blocks.DARK_OAK_FENCE.defaultBlockState(), () -> Items.DARK_OAK_FENCE);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.FENCE_GATE, false, Blocks.OAK_FENCE_GATE.defaultBlockState(), () -> Items.OAK_FENCE_GATE);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.FENCE_GATE, false, Blocks.SPRUCE_FENCE_GATE.defaultBlockState(), () -> Items.SPRUCE_FENCE_GATE);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.FENCE_GATE, false, Blocks.BIRCH_FENCE_GATE.defaultBlockState(), () -> Items.BIRCH_FENCE_GATE);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.FENCE_GATE, false, Blocks.JUNGLE_FENCE_GATE.defaultBlockState(), () -> Items.JUNGLE_FENCE_GATE);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.FENCE_GATE, false, Blocks.ACACIA_FENCE_GATE.defaultBlockState(), () -> Items.ACACIA_FENCE_GATE);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.FENCE_GATE, false, Blocks.DARK_OAK_FENCE_GATE.defaultBlockState(), () -> Items.DARK_OAK_FENCE_GATE);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.STAIRS, false, Blocks.OAK_STAIRS.defaultBlockState(), () -> Items.OAK_STAIRS);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.STAIRS, false, Blocks.SPRUCE_STAIRS.defaultBlockState(), () -> Items.SPRUCE_STAIRS);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.STAIRS, false, Blocks.BIRCH_STAIRS.defaultBlockState(), () -> Items.BIRCH_STAIRS);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.STAIRS, false, Blocks.JUNGLE_STAIRS.defaultBlockState(), () -> Items.JUNGLE_STAIRS);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.STAIRS, false, Blocks.ACACIA_STAIRS.defaultBlockState(), () -> Items.ACACIA_STAIRS);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.STAIRS, false, Blocks.DARK_OAK_STAIRS.defaultBlockState(), () -> Items.DARK_OAK_STAIRS);

		register(EnumVanillaWoodType.OAK, WoodBlockKind.DOOR, false, Blocks.OAK_DOOR.defaultBlockState(), () -> Items.OAK_DOOR);
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.DOOR, false, Blocks.SPRUCE_DOOR.defaultBlockState(), () -> Items.SPRUCE_DOOR);
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.DOOR, false, Blocks.BIRCH_DOOR.defaultBlockState(), () -> Items.BIRCH_DOOR);
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.DOOR, false, Blocks.JUNGLE_DOOR.defaultBlockState(), () -> Items.JUNGLE_DOOR);
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.DOOR, false, Blocks.ACACIA_DOOR.defaultBlockState(), () -> Items.ACACIA_DOOR);
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.DOOR, false, Blocks.DARK_OAK_DOOR.defaultBlockState(), () -> Items.DARK_OAK_DOOR);
	}

	/**
	 * Register wood blocks that have no variant property
	 */
	private <T extends Block & IWoodTyped> void registerWithoutVariants(T woodTyped, WoodBlockKind woodBlockKind) {
		boolean fireproof = woodTyped.isFireproof();
		BlockState blockState = woodTyped.defaultBlockState();
		IWoodType woodType = woodTyped.getWoodType();
		Supplier<Item> itemStack = woodTyped::asItem;
		register(woodType, woodBlockKind, fireproof, blockState, itemStack);
	}

	@Override
	public void register(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof, BlockState blockState, Supplier<Item> itemStack) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		if (!registeredWoodTypes.contains(woodType)) {
			registeredWoodTypes.add(woodType);
		}
		woodMap.getItem(fireproof).put(woodType, itemStack);
		woodMap.getBlock(fireproof).put(woodType, blockState);
	}

	@Override
	public ItemStack getStack(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		Supplier<Item> itemStack = woodMap.getItem(fireproof).get(woodType);
		if (itemStack == null) {
			String errMessage = String.format("No stack found for %s %s %s", woodType, woodMap.getName(), fireproof ? "fireproof" : "non-fireproof");
			throw new IllegalStateException(errMessage);
		}
		return new ItemStack(itemStack.get());
	}

	@Override
	public BlockState getBlock(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		BlockState blockState = woodMap.getBlock(fireproof).get(woodType);
		if (blockState == null) {
			String errMessage = String.format("No block found for %s %s %s", woodType, woodMap.getName(), fireproof ? "fireproof" : "non-fireproof");
			throw new IllegalStateException(errMessage);
		}
		return blockState;
	}

	@Override
	public List<IWoodType> getRegisteredWoodTypes() {
		return registeredWoodTypes;
	}

	private static class WoodMap {
		private final Map<IWoodType, Supplier<Item>> normalItems = new HashMap<>();
		private final Map<IWoodType, Supplier<Item>> fireproofItems = new HashMap<>();
		private final Map<IWoodType, BlockState> normalBlocks = new HashMap<>();
		private final Map<IWoodType, BlockState> fireproofBlocks = new HashMap<>();
		private final WoodBlockKind woodBlockKind;

		public WoodMap(WoodBlockKind woodBlockKind) {
			this.woodBlockKind = woodBlockKind;
		}

		public String getName() {
			return woodBlockKind.name();
		}

		public Map<IWoodType, Supplier<Item>> getItem(boolean fireproof) {
			return fireproof ? this.fireproofItems : this.normalItems;
		}

		public Map<IWoodType, BlockState> getBlock(boolean fireproof) {
			return fireproof ? this.fireproofBlocks : this.normalBlocks;
		}
	}
}
