package forestry.core.genetics.capability;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.capability.IIndividualHandler;

import deleteme.Todos;

public class IndividualHandler implements ICapabilityProvider, IIndividualHandler {
	@Override
	public ISpeciesType<?, ?> getSpeciesType() {
		return null;
	}

	@Override
	public ILifeStage getStage() {
		return null;
	}

	@Override
	public IIndividual getIndividual() {
		return null;
	}

	@Override
	public boolean isGeneticForm() {
		return false;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		throw Todos.unimplemented();
		//return ForestryCapabilities.INDIVIDUAL_HANDLER.orEmpty(capability, this.holder);
	}
}
