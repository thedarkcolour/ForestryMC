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
package forestry.storage;

import javax.annotation.Nullable;
import java.util.Locale;

import net.minecraft.util.StringRepresentable;

public enum BackpackMode implements StringRepresentable {
	NEUTRAL(null),
	LOCKED("for.storage.backpack.mode.locked"),
	RECEIVE("for.storage.backpack.mode.receiving"),
	RESUPPLY("for.storage.backpack.mode.resupply");

	public static final BackpackMode[] VALUES = values();

	@Nullable
	private final String translationkey;

	BackpackMode(@Nullable String translationkey) {
		this.translationkey = translationkey;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	@Nullable
	public String getTranslationkey() {
		return translationkey;
	}
}
