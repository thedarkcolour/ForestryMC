package forestry.core.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.DistExecutor;

/**
 * Util methods used at the runtime of the game based around the rendering.
 */
public class RenderUtil {

	private RenderUtil() {
	}

	public static void markForUpdate(BlockPos pos) {
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new ClientRun(pos)::markForUpdate);
	}

	private record ClientRun(BlockPos pos) {
		private void markForUpdate() {
			Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		}
	}
}
