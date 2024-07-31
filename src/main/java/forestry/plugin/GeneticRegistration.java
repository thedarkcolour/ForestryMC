package forestry.plugin;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ForestryTaxa;
import forestry.api.genetics.TaxonomicRank;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.api.plugin.IGeneticRegistration;
import forestry.api.plugin.ISpeciesTypeBuilder;
import forestry.api.plugin.ISpeciesTypeFactory;
import forestry.api.plugin.ITaxonBuilder;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public final class GeneticRegistration implements IGeneticRegistration {
	// Already defined taxa that have known parents
	private final HashMap<String, TaxonBuilder> taxaByName = new HashMap<>();
	// Unknown parent name -> Child name
	private final HashMap<String, HashSet<String>> unknownTaxa = new HashMap<>();
	// Name of taxon with missing parent -> Action
	private final HashMap<String, Consumer<ITaxonBuilder>> unknownActions = new HashMap<>();
	// Species type builders
	private final HashMap<ResourceLocation, SpeciesTypeBuilder> builders = new HashMap<>();
	// Species type modifications, run just before registry is finalized
	private final HashMap<ResourceLocation, ArrayList<Consumer<ISpeciesTypeBuilder>>> modifications = new HashMap<>();
	// Filter rule types used by IFilterRegistry
	private final ArrayList<IFilterRuleType> ruleTypes = new ArrayList<>();
	// Used to throw exceptions when a mod tries to register something too late
	private boolean registeredSpecies;

	public GeneticRegistration() {
		// Register default Domain and Kingdom taxa according to seven kingdoms
		TaxonBuilder prokaryota = registerTaxon(TaxonomicRank.DOMAIN, ForestryTaxa.DOMAIN_PROKARYOTA);
		prokaryota.defineSubTaxon(ForestryTaxa.KINGDOM_ARCHAEA);
		prokaryota.defineSubTaxon(ForestryTaxa.KINGDOM_BACTERIA);

		TaxonBuilder eukaryota = registerTaxon(TaxonomicRank.DOMAIN, ForestryTaxa.DOMAIN_EUKARYOTA);
		eukaryota.defineSubTaxon(ForestryTaxa.KINGDOM_FUNGI);
		eukaryota.defineSubTaxon(ForestryTaxa.KINGDOM_PLANT);
		eukaryota.defineSubTaxon(ForestryTaxa.KINGDOM_ANIMAL, animalia -> {
			animalia.defineSubTaxon(ForestryTaxa.PHYLUM_ARTHROPODS, arthropoda -> {
				arthropoda.defineSubTaxon(ForestryTaxa.CLASS_INSECTS);
			});
		});
		eukaryota.defineSubTaxon(ForestryTaxa.KINGDOM_PROTOZOA);
		eukaryota.defineSubTaxon(ForestryTaxa.KINGDOM_CHROMISTA);
	}

	@Override
	public void defineTaxon(String parent, String name) {
		if (this.taxaByName.containsKey(parent)) {
			// immediately define it if the parent is known
			this.taxaByName.get(parent).defineSubTaxon(name);
		} else {
			// defer if parent is not known, keeping track of which taxa are dependent upon this parent
			this.unknownTaxa.computeIfAbsent(parent, key -> new HashSet<>()).add(name);
		}
	}

	@Override
	public void defineTaxon(String parent, String name, Consumer<ITaxonBuilder> action) {
		if (this.taxaByName.containsKey(parent)) {
			this.taxaByName.get(parent).defineSubTaxon(name, action);
		} else {
			this.unknownTaxa.computeIfAbsent(parent, key -> new HashSet<>()).add(name);
			// also defer configuration of the taxon until it can be created
			this.unknownActions.put(name, action);
		}
	}

	@Override
	public ISpeciesTypeBuilder registerSpeciesType(ResourceLocation id, ISpeciesTypeFactory factory) {
		Preconditions.checkState(!this.registeredSpecies, "Species must be registered or modified in IForestryPlugin.registerGenetics.");

		if (this.builders.containsKey(id)) {
			throw new IllegalStateException("A species type was already registered with ID " + id + " - modify it instead using IGeneticRegistration.modifySpeciesType");
		} else {
			return new SpeciesTypeBuilder(factory);
		}
	}

	@Override
	public void modifySpeciesType(ResourceLocation id, Consumer<ISpeciesTypeBuilder> action) {
		Preconditions.checkState(!this.registeredSpecies, "Species must be registered or modified in IForestryPlugin.registerGenetics.");
		this.modifications.computeIfAbsent(id, key -> new ArrayList<>()).add(action);
	}

	private TaxonBuilder registerTaxon(TaxonomicRank rank, String name) {
		if (this.taxaByName.containsKey(name)) {
			TaxonBuilder existing = this.taxaByName.get(name);

			if (existing.rank == rank) {
				// registering the same taxon twice is fine, just return the existing one
				return existing;
			} else {
				// registering different taxa with the same name is not fine, names must be unique
				throw new RuntimeException("A taxon with name '" + name + "' is already defined in rank " + existing + " but plugin tried to set it in rank " + rank);
			}
		} else {
			// register a new taxon
			TaxonBuilder builder = new TaxonBuilder(this, rank, name);
			this.taxaByName.put(name, builder);

			// if there are children who list this taxon as a parent...
			HashSet<String> dependents = this.unknownTaxa.remove(name);
			if (dependents != null) {
				// register and configure them
				for (String childName : dependents) {
					Consumer<ITaxonBuilder> action = this.unknownActions.remove(childName);
					// this eventually leads back to this method, recursively loading their dependents as well
					if (action != null) {
						builder.defineSubTaxon(name, action);
					} else {
						builder.defineSubTaxon(name);
					}
				}
			}

			return builder;
		}
	}

	public void finishRegistration() {
		Preconditions.checkState(!this.registeredSpecies, "Registration of species is already finished. Some mod is being pesky!");
		this.registeredSpecies = true;
	}

	@Override
	public void registerFilterRuleType(IFilterRuleType ruleType) {
		this.ruleTypes.add(ruleType);
	}

	private static class TaxonBuilder implements ITaxonBuilder {
		private final GeneticRegistration registration;
		private final TaxonomicRank rank;
		private final String name;
		private final HashSet<TaxonBuilder> children = new HashSet<>();
		private final Reference2ObjectOpenHashMap<IChromosome<?>, AlleleHolder> defaultChromosomes = new Reference2ObjectOpenHashMap<>();
		// Must not be null by end of registration unless this is a domain
		@Nullable
		private TaxonBuilder parent;

		private TaxonBuilder(GeneticRegistration registration, TaxonomicRank rank, String name) {
			this.registration = registration;
			this.rank = rank;
			this.name = name;
		}

		@Override
		public void defineSubTaxon(String name) {
			registerChild(name);
		}

		@Override
		public void defineSubTaxon(String name, Consumer<ITaxonBuilder> action) {
			action.accept(registerChild(name));
		}

		private TaxonBuilder registerChild(String name) {
			if (this.rank == TaxonomicRank.GENUS) {
				throw new UnsupportedOperationException("Cannot directly add species '" + name + "' as a child of the '" + this.name + "' genus. Genera are populated by the ISpeciesBuilder.");
			}

			// defining the same child twice is fine...
			TaxonBuilder existing = this.registration.taxaByName.get(name);
			if (existing != null && existing.parent != this) {
				// ...but not when the child already has a parent somewhere else
				String parentName = existing.parent == null ? "null" : existing.parent.name;
				throw new IllegalStateException("Tried to set a taxon with name '" + name + "' as child of taxon '" + this.name + "' but that taxon is already a child of a taxon named'" + parentName + "'");
			}
			// register a new child and establish the parent-child relationship
			TaxonBuilder child = this.registration.registerTaxon(this.rank.next(), name);
			this.children.add(child);
			child.parent = this;
			return child;
		}

		@Override
		public <A extends IAllele> void setDefaultChromosome(IChromosome<A> chromosome, A value, boolean required) {
			this.defaultChromosomes.put(chromosome, new AlleleHolder(value, required));
		}
	}

	private record AlleleHolder(IAllele allele, boolean required) {
	}
}
