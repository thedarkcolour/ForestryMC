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
package forestry.climatology.items;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimatised;
import forestry.api.climate.IClimateTransformer;
import forestry.core.items.ItemForestry;
import forestry.core.items.definitions.IColoredItem;
import forestry.core.tiles.TileUtil;
import forestry.core.utils.StringUtil;

public class ItemHabitatScreen extends ItemForestry implements IColoredItem {

	public static final String POSITION_KEY = "greenhouse";
	public static final String DIMENSION_KEY = "dimension";
	public static final String PREVIEW_KEY = "preview";

	public static boolean isPreviewModeActive(ItemStack itemStack) {
		CompoundTag compoundNBT = itemStack.getTag();
		if (compoundNBT == null || !compoundNBT.contains(PREVIEW_KEY)) {
			return false;
		}
		return compoundNBT.getBoolean(PREVIEW_KEY);
	}

	public static void setPreviewMode(ItemStack itemStack, boolean preview) {
		itemStack.addTagElement(PREVIEW_KEY, ByteTag.valueOf((byte) (preview ? 1 : 0)));
	}

	@Nullable
	public static BlockPos getPosition(ItemStack itemStack) {
		CompoundTag CompoundNBT = itemStack.getTag();
		if (CompoundNBT == null || !CompoundNBT.contains(POSITION_KEY)) {
			return null;
		}
		CompoundTag compound = CompoundNBT.getCompound(POSITION_KEY);
		if (compound.isEmpty()) {
			return null;
		}
		return NbtUtils.readBlockPos(compound);
	}

	@Nullable
	public static ResourceKey<Level> getDimension(ItemStack stack) {
		CompoundTag nbt = stack.getTag();

		if (nbt != null && nbt.contains(DIMENSION_KEY)) {
			ResourceLocation id = ResourceLocation.tryParse(nbt.getString(DIMENSION_KEY));

			if (id != null) {
				return ResourceKey.create(Registry.DIMENSION_REGISTRY, id);
			}
		}
		return null;
	}

	public static boolean isValid(ItemStack stack, @Nullable Level level) {
		BlockPos pos = getPosition(stack);
		ResourceKey<Level> dimension = getDimension(stack);
		if (pos == null || level == null || dimension == null || dimension != level.dimension() || !level.hasChunkAt(pos)) {
			return false;
		} else {
			return TileUtil.getTile(level, pos, IClimateHousing.class) != null;
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (!player.isShiftKeyDown()) {
			boolean previewModeActive = isPreviewModeActive(itemStack);
			setPreviewMode(itemStack, !previewModeActive);

			if (!world.isClientSide) {
				String text = !previewModeActive ? "for.habitat_screen.mode.active" : "for.habitat_screen.mode.inactive";
				player.displayClientMessage(Component.translatable(text), true);
			}
		}

		return InteractionResultHolder.success(itemStack);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (player.isShiftKeyDown()) {
			IClimateHousing housing = TileUtil.getTile(level, pos, IClimateHousing.class);
			if (housing != null) {
				ItemStack heldItem = player.getItemInHand(context.getHand());
				CompoundTag nbt = heldItem.getOrCreateTag();
				nbt.put(POSITION_KEY, NbtUtils.writeBlockPos(pos));
				nbt.putString(DIMENSION_KEY, level.dimension().location().toString());
			}
		}
		if (!level.isClientSide) {
			IClimatised state;
			ClimateState climateState = IForestryApi.INSTANCE.getClimateManager().getState((ServerLevel) level, pos);
			if (climateState.isPresent()) {
				state = climateState;
				if (!state.isPresent()) {
					state = IForestryApi.INSTANCE.getClimateManager().getBiomeState(level, pos);
				}
			} else {
				state = IForestryApi.INSTANCE.getClimateManager().getBiomeState(level, pos);
			}
			// todo this shit broken
			if (state.isPresent()) {
				player.displayClientMessage(Component.translatable("for.habitat_screen.status.state", ChatFormatting.GOLD.toString() + StringUtil.floatAsPercent(state.getTemperature()), ChatFormatting.BLUE.toString() + StringUtil.floatAsPercent(state.getHumidity())), true);
			} else {
				player.displayClientMessage(Component.translatable("for.habitat_screen.status.nostate"), true);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		if (world == null) {
			return;
		}
		boolean previewModeActive = isPreviewModeActive(stack);
		tooltip.add(Component.translatable(previewModeActive ? "for.habitat_screen.mode.active" : "for.habitat_screen.mode.inactive"));
		boolean isValid = isValid(stack, world);
		BlockPos pos = getPosition(stack);
		if (pos != null) {
			int id = 0; //TODO: Fix dimension id
			Component state = isValid ? Component.translatable("for.habitat_screen.state.linked", pos.getX(), pos.getY(), pos.getZ(), id) : Component.translatable("for.habitat_screen.state.fail");
			tooltip.add(state);
		}
		if (!isValid || pos == null) {
			return;
		}
		IClimateHousing housing = TileUtil.getTile(world, pos, IClimateHousing.class);
		if (housing == null) {
			return;
		}
		IClimatised climateState = housing.getTransformer().getCurrent();
		tooltip.add(Component.translatable("for.habitat_screen.temperature", StringUtil.floatAsPercent(climateState.getTemperature())).withStyle(ChatFormatting.GOLD));
		tooltip.add(Component.translatable("for.habitat_screen.humidity", StringUtil.floatAsPercent(climateState.getHumidity())).withStyle(ChatFormatting.BLUE));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		if (tintIndex == 2) {
			return isValid(stack, Minecraft.getInstance().level) ? 0x14B276 : 0xBA1F17;
		} else if (tintIndex == 1) {
			Level world = Minecraft.getInstance().level;
			if (!isValid(stack, world)) {
				return 0xFFFFFF;
			}
			BlockPos pos = getPosition(stack);
			if (pos == null) {
				return 0xFFFFFF;
			}
			IClimateHousing housing = TileUtil.getTile(world, pos, IClimateHousing.class);
			if (housing == null) {
				return 0xFFFFFF;
			}
			IClimateTransformer transformer = housing.getTransformer();
			IClimatised state = transformer.getCurrent();
			return state.getTemperatureEnum().color;
		}
		return 0xFFFFFF;
	}
}
