package forestry.apiimpl.plugin;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.IForestryApi;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.ITaxon;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IKaryotype;
import forestry.api.plugin.IGenomeBuilder;
import forestry.api.plugin.ISpeciesBuilder;

/**
 * Base implementation of {@link ISpeciesBuilder} with common logic.
 *
 * @param <I> Interface type of the species builders used by this species registration.
 * @param <S> Interface type of the species registered by this species registration.
 * @param <B> The concrete type of the species builder used by this species registration.
 */
public abstract class SpeciesRegistration<I extends ISpeciesBuilder<? extends ISpeciesType<S, ?>, S, I>, S extends ISpecies<?>, B extends I> {
	@SuppressWarnings({"unchecked", "rawtypes"})
	private final ModifiableRegistrar<ResourceLocation, I, B> species = new ModifiableRegistrar(ISpeciesBuilder.class);

	protected final ISpeciesType<S, ?> type;

	public SpeciesRegistration(ISpeciesType<S, ?> type) {
		this.type = type;
	}

	protected abstract B createSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations);

	protected I register(ResourceLocation id, String genus, String species) {
		return this.species.create(id, createSpeciesBuilder(id, genus, species, new MutationsRegistration(id)));
	}

	public void modifySpecies(ResourceLocation id, Consumer<I> action) {
		this.species.modify(id, action);
	}

	// Creates final map of species and populates the species chromosome
	public ImmutableMap<ResourceLocation, S> buildSpecies() {
		IKaryotype karyotype = this.type.getKaryotype();

		ImmutableMap<ResourceLocation, S> allSpecies = this.species.build((id, builder) -> {
			// create default genome builder
			IGenomeBuilder defaultGenomeBuilder = karyotype.createGenomeBuilder();
			ITaxon[] ancestry = IForestryApi.INSTANCE.getGeneticManager().getParentTaxa(builder.getGenus());

			// apply default genomes from parent taxa
			for (ITaxon taxon : ancestry) {
				for (Map.Entry<IChromosome<?>, ITaxon.TaxonAllele> alleleEntry : taxon.alleles().entrySet()) {
					IAllele allele = alleleEntry.getValue().allele();
					defaultGenomeBuilder.set(alleleEntry.getKey(), allele.cast());
				}
			}

			// set default chromosomes that weren't overridden by taxa
			defaultGenomeBuilder.setRemainingDefault();

			IGenome defaultGenome = builder.buildGenome(defaultGenomeBuilder);

			return builder.createSpeciesFactory().create(id, this.type.cast(), defaultGenome, builder);
		});

		// populate default species chromosome
		karyotype.getSpeciesChromosome().populate((ImmutableMap) allSpecies);

		return allSpecies;
	}
}
