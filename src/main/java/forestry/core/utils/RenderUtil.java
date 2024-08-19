package forestry.core.utils;

import java.awt.Color;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.core.ClientsideCode;
import forestry.core.fluids.ForestryFluids;

public class RenderUtil {
	public static void markForUpdate(BlockPos pos) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			ClientsideCode.markForUpdate(pos);
		}
	}

	// requires external push/pop
	public static void rotateByHorizontalDirection(PoseStack stack, Direction facing) {
		if (facing != Direction.SOUTH) {
			stack.translate(0.5, 0.5, 0.5);
			stack.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
			stack.translate(-0.5, -0.5, -0.5);
		}
	}

	// requires external push/pop
	public static void renderDisplayStack(PoseStack stack, ItemRenderer itemRenderer, ItemStack displayStack, Level level, float partialTick, MultiBufferSource buffers, int light) {
		BakedModel itemModel = itemRenderer.getModel(displayStack, level, null, 1);
		boolean isGui3d = itemModel.isGui3d();
		float smoothTick = ((float) (int) level.getGameTime()) + partialTick;
		float f1 = Mth.sin(smoothTick / 10.0f) * 0.1f + 0.1f;
		float f2 = itemModel.getTransforms().getTransform(ItemTransforms.TransformType.GROUND).scale.y();
		stack.translate(0, f1 + 0.25f * f2, 0);
		stack.mulPose(Vector3f.YP.rotation(smoothTick / 20f));

		itemRenderer.render(displayStack, ItemTransforms.TransformType.GROUND, false, stack, buffers, light, OverlayTexture.NO_OVERLAY, itemModel);

		if (!isGui3d) {
			stack.translate(0.0, 0.0, 0.09375F);
		}
	}

	public static int getFluidColor(Fluid fluid) {
		FluidType attributes = fluid.getFluidType();
		int color = IClientFluidTypeExtensions.of(attributes).getTintColor();
		ForestryFluids definition = ForestryFluids.getFluidDefinition(fluid);
		if (color < 0) {
			color = Color.BLUE.getRGB();
			if (definition != null) {
				color = definition.getParticleColor().getRGB();
			}
		}
		return color;
	}
}
