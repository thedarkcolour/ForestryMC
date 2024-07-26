package forestry.plugin;

import java.awt.Color;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.api.plugin.IHiveBuilder;

import deleteme.Todos;

public class ApicultureRegistration extends SpeciesRegistration<IBeeSpeciesBuilder, BeeSpeciesBuilder> implements IApicultureRegistration {
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
	public void addVillageBee(ResourceLocation id, boolean rare) {

	}

	@Override
	public void registerFlowerType(ResourceLocation id, IFlowerType type) {

	}

	@Override
	public void registerBeeEffect(ResourceLocation id, IBeeEffect effect) {

	}

	@Override
	public IHiveBuilder registerHive(ResourceLocation id, IHiveDefinition definition) {
		throw Todos.unimplemented();
	}

	@Override
	public void addHiveDrop(ResourceLocation id, IHiveDrop drop) {

	}
}
