/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.storage;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum EnumBackpackType implements StringRepresentable {
	NORMAL, WOVEN, NATURALIST;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}
}
