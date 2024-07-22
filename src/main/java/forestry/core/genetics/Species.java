package forestry.core.genetics;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.plugin.ISpeciesBuilder;

public abstract class Species<T extends ISpeciesType<? extends ISpecies<I>>, I extends IIndividual> implements ISpecies<I> {
	protected final ResourceLocation id;
	protected final T speciesType;
	protected final IGenome defaultGenome;
	protected final int complexity;
	protected final boolean secret;
	protected final String binomial;
	protected final String translationKey;

	public Species(ResourceLocation id, T speciesType, IGenome defaultGenome, ISpeciesBuilder<?> builder) {
		this.id = id;
		this.speciesType = speciesType;
		this.defaultGenome = defaultGenome;

		this.complexity = builder.getComplexity();
		this.secret = builder.isSecret();
		this.binomial = createBinomial(builder.getGenus(), builder.getSpecies());
		this.translationKey = createTranslationKey(speciesType.id(), id);
	}

	private static String createBinomial(String genus, String species) {
		@SuppressWarnings("StringBufferReplaceableByString")
		StringBuilder binomial = new StringBuilder();
		binomial.append(Character.toUpperCase(genus.charAt(0)));
		// direct array copy, faster than creating a substring
		binomial.append(genus, 1, genus.length());
		binomial.append(' ');
		binomial.append(species);
		return binomial.toString();
	}

	private static String createTranslationKey(ResourceLocation typeId, ResourceLocation speciesId) {
		String typeNamespace = typeId.getNamespace();
		StringBuilder translationKey = new StringBuilder("species.");

		translationKey.append(typeNamespace);
		translationKey.append('.');
		translationKey.append(typeId.getPath());
		translationKey.append('.');

		String speciesNamespace = speciesId.getNamespace();
		if (speciesNamespace.equals(typeNamespace)) {
			// for species from the same mod as species type, use the following format:
			// species.forestry.bee.austere
			translationKey.append(speciesId.getPath());
		} else {
			// if species type is from another mod, use this format instead:
			// species.forestry.bee.extrabees.creeper
			translationKey.append(speciesNamespace);
			translationKey.append('.');
			translationKey.append(speciesId.getPath());
		}

		return translationKey.toString();
	}

	@Override
	public String getBinomial() {
		return this.binomial;
	}

	@Override
	public String getTranslationKey() {
		return this.translationKey;
	}

	@Override
	public IGenome getDefaultGenome() {
		return this.defaultGenome;
	}

	@Override
	public ResourceLocation id() {
		return this.id;
	}

	@Override
	public T getType() {
		return this.speciesType;
	}

	@Override
	public boolean isSecret() {
		return this.secret;
	}

	@Override
	public int getComplexity() {
		return this.complexity;
	}
}
