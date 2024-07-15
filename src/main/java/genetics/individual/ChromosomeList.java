package genetics.individual;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IChromosomeList;
import forestry.api.genetics.alleles.IChromosome;
import genetics.api.individual.IChromosomeTypeBuilder;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IRootDefinition;

public class ChromosomeList implements IChromosomeList {
	private final String rootUID;
	private final ISpeciesType<?> root;
	private final List<IChromosome> types = new LinkedList<>();

	public ChromosomeList(String rootUID) {
		this.rootUID = rootUID;
		this.root = GeneticsAPI.apiInstance.getRoot(rootUID);
	}

	@Override
	public IChromosomeTypeBuilder builder() {
		return new ChromosomeBuilder(this);
	}

	@Override
	public Collection<IChromosome> types() {
		return types;
	}

	@Override
	public IChromosome[] typesArray() {
		return types.toArray(new IChromosome[0]);
	}

	@Override
	public int size() {
		return types.size();
	}

	@Override
	public String getUID() {
		return rootUID;
	}

	public <T extends IChromosome> T add(T type) {
		types.add(type);
		return type;
	}

	@Override
	public Iterator<IChromosome> iterator() {
		return types.iterator();
	}

	@Override
	public ISpeciesType<?> getRoot() {
		return this.root;
	}
}
