package forestry.core.items.definitions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

/**
 * Fluid handler that consumes the container item after it was used.
 */
public class FluidHandlerItemForestry extends FluidHandlerItemStackSimple.Consumable {
	private final EnumContainerType containerType;

	public FluidHandlerItemForestry(ItemStack container, EnumContainerType containerType) {
		super(container, FluidType.BUCKET_VOLUME);
		this.containerType = containerType;
	}

	/**
	 * Checks if the given fluid can be contained in this handler. This property gets defined by the container type of
	 * this handler.
	 * <p>
	 * Capsules can't contain fluid that are hotter or equal to the melting point of wax
	 */
	private boolean contentsAllowed(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		if (fluid == null) {
			return false;
		}

		if (containerType == EnumContainerType.CAPSULE) {
			return fluid.getFluidType().getTemperature(fluidStack) < 310.15; // melting point of wax in kelvin
		}
		return true;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid) {
		return contentsAllowed(fluid);
	}

	@Override
	protected void setFluid(FluidStack fluid) {
		super.setFluid(fluid);
		container.setDamageValue(1); // show the filled container model
	}
}

