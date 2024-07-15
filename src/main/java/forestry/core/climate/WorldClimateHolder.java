package forestry.core.climate;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateTransformer;
import forestry.api.core.INbtWritable;

import it.unimi.dsi.fastutil.longs.Long2LongArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

// todo give a reference to this class to each ClimateTransformer instead of repeatedly getting it
public class WorldClimateHolder extends SavedData {
	private static final TransformerData DEFAULT_DATA = new TransformerData(0L, null, 0, false, new long[0]);

	static final String NAME = "forestry_climate";

	private static final String TRANSFORMERS_KEY = "Transformers";
	private static final String CHUNK_KEY = "Chunk";
	private static final String TRANSFORMERS_DATA_KEY = "Data";
	private static final String STATE_DATA_KEY = "Data";
	private static final String POS_KEY = "Pos";
	private static final String RANGE_KEY = "Range";
	private static final String CIRCULAR_KEY = "circular";
	private static final String CHUNKS_KEY = "Chunks";

	private final Long2ObjectOpenHashMap<TransformerData> transformers = new Long2ObjectOpenHashMap<>();
	private final Long2ObjectOpenHashMap<long[]> transformersByChunk = new Long2ObjectOpenHashMap<>();
	// todo shouldn't this be a Long2Object map?
	private final Long2LongArrayMap chunkUpdates = new Long2LongArrayMap();

	@Nullable
	private Level level;

	public WorldClimateHolder() {
	}

	public WorldClimateHolder(CompoundTag tag) {
		ListTag transformerData = tag.getList(TRANSFORMERS_KEY, Tag.TAG_COMPOUND);
		for (int i = 0; i < transformerData.size(); i++) {
			CompoundTag tagCompound = transformerData.getCompound(i);
			TransformerData data = new TransformerData(tagCompound);
			transformers.put(data.position, data);
		}
		ListTag chunkData = tag.getList(CHUNK_KEY, Tag.TAG_COMPOUND);
		for (int i = 0; i < chunkData.size(); i++) {
			CompoundTag tagCompound = chunkData.getCompound(i);
			long pos = tagCompound.getLong(POS_KEY);
			long[] chunkTransformers = tagCompound.getLongArray(TRANSFORMERS_DATA_KEY);
			transformersByChunk.put(pos, chunkTransformers);
		}
	}

	public static WorldClimateHolder get(ServerLevel level) {
		DimensionDataStorage storage = level.getDataStorage();
		WorldClimateHolder holder = storage.computeIfAbsent(WorldClimateHolder::new, WorldClimateHolder::new, WorldClimateHolder.NAME);
		holder.setLevel(level);
		return holder;
	}

	public void setLevel(@Nullable Level level) {
		this.level = level;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		ListTag transformerData = new ListTag();
		for (Map.Entry<Long, TransformerData> entry : transformers.long2ObjectEntrySet()) {
			TransformerData data = entry.getValue();
			transformerData.add(data.write(new CompoundTag()));
		}
		compound.put(TRANSFORMERS_KEY, transformerData);
		ListTag chunkData = new ListTag();
		for (Map.Entry<Long, long[]> entry : transformersByChunk.long2ObjectEntrySet()) {
			CompoundTag tagCompound = new CompoundTag();
			tagCompound.putLong(POS_KEY, entry.getKey());
			tagCompound.put(TRANSFORMERS_DATA_KEY, new LongArrayTag(entry.getValue()));
			chunkData.add(tagCompound);
		}
		compound.put(CHUNK_KEY, chunkData);
		return compound;
	}

	public ClimateState getClimate(long position) {
		return transformers.getOrDefault(position, DEFAULT_DATA).climateState;
	}

	public void addTransformer(long chunkPos, long transformerPos) {
		long[] oldData = transformersByChunk.get(chunkPos);
		long[] newData;
		if (oldData != null) {
			for (long pos : oldData) {
				if (pos == transformerPos) {
					return;
				}
			}
			newData = Arrays.copyOf(oldData, oldData.length + 1);
		} else {
			newData = new long[1];
		}
		newData[newData.length - 1] = transformerPos;
		transformersByChunk.put(chunkPos, newData);
		setDirty(true);
		markChunkUpdate(chunkPos);
	}

	public void removeTransformer(long chunkPos, long transformerPos) {
		long[] oldData = transformersByChunk.get(chunkPos);
		if (oldData == null) {
			return;
		}
		for (long pos : oldData) {
			if (pos == transformerPos) {
				if (oldData.length == 1) {
					transformersByChunk.remove(chunkPos);
					chunkUpdates.remove(chunkPos);
				} else {
					long[] newData = Arrays.copyOf(oldData, oldData.length - 1);
					transformersByChunk.put(chunkPos, newData);
				}
				setDirty(true);
				markChunkUpdate(chunkPos);
				return;
			}
		}
	}

	private void markChunkUpdate(long chunkPos) {
		if (level != null) {
			chunkUpdates.put(chunkPos, level.getGameTime());
		}
	}

	public void updateTransformer(IClimateTransformer transformer) {
		BlockPos position = transformer.getCoordinates();
		long longPos = position.asLong();
		TransformerData data = transformers.get(longPos);
		if (data != null) {
			boolean needChunkUpdate = data.range != transformer.getRange() || data.circular != transformer.isCircular() || data.chunks.length == 0;
			boolean needClimateUpdate = !data.climateState.equals(transformer.getCurrent());
			data.climateState = transformer.getCurrent();
			if (needChunkUpdate) {
				data.circular = transformer.isCircular();
				data.range = transformer.getRange();
				data.chunks = updateTransformerChunks(transformer, needClimateUpdate);
			} else if (needClimateUpdate) {
				for (long chunkPos : data.chunks) {
					markChunkUpdate(chunkPos);
				}
			}
		} else {
			long[] transformerChunks = updateTransformerChunks(transformer, false);
			transformers.put(longPos, new TransformerData(longPos, transformer.getCurrent(), transformer.getRange(), transformer.isCircular(), transformerChunks));
		}
		setDirty(true);
	}

	private long[] updateTransformerChunks(IClimateTransformer transformer, boolean forceDirty) {
		BlockPos transformerPos = transformer.getCoordinates();
		long longPos = transformerPos.asLong();
		int range = transformer.getRange();
		Set<Long> chunkSet = new HashSet<>();
		for (int x = transformerPos.getX() - range; x <= transformerPos.getX() + range; x += 16) {
			for (int z = transformerPos.getZ() - range; z <= transformerPos.getZ() + range; z += 16) {
				int chunkX = x >> 4;
				int chunkZ = z >> 4;
				long chunkPos = ChunkPos.asLong(chunkX, chunkZ);
				addTransformer(chunkPos, longPos);
				chunkSet.add(chunkPos);
				if (forceDirty) {
					markChunkUpdate(chunkPos);
				}
			}
		}
		return chunkSet.stream().mapToLong(l -> l).toArray();
	}

	public void removeTransformer(IClimateTransformer transformer) {
		removeTransformerChunks(transformer);
		transformers.remove(transformer.getCoordinates().asLong());
		setDirty(true);
	}

	private void removeTransformerChunks(IClimateTransformer transformer) {
		BlockPos transformerPos = transformer.getCoordinates();
		long longPos = transformerPos.asLong();
		TransformerData data = transformers.get(longPos);
		if (data == null) {
			return;
		}
		for (long chunkPos : data.chunks) {
			removeTransformer(chunkPos, longPos);
		}
	}

	public int getRange(long position) {
		return this.transformers.getOrDefault(position, DEFAULT_DATA).range;
	}

	public boolean isCircular(long position) {
		return transformers.getOrDefault(position, DEFAULT_DATA).circular;
	}

	public boolean isPositionInTransformerRange(long position, BlockPos blockPos) {
		BlockPos pos = BlockPos.of(position);
		int rangeSq = getRange(position);
		rangeSq *= rangeSq;
		if (isCircular(position)) {
			double distance = Math.round(blockPos.distSqr(pos));
			return rangeSq > 0f && distance <= rangeSq;
		}
		return Mth.abs(blockPos.getX() - pos.getX()) <= rangeSq && Mth.abs(blockPos.getZ() - pos.getZ()) <= rangeSq;
	}

	@Nullable
	public ClimateState getAdjustedState(BlockPos pos) {
		long chunkPos = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
		if (!transformersByChunk.containsKey(chunkPos)) {
			return null;
		}
		double transformerCount = 0;
		for (long transformerPos : transformersByChunk.get(chunkPos)) {
			if (isPositionInTransformerRange(transformerPos, pos)) {
				//state = state.add(getClimate(transformerPos));
				transformerCount++;
			}
		}
		return null; //transformerCount > 0 ? state.multiply(1.0D / transformerCount).toImmutable() : ClimateStateHelper.INSTANCE.absent();
	}

	public boolean hasTransformers(BlockPos pos) {
		return transformersByChunk.containsKey(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
	}

	public long getLastUpdate(BlockPos pos) {
		return getLastUpdate(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
	}

	public long getLastUpdate(long chunkPos) {
		return chunkUpdates.get(chunkPos);
	}

	private static class TransformerData implements INbtWritable {
		private final long position;
		@Nullable
		private ClimateState climateState;
		private int range;
		private boolean circular;
		private long[] chunks;

		private TransformerData(long position, @Nullable ClimateState climateState, int range, boolean circular, long[] chunks) {
			this.position = position;
			this.climateState = climateState;
			this.range = range;
			this.circular = circular;
			this.chunks = chunks;
		}

		private TransformerData(CompoundTag nbt) {
			this.position = nbt.getLong(POS_KEY);
			this.range = nbt.getInt(RANGE_KEY);
			this.climateState = ClimateState.readFromNbt(nbt.getCompound(STATE_DATA_KEY));
			this.circular = nbt.getBoolean(CIRCULAR_KEY);
			this.chunks = nbt.getLongArray(CHUNKS_KEY);
		}

		@Override
		public CompoundTag write(CompoundTag nbt) {
			nbt.putLong(POS_KEY, position);
			if (climateState != null) {
				nbt.put(STATE_DATA_KEY, climateState.writeToNbt());
			}
			nbt.putInt(RANGE_KEY, range);
			nbt.putBoolean(CIRCULAR_KEY, circular);
			nbt.put(CHUNKS_KEY, new LongArrayTag(chunks));
			return nbt;
		}
	}
}
