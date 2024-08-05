package forestry.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.api.plugin.IHiveBuilder;
import forestry.apiculture.VillageHive;
import forestry.apiculture.hives.HiveManager;

public class ApicultureRegistration extends SpeciesRegistration<IBeeSpeciesBuilder, IBeeSpecies, BeeSpeciesBuilder> implements IApicultureRegistration {
	private final ModifiableRegistrar<ResourceLocation, IHiveBuilder, HiveBuilder> hives = new ModifiableRegistrar<>(IHiveBuilder.class);
	private final Registrar<ResourceLocation, IFlowerType, IFlowerType> flowerTypes = new Registrar<>(IFlowerType.class);
	private final Registrar<ResourceLocation, IBeeEffect, IBeeEffect> beeEffects = new Registrar<>(IBeeEffect.class);
	private final ArrayList<VillageHive> commonVillageHives = new ArrayList<>();
	private final ArrayList<VillageHive> rareVillageHives = new ArrayList<>();

	public ApicultureRegistration(ISpeciesType<IBeeSpecies, ?> type) {
		super(type);
	}

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
		this.flowerTypes.create(id, type);
	}

	public ImmutableMap<ResourceLocation, IFlowerType> getFlowerTypes() {
		return this.flowerTypes.build();
	}

	@Override
	public void registerBeeEffect(ResourceLocation id, IBeeEffect effect) {
		this.beeEffects.create(id, effect);
	}

	public ImmutableMap<ResourceLocation, IBeeEffect> getBeeEffects() {
		return this.beeEffects.build();
	}

	@Override
	public IHiveBuilder registerHive(ResourceLocation id, IHiveDefinition definition) {
		return this.hives.create(id, new HiveBuilder(definition));
	}

	@Override
	public void modifyHive(ResourceLocation id, Consumer<IHiveBuilder> builder) {
		this.hives.modify(id, builder);
	}

	public HiveManager buildHiveManager() {
		return new HiveManager(this.hives.build(HiveBuilder::build), ImmutableList.copyOf(this.commonVillageHives), ImmutableList.copyOf(this.rareVillageHives));
	}
}
