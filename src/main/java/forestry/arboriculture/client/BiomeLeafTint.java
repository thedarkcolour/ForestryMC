package forestry.arboriculture.client;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import forestry.api.client.arboriculture.ILeafTint;

import org.jetbrains.annotations.Nullable;

// Based on the block color for Oak, Jungle, Dark Oak, and Acacia leaves
public enum BiomeLeafTint implements ILeafTint {
	INSTANCE;

	@Override
	public int get(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
		if (level != null && pos != null) {
			return BiomeColors.getAverageFoliageColor(level, pos);
		}
		// from FoliageColor.getDefaultColor
		return 0x48b518;
	}
}
