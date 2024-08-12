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
package forestry.core.errors;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;

import forestry.api.core.IError;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorManager;

import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class ErrorManager implements IErrorManager {
	// Numeric IDs should be used only for network synchronization
	private final Short2ObjectOpenHashMap<IError> byNumericId;
	private final Object2ShortOpenHashMap<IError> numericIdLookup;
	private final ImmutableMap<ResourceLocation, IError> byId;

	public ErrorManager(Short2ObjectOpenHashMap<IError> byNumericId, Object2ShortOpenHashMap<IError> numericIdLookup, ImmutableMap<ResourceLocation, IError> byId) {
		this.byNumericId = byNumericId;
		this.numericIdLookup = numericIdLookup;
		this.byId = byId;
	}

	@Override
	public IError getError(short id) {
		return this.byNumericId.get(id);
	}

	@Nullable
	@Override
	public IError getError(ResourceLocation errorId) {
		return this.byId.get(errorId);
	}

	@Override
	public List<IError> getErrors() {
		return this.byId.values().asList();
	}

	@Override
	public IErrorLogic createErrorLogic() {
		return new ErrorLogic();
	}

	@Override
	public short getNumericId(IError error) {
		return this.numericIdLookup.getShort(error);
	}
}
