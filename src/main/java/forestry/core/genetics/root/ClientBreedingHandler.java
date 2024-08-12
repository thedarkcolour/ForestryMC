package forestry.core.genetics.root;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesType;

public class ClientBreedingHandler extends ServerBreedingHandler {
	private final Map<ISpeciesType<?, ?>, IBreedingTracker> trackerByUID = new LinkedHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IBreedingTracker> T getTracker(ISpeciesType<?, ?> type, LevelAccessor level, @Nullable GameProfile profile) {
		if (level instanceof ServerLevel) {
			return super.getTracker(type, level, profile);
		}
		T tracker = (T) trackerByUID.computeIfAbsent(type, (key) -> type.createBreedingTracker());
		type.initializeBreedingTracker(tracker, Minecraft.getInstance().level, profile);
		return tracker;
	}
}
