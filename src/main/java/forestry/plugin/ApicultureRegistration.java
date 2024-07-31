package forestry.plugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.api.plugin.IHiveBuilder;
import forestry.apiculture.VillageHive;

import deleteme.Todos;

public class ApicultureRegistration extends SpeciesRegistration<IBeeSpeciesBuilder, BeeSpeciesBuilder> implements IApicultureRegistration {
	private final HashMap<ResourceLocation, IHiveBuilder> hives = new HashMap<>();
	private final ArrayList<VillageHive> commonVillageHives = new ArrayList<>();
	private final ArrayList<VillageHive> rareVillageHives = new ArrayList<>();

	@Override
	protected BeeSpeciesBuilder createSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		return new BeeSpeciesBuilder(id, genus, species, mutations);
	}

	@Override
	public IBeeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, Color outline) {
		return register(id, genus, species)
				.setDominant(dominant)
				.setOutline(outline);
	}

	@Override
	public void addVillageBee(ResourceLocation speciesId, boolean rare, Map<IChromosome<?>, IAllele> alleles) {
		(rare ? this.rareVillageHives : this.commonVillageHives).add(new VillageHive(speciesId, alleles));
	}

	@Override
	public void registerFlowerType(ResourceLocation id, IFlowerType type) {

	}

	@Override
	public void registerBeeEffect(ResourceLocation id, IBeeEffect effect) {

	}

	@Override
	public IHiveBuilder registerHive(ResourceLocation id, IHiveDefinition definition) {
		return this.hives.computeIfAbsent(id, key -> new HiveBuilder(definition));
	}
}
