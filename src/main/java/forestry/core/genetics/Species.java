package forestry.core.genetics;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.IForestryApi;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.Taxon;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.ISpeciesBuilder;
import forestry.core.utils.GeneticsUtil;

public abstract class Species<T extends ISpeciesType<? extends ISpecies<I>, I>, I extends IIndividual> implements ISpecies<I> {
	protected final ResourceLocation id;
	protected final T speciesType;
	protected final IGenome defaultGenome;
	protected final int complexity;
	protected final boolean secret;
	protected final boolean glint;
	protected final boolean dominant;
	protected final String authority;
	protected final String species;
	protected final Taxon genus;
	protected final String binomial;
	protected final String translationKey;

	public Species(ResourceLocation id, T speciesType, IGenome defaultGenome, ISpeciesBuilder<T, ?> builder) {
		this.id = id;
		this.speciesType = speciesType;
		this.defaultGenome = defaultGenome;

		this.complexity = builder.getComplexity();
		this.secret = builder.isSecret();
		this.glint = builder.hasGlint();
		this.dominant = builder.isDominant();
		this.authority = builder.getAuthority();
		this.species = builder.getSpecies();
		this.genus = IForestryApi.INSTANCE.getGeneticManager().getTaxon(builder.getGenus());
		this.binomial = createBinomial(this.genus.name(), this.species);
		this.translationKey = GeneticsUtil.createTranslationKey("species", speciesType.id(), id);
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

	@Override
	public boolean hasGlint() {
		return this.glint;
	}

	@Override
	public Taxon getGenus() {
		return this.genus;
	}

	@Override
	public boolean isDominant() {
		return this.dominant;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}

	@Override
	public I createIndividual(Map<IChromosome<?>, IAllele> alleles) {
		return createIndividual(this.defaultGenome.copyWith(alleles));
	}
}
