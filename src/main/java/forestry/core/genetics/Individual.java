package forestry.core.genetics;

import javax.annotation.Nullable;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.core.utils.SpeciesUtil;

public abstract class Individual<S extends ISpecies<I>, I extends IIndividual, T extends ISpeciesType<S, I>> implements IIndividual {
	protected final S species;
	protected final S inactiveSpecies;
	protected final IGenome genome;

	@Nullable
	protected IGenome mate;
	protected boolean isAnalyzed;

	public Individual(IGenome genome) {
		this.species = genome.getActiveSpecies();
		this.inactiveSpecies = genome.getInactiveSpecies();
		this.genome = genome;
	}

	@Override
	public void setMate(@Nullable IGenome mate) {
		if (mate == null || this.genome.getKaryotype() == mate.getKaryotype()) {
			this.mate = mate;
		}
	}

	@Nullable
	@Override
	public IGenome getMate() {
		return this.mate;
	}

	@Override
	public IGenome getGenome() {
		return this.genome;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getType() {
		return (T) this.species.getType();
	}

	@Override
	public S getSpecies() {
		return this.species;
	}

	@Override
	public S getInactiveSpecies() {
		return this.inactiveSpecies;
	}

	@Override
	public boolean isAnalyzed() {
		return this.isAnalyzed;
	}

	@Override
	public boolean analyze() {
		if (this.isAnalyzed) {
			return false;
		}

		this.isAnalyzed = true;
		return true;
	}

	@Override
	public I copy() {
		// todo should i copy the mate?
		return this.species.createIndividual(this.genome);
	}

	@Override
	public void saveToStack(ItemStack stack) {
		Tag individual = SpeciesUtil.serializeIndividual(this);
		if (individual != null) {
			stack.getOrCreateTag().put("individual", individual);
		}
	}

	@Override
	public ItemStack copyWithStage(ILifeStage stage) {
		ItemStack stack = new ItemStack(stage.getItemForm());
		saveToStack(stack);
		return stack;
	}
}
