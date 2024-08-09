package forestry.core.render;

import net.minecraft.client.model.geom.ModelLayerLocation;

import forestry.api.ForestryConstants;

public class ForestryModelLayers {
	public static final ModelLayerLocation BUTTERFLY_LAYER = register("butterfly");
	public static final ModelLayerLocation NATURALIST_CHEST_LAYER = register("naturalist_chest");
	public static final ModelLayerLocation MILL_LAYER = register("mill");
	public static final ModelLayerLocation MACHINE_LAYER = register("machine");
	public static final ModelLayerLocation ESCRITOIRE_LAYER = register("escritoire");
	public static final ModelLayerLocation ENGINE_LAYER = register("engine");
	public static final ModelLayerLocation ANALYZER_LAYER = register("analyzer");

	public static ModelLayerLocation register(String name) {
		return new ModelLayerLocation(ForestryConstants.forestry(name), "main");
	}
}
