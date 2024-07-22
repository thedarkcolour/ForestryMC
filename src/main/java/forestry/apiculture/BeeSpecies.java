package forestry.apiculture;

import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeJubilance;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.apiculture.genetics.Bee;
import forestry.core.genetics.Species;

public class BeeSpecies extends Species<IBeeSpeciesType, IBee> implements IBeeSpecies {
	private final boolean nocturnal;
	private final List<Product> products;
	private final List<Product> specialties;
	private final TemperatureType temperature;
	private final HumidityType humidity;
	private final IBeeJubilance jubilance;

	public BeeSpecies(ResourceLocation id, IBeeSpeciesType speciesType, IGenome defaultGenome, IBeeSpeciesBuilder builder) {
		super(id, speciesType, defaultGenome, builder);

		this.nocturnal = builder.isNocturnal();
		this.products = builder.buildProducts();
		this.specialties = builder.buildSpecialties();
		this.temperature = builder.getTemperature();
		this.humidity = builder.getHumidity();
		this.jubilance = builder.getJubilance();
	}

	@Override
	public boolean isNocturnal() {
		return this.nocturnal;
	}

	@Override
	public List<Product> getProducts() {
		return this.products;
	}

	@Override
	public List<Product> getSpecialties() {
		return this.specialties;
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
	public boolean isJubilant(IGenome genome, IBeeHousing housing) {
		return this.jubilance.isJubilant(this, genome, housing);
	}

	@Override
	public IBee createIndividual(Map<IChromosome<?>, IAllele> alleles) {
		return new Bee(this.defaultGenome.copyWith(alleles));
	}
}
