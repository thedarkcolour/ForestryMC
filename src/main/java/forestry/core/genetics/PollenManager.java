package forestry.core.genetics;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.genetics.pollen.IPollen;
import forestry.api.genetics.pollen.IPollenManager;
import forestry.api.genetics.pollen.IPollenType;


public class PollenManager implements IPollenManager {
	private final ImmutableMap<ResourceLocation, IPollenType<?>> types;

	public PollenManager(ImmutableMap<ResourceLocation, IPollenType<?>> types) {
		this.types = types;
	}

	@Override
	public boolean canPollinate(LevelAccessor level, BlockPos pos, @org.jetbrains.annotations.Nullable Object pollinator) {
		for (IPollenType<?> type : this.types.values()) {
			if (type.canPollinate(level, pos, pollinator)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	@Override
	public IPollen<?> getPollen(LevelAccessor level, BlockPos pos, @Nullable Object pollinator) {
		for (IPollenType<?> type : this.types.values()) {
			IPollen<?> pollen = type.tryCollectPollen(level, pos, pollinator);

			if (pollen != null) {
				return pollen;
			}
		}

		return null;
	}

	@Nullable
	@Override
	public IPollen<?> getPollenOfType(LevelAccessor level, BlockPos pos, @Nullable Object pollinator, Set<ResourceLocation> pollenTypes) {
		for (ResourceLocation id : pollenTypes) {
			IPollenType<?> type = this.types.get(id);

			if (type != null) {
				IPollen<?> pollen = type.tryCollectPollen(level, pos, pollinator);

				if (pollen != null) {
					return pollen;
				}
			}
		}

		return null;
	}

	@Nullable
	@Override
	public IPollenType<?> getPollenType(ResourceLocation id) {
		return this.types.get(id);
	}

	@Override
	public Collection<IPollenType<?>> getAllPollenTypes() {
		return this.types.values();
	}
}
