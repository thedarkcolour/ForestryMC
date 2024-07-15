/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import forestry.api.core.ILocatable;

/**
 * A transformer modifies the climate in a specific area.
 * To get the climate at a specific position use {@link IWorldClimateHolder#getClimate(long)}.
 * {@link IClimateTransformer} have to be registered with {@link IWorldClimateHolder#updateTransformer(IClimateTransformer)}
 * and {@link IWorldClimateHolder#removeTransformer(IClimateTransformer)} has to be called after the block that contains
 * the transformer gets harvested / replaced.
 * <p>
 * {@link IWorldClimateHolder#updateTransformer(IClimateTransformer)} has to be called after every change of the climate,
 * the range or the circular state of the transformer.
 */
public interface IClimateTransformer extends ILocatable {
	/**
	 * Updates the transformer and adds it to the world at the first time this method gets called.
	 */
	void onAdded(ServerLevel level, BlockPos pos);

	/**
	 * Called when the containing block entity is removed from the world.
	 */
	void onRemoved(ServerLevel level);

	/**
	 * @return The parent of this transformer.
	 */
	IClimateHousing getHousing();

	/**
	 * @return The range of this transformer in one direction from the center of the transformer.
	 */
	int getRange();

	/**
	 * Sets the range of this transformer.
	 * The default range of the value is from 1 to 16.
	 */
	void setRange(int range);

	/**
	 * Sets the circular state of the transformer.
	 */
	void setCircular(boolean circular);

	/**
	 * @return True if the current area of the transformer is circular.
	 */
	boolean isCircular();

	/**
	 * @return The current area of the transformer in blocks.
	 */
	int getArea();

	/**
	 * @return A modifier that is calculated with the help of the area size and is used by
	 * {@link #getCostModifier()} and {@link #getSpeedModifier()} to multiply the cost and the speed based on the
	 * size of the area that the transformer transforms.
	 */
	float getAreaModifier();

	/**
	 * @return A modifier that gets used to scale the cost of the habitat former based on the size of the area
	 * it transforms.
	 * @see #getAreaModifier()
	 */
	float getCostModifier();

	/**
	 * @return A modifier that gets used to scale the speed of the habitat former based on the size of the area
	 * it transforms.
	 * @see #getAreaModifier()
	 */
	float getSpeedModifier();

	/**
	 * @return The targeted state of this transformer.
	 */
	@Nullable
	ClimateState getTarget();

	/**
	 * Sets the targeted state of this transformer.
	 */
	void setTarget(@Nullable ClimateState target);

	/**
	 * @return The immutable current state of the transformer.
	 */
	@Nullable
	ClimateState getCurrent();

	/**
	 * Sets the current climate state of the transformer.
	 */
	void setCurrent(ClimateState state);

	/**
	 * @return The unmodified climate of the biome where this transformer is located.
	 */
	@Nullable
	ClimateState getDefault();

	/**
	 * A helper interface that can be usd to manipulate the state of this logic.
	 *
	 * @return A helper interface that can be usd to manipulate the state of this logic.
	 */
	IClimateManipulator createManipulator(ClimateType type, boolean allowBackwards);
}
