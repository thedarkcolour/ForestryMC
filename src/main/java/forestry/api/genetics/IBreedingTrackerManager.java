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

	<T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile profile);
}
