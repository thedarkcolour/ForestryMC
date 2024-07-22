package forestry.core.genetics;

import javax.annotation.Nullable;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

public abstract class Individual<S extends ISpecies<?>, T extends ISpeciesType<S>> implements IIndividual {
	protected final S species;
	protected final IGenome genome;
	protected final ILifeStage stage;

	@Nullable
	protected IGenome mate;
	protected boolean isAnalyzed;

	public Individual(S species, IGenome genome, ILifeStage stage) {
		this.species = species;
		this.genome = genome;
		this.stage = stage;
	}

	@Override
	public boolean mate(@Nullable IGenome mate) {
		if (mate != null && this.genome.getKaryotype() != mate.getKaryotype()) {
			return false;
		} else {
			this.mate = mate;
			return true;
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
	public ILifeStage getLifeStage() {
		return this.stage;
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
	public boolean isAnalyzed() {
		return this.isAnalyzed;
	}
}
