package forestry.core.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.math.Vector3f;

import forestry.core.blocks.BlockBase;
import forestry.core.config.Constants;
import forestry.core.tiles.TemperatureState;
import forestry.energy.tiles.EngineBlockEntity;

public class RenderEngine implements IForestryRenderer<EngineBlockEntity> {
	private static final float[] angleMap = new float[6];

	private enum Textures {
		BASE, PISTON, EXTENSION, TRUNK_HIGHEST, TRUNK_HIGHER, TRUNK_HIGH, TRUNK_MEDIUM, TRUNK_LOW
	}

	public static final ModelLayerLocation MODEL_LAYER = IForestryRenderer.register("engine");

	private final ResourceLocation[] textures;
	private final ModelPart boiler;
	private final ModelPart trunk;
	private final ModelPart piston;
	private final ModelPart extension;

	static {
		angleMap[Direction.EAST.ordinal()] = -Mth.HALF_PI;
		angleMap[Direction.NORTH.ordinal()] = -Mth.HALF_PI;
		angleMap[Direction.WEST.ordinal()] = Mth.HALF_PI;
		angleMap[Direction.SOUTH.ordinal()] = Mth.HALF_PI;
		angleMap[Direction.UP.ordinal()] = 0;
		angleMap[Direction.DOWN.ordinal()] = Mth.PI;
	}

	public RenderEngine(ModelPart root, String baseTexture) {
		this.boiler = root.getChild("boiler");
		this.trunk = root.getChild("trunk");
		this.piston = root.getChild("piston");
		this.extension = root.getChild("extension");

		this.textures = new ResourceLocation[]{
				new ForestryResource(baseTexture + "base.png"),
				new ForestryResource(baseTexture + "piston.png"),
				new ForestryResource(baseTexture + "extension.png"),
				new ForestryResource(Constants.TEXTURE_PATH_BLOCK + "/engine_trunk_highest.png"),
				new ForestryResource(Constants.TEXTURE_PATH_BLOCK + "/engine_trunk_higher.png"),
				new ForestryResource(Constants.TEXTURE_PATH_BLOCK + "/engine_trunk_high.png"),
				new ForestryResource(Constants.TEXTURE_PATH_BLOCK + "/engine_trunk_medium.png"),
				new ForestryResource(Constants.TEXTURE_PATH_BLOCK + "/engine_trunk_low.png"),
		};
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("boiler", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-8f, -8f, -8f, 16, 6, 16), PartPose.offset(8, 8, 8));
		root.addOrReplaceChild("trunk", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-4f, -4f, -4f, 8, 12, 8), PartPose.offset(8, 8, 8));
		root.addOrReplaceChild("piston", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-6f, -2f, -6f, 12, 4, 12), PartPose.offset(8, 8, 8));
		root.addOrReplaceChild("extension", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5f, -3f, -5f, 10, 2, 10), PartPose.offset(8, 8, 8));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void renderTile(EngineBlockEntity tile, RenderHelper helper) {
		BlockState blockState = tile.getBlockState();
		Direction facing = blockState.getValue(BlockBase.FACING);

		float progress = tile.progress;
		if (tile.stagePiston != 0) {
			float smoothing = tile.pistonSpeedServer * helper.partialTicks;
			progress = (progress + smoothing);
		}
		render(tile.getTemperatureState(), progress, facing, helper);
	}

	@Override
	public void renderItem(ItemStack stack, RenderHelper helper) {
		render(TemperatureState.COOL, 0.25F, Direction.UP, helper);
	}

	private void render(TemperatureState state, float progress, Direction orientation, RenderHelper helper) {
		float step;

		if (progress > 0.5f) {
			step = 6f - (progress - 0.5f) * 2f * 6F;
		} else {
			step = progress * 2f * 6f;
		}

		float tfactor = step / 16;

		Vector3f rotation = new Vector3f(0, 0, 0);
		float[] translate = {orientation.getStepX(), orientation.getStepY(), orientation.getStepZ()};

		switch (orientation) {
			case EAST:
			case WEST:
			case DOWN:
				rotation.setZ(angleMap[orientation.ordinal()]);
				break;
			case SOUTH:
			case NORTH:
			default:
				rotation.setX(angleMap[orientation.ordinal()]);
				break;
		}

		helper.setRotation(rotation);
		helper.renderModel(textures[Textures.BASE.ordinal()], boiler);

		helper.push();

		helper.translate(translate[0] * tfactor, translate[1] * tfactor, translate[2] * tfactor);
		helper.renderModel(textures[Textures.PISTON.ordinal()], piston);
		helper.translate(-translate[0] * tfactor, -translate[1] * tfactor, -translate[2] * tfactor);

		ResourceLocation texture = switch (state) {
			case OVERHEATING -> textures[Textures.TRUNK_HIGHEST.ordinal()];
			case RUNNING_HOT -> textures[Textures.TRUNK_HIGHER.ordinal()];
			case OPERATING_TEMPERATURE -> textures[Textures.TRUNK_HIGH.ordinal()];
			case WARMED_UP -> textures[Textures.TRUNK_MEDIUM.ordinal()];
			default -> textures[Textures.TRUNK_LOW.ordinal()];
		};

		helper.renderModel(texture, trunk);

		float chamberf = 2F / 16F;

		if (step > 0) {
			for (int i = 0; i <= step + 2; i += 2) {
				helper.renderModel(textures[Textures.EXTENSION.ordinal()], extension);
				helper.translate(translate[0] * chamberf, translate[1] * chamberf, translate[2] * chamberf);
			}
		}
		helper.pop();
	}
}
