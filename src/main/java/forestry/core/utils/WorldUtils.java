package forestry.core.utils;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;

public final class WorldUtils {

	public static Level client() {
		return Objects.requireNonNull(Minecraft.getInstance().level);
	}

	public static ServerLevelData getServerInfo(Level world) {
		LevelData info = world.getLevelData();
		if (!(info instanceof PrimaryLevelData)) {
			throw new IllegalStateException("Failed to cast the world to its server version.");
		}
		return (PrimaryLevelData) info;
	}

}
