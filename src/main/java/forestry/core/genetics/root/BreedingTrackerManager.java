package forestry.core.genetics.root;

import javax.annotation.Nullable;

import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IBreedingTrackerManager;
import forestry.api.genetics.ISpeciesType;
import forestry.core.ClientsideCode;

public enum BreedingTrackerManager implements IBreedingTrackerManager {
	INSTANCE;

	private static final SidedHandler BREEDING_HANDLER = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newBreedingHandler() : new ServerBreedingHandler();

	@Override
	public <T extends IBreedingTracker> T getTracker(ISpeciesType<?, ?> type, LevelAccessor level, @Nullable GameProfile profile) {
		return BREEDING_HANDLER.getTracker(type, level, profile);
	}

	interface SidedHandler {
		<T extends IBreedingTracker> T getTracker(ISpeciesType<?, ?> type, LevelAccessor level, @Nullable GameProfile profile);
	}
}
