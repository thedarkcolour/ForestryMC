/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climate;

import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.core.ILocatable;

public interface IClimateListener extends ILocatable, IClimateProvider {
	/**
	 * @return Returns the climate state of this listener.
	 */
	ClimateState getClimateState();

	/* CLIENT */

	/**
	 * Updates the listener on the client side.
	 *
	 * @param spawnParticles If the listener should spawn particles around its location.
	 */
	@OnlyIn(Dist.CLIENT)
	void updateClientSide(boolean spawnParticles);

	/**
	 * Sets the cached state to the given state.
	 */
	void setClimateState(ClimateState climateState);

	/**
	 * Sends a packet if needed to all players that are currently "watching" the chunk that the listener is located in.
	 */
	void syncToClient();

	/**
	 * Sends a packet to the given players.
	 */
	void syncToClient(ServerPlayer player);

	void markLocatableDirty();
}
