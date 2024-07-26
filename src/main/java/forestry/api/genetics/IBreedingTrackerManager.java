package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

public interface IBreedingTrackerManager {
	/**
	 * Registers a species type for tracking.
	 *
	 * @param id      The ID of the species type.
	 * @param handler
	 */
	void registerTracker(ResourceLocation id, IBreedingTrackerHandler handler);

	/**
	 *
	 * @param id      The ID of the species type.
	 * @param level   The level where this breeding tracker is saved.
	 * @param profile The profile of the player whose breeding tracker should be queried.
	 * @return The player-specific species tracker for the species type with the given ID.
	 */
	<T extends IBreedingTracker> T getTracker(ResourceLocation id, LevelAccessor level, @Nullable GameProfile profile);
}
