/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.mail;

import javax.annotation.Nullable;
import java.util.Locale;

// todo why are these being converted to lowercase?
public enum EnumAddressee {
	PLAYER, TRADER;

	private final String lowercase = this.name().toLowerCase(Locale.ENGLISH);

	@Nullable
	public static EnumAddressee fromString(String ident) {
		ident = ident.toLowerCase(Locale.ENGLISH);
		for (EnumAddressee type : values()) {
			if (type.toString().equals(ident)) {
				return type;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return lowercase;
	}
}
