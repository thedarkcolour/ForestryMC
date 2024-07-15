package forestry.core.utils;

import net.minecraft.core.BlockPos;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.core.ClientsideCode;

public class RenderUtil {
	public static void markForUpdate(BlockPos pos) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			ClientsideCode.markForUpdate(pos);
		}
	}
}
