package forestry.plugin;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.plugin.IGenomeBuilder;
import forestry.api.plugin.IMutationsRegistration;
import forestry.api.plugin.ISpeciesBuilder;

public class SpeciesBuilder<B extends ISpeciesBuilder<B>> implements ISpeciesBuilder<B> {
	protected final ResourceLocation id;
	protected final String genus;
	protected final String species;
	protected final MutationsRegistration mutations;

	protected TemperatureType temperature = TemperatureType.NORMAL;
	protected HumidityType humidity = HumidityType.NORMAL;
	protected boolean dominant;
	protected boolean glint;
	protected int complexity;
	protected boolean secret;

	public SpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		this.id = id;
		this.genus = genus;
		this.species = species;
		this.mutations = mutations;
	}

	@Override
	public B setDominant(boolean dominant) {
		this.dominant = dominant;
		return self();
	}

	@Override
	public B setGenome(Consumer<IGenomeBuilder> karyotype) {
		return self();
	}

	@Override
	public B addMutations(Consumer<IMutationsRegistration> mutations) {
		mutations.accept(this.mutations);
		return self();
	}

	@Override
	public B setGlint(boolean glint) {
		this.glint = glint;
		return self();
	}

	@Override
	public B setTemperature(TemperatureType temperature) {
		this.temperature = temperature;
		return self();
	}

	@Override
	public B setHumidity(HumidityType humidity) {
		this.humidity = humidity;
		return self();
	}

	@Override
	public B setComplexity(int complexity) {
		this.complexity = complexity;
		return self();
	}

	@Override
	public B setSecret(boolean secret) {
		this.secret = secret;
		return self();
	}

	@Override
	public TemperatureType getTemperature() {
		return this.temperature;
	}

	@Override
	public HumidityType getHumidity() {
		return this.humidity;
	}

	@Override
	public int getComplexity() {
		return this.complexity;
	}

	@Override
	public String getGenus() {
		return this.genus;
	}

	@Override
	public String getSpecies() {
		return this.species;
	}

	@SuppressWarnings("unchecked")
	protected B self() {
		return (B) this;
	}
}
