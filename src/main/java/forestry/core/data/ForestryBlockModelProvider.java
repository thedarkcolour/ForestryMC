package forestry.core.data;

import deleteme.RegistryNameFinder;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import forestry.core.config.Constants;
import forestry.core.fluids.ForestryFluids;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ForestryBlockModelProvider extends BlockModelProvider {

	public ForestryBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Constants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (ForestryFluids fluid : ForestryFluids.values()) {
			Block block = fluid.getFeature().fluidBlock().block();
			getBuilder(RegistryNameFinder.getRegistryName(block).getPath())
					.texture("particle", fluid.getFeature().properties().resources[0]);
		}
	}
}
