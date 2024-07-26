package forestry.core.genetics;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.ILifeStage;

public class IndividualHandler implements ICapabilityProvider, IIndividualHandler {
	private final LazyOptional<IIndividualHandler> holder;

	private final ItemStack container;
	private final IIndividual individual;
	private final ILifeStage stage;


	public IndividualHandler(ItemStack container, IIndividual individual, ILifeStage stage) {
		this.container = container;
		this.individual = individual;
		this.stage = stage;
		this.holder = LazyOptional.of(() -> this);
	}

	@Override
	public ILifeStage getStage() {
		return this.stage;
	}

	@Override
	public IIndividual getIndividual() {
		return this.individual;
	}

	@Override
	public ItemStack getContainer() {
		return this.container;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		return ForestryCapabilities.INDIVIDUAL.orEmpty(capability, this.holder);
	}
}
