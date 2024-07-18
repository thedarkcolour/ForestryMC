package forestry.plugin;

import java.awt.Color;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IFlowerType;
import forestry.api.apiculture.genetics.IBeeEffect;
import forestry.api.apiculture.hives.IHiveDefinition;
import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.plugin.IApicultureRegistration;
import forestry.api.plugin.IBeeSpeciesBuilder;

public class ApicultureRegistration extends SpeciesRegistration<IBeeSpeciesBuilder, BeeSpeciesBuilder> implements IApicultureRegistration {
	@Override
	protected BeeSpeciesBuilder createSpeciesBuilder(ResourceLocation id, String genus, String species) {
		return new BeeSpeciesBuilder(id, genus, species);
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
	public void registerFlowerType(IFlowerType type) {

	}

	@Override
	public void registerBeeEffect(IBeeEffect effect) {

	}

	@Override
	public void registerHive(ResourceLocation id, IHiveDefinition definition) {

	}

	@Override
	public void addHiveDrop(ResourceLocation id, IHiveDrop drop) {

	}
}
