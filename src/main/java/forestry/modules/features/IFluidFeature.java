package forestry.modules.features;

import javax.annotation.Nullable;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.BlockItem;

import net.minecraftforge.fluids.FluidStack;

import forestry.core.fluids.BlockForestryFluid;
import net.minecraftforge.fluids.FluidType;

public interface IFluidFeature extends IModFeature {

	FeatureBlock<BlockForestryFluid, BlockItem> fluidBlock();

	FluidProperties properties();

	FlowingFluid fluid();

	FlowingFluid flowing();

	default FluidStack fluidStack(int amount) {
		return new FluidStack(fluid(), amount);
	}

	default FluidStack fluidStack() {
		return fluidStack(FluidType.BUCKET_VOLUME);
	}
}
