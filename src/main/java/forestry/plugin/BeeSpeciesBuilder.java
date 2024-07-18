package forestry.plugin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeJubilance;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.Product;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.api.plugin.IGenomeBuilder;
import forestry.api.plugin.IKaryotypeBuilder;
import forestry.api.plugin.IMutationsRegistration;

public class BeeSpeciesBuilder implements IBeeSpeciesBuilder {
	private final ResourceLocation id;
	private final String genus;
	private final String species;
	private final IMutationsRegistration mutations;
	private final ArrayList<Product> products = new ArrayList<>();
	private final ArrayList<Product> specialties = new ArrayList<>();
	private int bodyColor = 0xffdc16;
	private int stripesColor = 0;
	private int outlineColor = -1;
	private boolean secret;
	private boolean glint;

	public BeeSpeciesBuilder(ResourceLocation id, String genus, String species) {
		this.id = id;
		this.genus = genus;
		this.species = species;
		this.mutations = new MutationsRegistration();
	}

	@Override
	public IBeeSpeciesBuilder addProduct(Supplier<ItemStack> stack, float chance) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder addSpecialty(Supplier<ItemStack> stack, float chance) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setTemperature(TemperatureType temperature) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setHumidity(HumidityType humidity) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setGenome(Consumer<IGenomeBuilder> genome) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setBody(Color color) {
		this.bodyColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setStripes(Color color) {
		this.stripesColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setOutline(Color color) {
		this.outlineColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setDominant(boolean dominant) {
		return this;
	}

	@Override
	public IBeeSpeciesBuilder addMutations(Consumer<IMutationsRegistration> mutations) {
		mutations.accept(this.mutations);
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setGlint(boolean glint) {
		this.glint = glint;
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setSecret(boolean secret) {
		this.secret = secret;
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setJubilance(IBeeJubilance jubilance) {
		return this;
	}

	public BeeSpecies build() {
		return new BeeSpecies();
	}
}
