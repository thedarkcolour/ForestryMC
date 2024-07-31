package forestry.core.genetics.root;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesType;

public class ServerBreedingHandler implements BreedingTrackerManager.SidedHandler {
	@Override
	@SuppressWarnings("unchecked")
	public <T extends IBreedingTracker> T getTracker(ISpeciesType<?, ?> type, LevelAccessor level, @Nullable GameProfile profile) {
		String filename = type.getBreedingTrackerFile(profile);
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
		T tracker = (T) overworld.getDataStorage().computeIfAbsent(tag -> (SavedData) type.createBreedingTracker(tag), () -> (SavedData) type.createBreedingTracker(), filename);
		type.initializeBreedingTracker(tracker, overworld, profile);
		return tracker;
	}
}
