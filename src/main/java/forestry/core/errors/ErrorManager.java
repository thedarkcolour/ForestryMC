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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.resources.ResourceLocation;

import forestry.api.core.IError;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorManager;

import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class ErrorManager implements IErrorManager {
	// Numeric IDs should be used only for network synchronization
	private final Short2ObjectOpenHashMap<IError> byNumericId = new Short2ObjectOpenHashMap<>();
	private final Object2ShortOpenHashMap<IError> numericIdLookup = new Object2ShortOpenHashMap<>();
	private final HashMap<ResourceLocation, IError> byId = new HashMap<>();

	@Override
	public void registerError(IError state) {
		if (this.byId.containsKey(state.getId())) {
			throw new RuntimeException("Forestry Error State does not possess a unique id.");
		}

		this.byId.put(state.getId(), state);
	}

	@Override
	public IError getError(short id) {
		return this.byNumericId.get(id);
	}

	@Override
	public IError getError(ResourceLocation name) {
		return this.byId.get(name);
	}

	@Override
	public Collection<IError> getRegisteredErrors() {
		return Collections.unmodifiableCollection(this.byNumericId.values());
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
