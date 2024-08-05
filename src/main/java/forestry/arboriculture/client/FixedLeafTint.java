package forestry.arboriculture.client;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;

import forestry.api.client.arboriculture.ILeafTint;

public record FixedLeafTint(int color) implements ILeafTint {
	// TODO use for Azalea and Cherry trees
	public static final FixedLeafTint NONE = new FixedLeafTint(0xffffff);

	@Override
	public int get(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
		return this.color;
	}
}
