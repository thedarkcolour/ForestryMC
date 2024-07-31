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
package forestry.core.items;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import forestry.api.core.IToolPipette;
import forestry.api.core.ItemGroups;
import forestry.core.fluids.PipetteContents;

public class ItemPipette extends ItemForestry implements IToolPipette {
	public ItemPipette() {
		super(new Properties().stacksTo(1).tab(ItemGroups.tabForestry));
	}

	@Override
	public boolean canPipette(ItemStack itemstack) {
		PipetteContents contained = PipetteContents.create(itemstack);
		return contained == null || !contained.isFull();
	}

	@Override
	public void appendHoverText(ItemStack itemstack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);

		if (ForgeCapabilities.FLUID_HANDLER_ITEM == null) {
			return;
		}

		PipetteContents contained = PipetteContents.create(itemstack);
		if (contained != null) {
			contained.addTooltip(list);
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidHandlerItemStack(stack, FluidType.BUCKET_VOLUME);
	}
}
