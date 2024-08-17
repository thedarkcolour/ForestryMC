package forestry.core.data;

import javax.annotation.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;

import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import forestry.api.ForestryConstants;
import forestry.api.ForestryTags;
import forestry.core.fluids.ForestryFluids;

public class ForestryFluidTagsProvider extends FluidTagsProvider {
	public ForestryFluidTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
		super(generator, ForestryConstants.MOD_ID, fileHelper);
	}

	@Override
	protected void addTags() {
		tag(Tags.Fluids.MILK).add(ForestryFluids.MILK.getFluid(), ForestryFluids.MILK.getFlowing());
		tag(ForestryTags.Fluids.HONEY).add(ForestryFluids.HONEY.getFluid(), ForestryFluids.HONEY.getFlowing());
	}


	@Override
	public String getName() {
		return "Forestry Fluid Tags";
	}
}
