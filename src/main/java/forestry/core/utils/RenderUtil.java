package forestry.core.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.core.ClientsideCode;

/**
 * Util methods used at the runtime of the game based around the rendering.
 */
public class RenderUtil {

	private RenderUtil() {
	}

	public static void markForUpdate(BlockPos pos) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			ClientsideCode.markForUpdate(pos);
		}
	}
}
