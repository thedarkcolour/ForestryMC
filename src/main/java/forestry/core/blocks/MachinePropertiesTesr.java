package forestry.core.blocks;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

import forestry.core.config.Constants;
import forestry.core.render.IForestryRendererProvider;
import forestry.core.render.RenderForestryTile;
import forestry.core.tiles.ForestryTicker;
import forestry.core.tiles.TileForestry;
import forestry.modules.features.FeatureTileType;

public class MachinePropertiesTesr<T extends TileForestry> extends MachineProperties<T> implements IMachinePropertiesTesr<T> {

	private final ResourceLocation particleTexture;

	@Nullable
	@OnlyIn(Dist.CLIENT)
	private ModelLayerLocation modelLayer;
	
	@Nullable
	@OnlyIn(Dist.CLIENT)
	private IForestryRendererProvider<? super T> renderer;

	public MachinePropertiesTesr(Supplier<FeatureTileType<? extends T>> teType, String name, IShapeProvider shape, @Nullable ForestryTicker<? extends T> clientTicker, @Nullable ForestryTicker<? extends T> serverTicker, @Nullable ResourceLocation particleTexture) {
		super(teType, name, shape, clientTicker, serverTicker);
		this.particleTexture = particleTexture;
	}

	@OnlyIn(Dist.CLIENT)
	public void setRenderer(ModelLayerLocation modelLayer, IForestryRendererProvider<? super T> renderer) {
		this.modelLayer = modelLayer;
		this.renderer = renderer;
	}

	@Override
	@Nullable
	public IForestryRendererProvider<? super T> getRenderer() {
		return renderer;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		if (renderer != null) {
			event.registerBlockEntityRenderer(getTeType(), (ctx) -> new RenderForestryTile<>(renderer.create(ctx.bakeLayer(modelLayer))));
		}
	}

	@Override
	public ResourceLocation getParticleTexture() {
		return particleTexture;
	}

	@Override
	public ModelLayerLocation getModelLayer() {
		return modelLayer;
	}

	public static class Builder<T extends TileForestry> extends MachineProperties.Builder<T, Builder<T>> {
		@Nullable
		private ResourceLocation particleTexture;

		public Builder(Supplier<FeatureTileType<? extends T>> type, String name) {
			super(type, name);
		}

		public Builder<T> setParticleTexture(String particleTexture) {
			return setParticleTexture(new ResourceLocation(Constants.MOD_ID, "block/" + particleTexture));
		}

		public Builder<T> setParticleTexture(ResourceLocation particleTexture) {
			this.particleTexture = particleTexture;
			return this;
		}

		@Override
		public MachinePropertiesTesr<T> create() {
			Preconditions.checkNotNull(shape);
			Preconditions.checkNotNull(particleTexture);
			return new MachinePropertiesTesr<>(type, name, shape, clientTicker, serverTicker, particleTexture);
		}
	}
}
