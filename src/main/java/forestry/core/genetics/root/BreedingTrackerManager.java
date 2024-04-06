package forestry.core.genetics.root;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.level.LevelAccessor;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IBreedingTrackerHandler;
import forestry.api.genetics.IBreedingTrackerManager;
import forestry.core.ClientsideCode;

public enum BreedingTrackerManager implements IBreedingTrackerManager {
	INSTANCE;

	static final Map<String, IBreedingTrackerHandler> factories = new LinkedHashMap<>();

	private static final SidedHandler BREEDING_HANDLER = FMLEnvironment.dist == Dist.CLIENT ? ClientsideCode.newBreedingHandler() : new ServerBreedingHandler();

	@Override
	public void registerTracker(String rootUID, IBreedingTrackerHandler handler) {
		factories.put(rootUID, handler);
	}

	@Override
	public <T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile profile) {
		return BREEDING_HANDLER.getTracker(rootUID, world, profile);
	}

	interface SidedHandler {
		<T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile player);
	}
}
