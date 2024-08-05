package forestry.api.client.arboriculture;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

/**
 * Responsible for tinting leaf colors according to their environment.
 * The default implementation for vanilla species is using the biome's foliage color, while Forestry species tint
 * based on the escritoire color of the species.
 */
public interface ILeafTint {
	int get(@Nullable BlockGetter level, @Nullable BlockPos pos);
}
