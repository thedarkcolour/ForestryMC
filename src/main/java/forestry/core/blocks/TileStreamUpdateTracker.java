package forestry.core.blocks;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;

import net.minecraftforge.network.PacketDistributor;

import forestry.core.network.NetworkHandler;
import forestry.core.network.packets.PacketTileStream;
import forestry.core.tiles.TileForestry;

// Based on https://github.com/thedarkcolour/ExDeorum/blob/1.20.4/src/main/java/thedarkcolour/exdeorum/network/VisualUpdateTracker.java
public class TileStreamUpdateTracker {
	private static final Map<LevelChunk, Set<BlockPos>> UPDATES = new WeakHashMap<>();

	public static void sendVisualUpdate(TileForestry tile) {
		var level = tile.getLevel();

		if (level != null && !level.isClientSide) {
			var dimension = level.getChunkAt(tile.getBlockPos());
			Set<BlockPos> updatesList;
			if (!UPDATES.containsKey(dimension)) {
				UPDATES.put(dimension, updatesList = new HashSet<>());
			} else {
				updatesList = UPDATES.get(dimension);
			}
			updatesList.add(tile.getBlockPos());
		}
	}

	public static void syncVisualUpdates() {
		for (var entry : UPDATES.entrySet()) {
			var pendingUpdates = entry.getValue();

			for (var updatePos : pendingUpdates) {
				var chunk = entry.getKey();

				if (chunk.getBlockEntity(updatePos) instanceof TileForestry blockEntity) {
					// packet uses strong reference
					NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new PacketTileStream(blockEntity));
				}
			}

			pendingUpdates.clear();
		}
	}
}
