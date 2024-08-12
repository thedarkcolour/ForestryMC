package forestry.core.genetics.capability;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.capability.IIndividualHandlerItem;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

// Used for Vanilla sapling items.
public class IndividualHandlerItem implements ICapabilityProvider, IIndividualHandlerItem {
	private final LazyOptional<IIndividualHandlerItem> holder = LazyOptional.of(() -> this);

	protected final ISpeciesType<?, ?> speciesType;
	protected final ItemStack container;
	protected IIndividual individual;
	protected final ILifeStage stage;

	public IndividualHandlerItem(ISpeciesType<?, ?> type, ItemStack container, IIndividual individual, ILifeStage stage) {
		this.speciesType = type;
		this.container = container;
		this.individual = individual;
		this.stage = stage;
	}

	@Override
	public ISpeciesType<?, ?> getSpeciesType() {
		return this.speciesType;
	}

	@Override
	public ILifeStage getStage() {
		return this.stage;
	}

	@Override
	public IIndividual getIndividual() {
		return this.individual;
	}

	public ItemStack getContainer() {
		return this.container;
	}

	@Override
	public boolean isGeneticForm() {
		return this.container.is(getStage().getItemForm());
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		return ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM.orEmpty(capability, this.holder);
	}
}
