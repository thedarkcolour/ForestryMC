/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.mail;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

import com.mojang.authlib.GameProfile;

public interface ITradeStationInfo {
	IMailAddress getAddress();

	GameProfile getOwner();

	ItemStack getTradegood();

	List<ItemStack> getRequired();

	EnumTradeStationState getState();
}
