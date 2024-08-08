package forestry.lepidopterology;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.ForestryConstants;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.IGenome;
import forestry.api.core.Product;
import forestry.api.genetics.alleles.AllelePair;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.api.plugin.IButterflySpeciesBuilder;
import forestry.core.genetics.GenericRatings;
import forestry.core.genetics.Species;
import forestry.lepidopterology.genetics.Butterfly;

public class ButterflySpecies extends Species<IButterflySpeciesType, IButterfly> implements IButterflySpecies {
	private final TemperatureType temperature;
	private final HumidityType humidity;
	private final boolean nocturnal;
	private final boolean moth;
	private final float rarity;
	@Nullable
	private final TagKey<Biome> spawnBiomes;
	private final float flightDistance;
	private final int serumColor;
	private final List<Product> products;
	private final List<Product> caterpillarProducts;

	public ButterflySpecies(ResourceLocation id, IButterflySpeciesType speciesType, IGenome defaultGenome, IButterflySpeciesBuilder builder) {
		super(id, speciesType, defaultGenome, builder);

		this.temperature = builder.getTemperature();
		this.humidity = builder.getHumidity();
		this.nocturnal = builder.isNocturnal();
		this.moth = builder.isMoth();
		this.rarity = builder.getRarity();
		this.spawnBiomes = builder.getSpawnBiomes();
		this.flightDistance = builder.getFlightDistance();
		this.serumColor = builder.getSerumColor();
		this.products = builder.buildProducts();
		this.caterpillarProducts = builder.buildCaterpillarProducts();
	}

	@Override
	public IButterfly createIndividual(IGenome genome) {
		return new Butterfly(genome);
	}

	@Override
	public void addTooltip(IButterfly butterfly, List<Component> tooltip) {
		ToolTip toolTip = new ToolTip();

		// No info 4 u!
		if (!butterfly.isAnalyzed()) {
			toolTip.singleLine().text("<").translated("for.gui.unknown").text(">").style(ChatFormatting.GRAY).create();
			return;
		}

		IGenome genome = butterfly.getGenome();

		// You analyzed it? Juicy tooltip coming up!
		AllelePair<IValueAllele<IButterflySpecies>> speciesPair = genome.getAllelePair(ButterflyChromosomes.SPECIES);
		IButterflySpecies primary = speciesPair.active().value();
		IButterflySpecies secondary = speciesPair.inactive().value();
		if (!speciesPair.isSameAlleles()) {
			toolTip.translated("for.butterflies.hybrid", primary.getDisplayName(), secondary.getDisplayName()).style(ChatFormatting.BLUE);
		}

		if (butterfly.getMate() != null) {
			toolTip.add(Component.translatable("for.gui.fecundated").withStyle(ChatFormatting.RED));
		}
		toolTip.add(genome.getActiveName(ButterflyChromosomes.SIZE).withStyle(ChatFormatting.YELLOW));
		toolTip.add(genome.getActiveName(ButterflyChromosomes.SPEED).withStyle(ChatFormatting.DARK_GREEN));
		toolTip.add(genome.getActiveName(ButterflyChromosomes.LIFESPAN).append(" ").append(Component.translatable("for.gui.life").withStyle(ChatFormatting.GRAY)));

		MutableComponent tempTolerance = genome.getActiveName(ButterflyChromosomes.TEMPERATURE_TOLERANCE);
		MutableComponent humidTolerance = genome.getActiveName(ButterflyChromosomes.HUMIDITY_TOLERANCE);

		toolTip.singleLine().text("T: ").add(ClimateHelper.toDisplay(primary.getTemperature())).text(" / ").add(tempTolerance).style(ChatFormatting.GREEN).create();
		toolTip.singleLine().text("H: ").add(ClimateHelper.toDisplay(primary.getHumidity())).text(" / ").add(humidTolerance).style(ChatFormatting.GREEN).create();

		toolTip.add(GenericRatings.rateActivityTime(genome.getActiveValue(ButterflyChromosomes.NEVER_SLEEPS), primary.isNocturnal()).withStyle(ChatFormatting.RED));

		if (genome.getActiveValue(ButterflyChromosomes.TOLERATES_RAIN)) {
			toolTip.translated("for.gui.flyer.tooltip").style(ChatFormatting.WHITE);
		}

		tooltip.addAll(toolTip.getLines());
	}

	@Override
	public TemperatureType getTemperature() {
		return this.temperature;
	}

	@Override
	public HumidityType getHumidity() {
		return this.humidity;
	}

	@Nullable
	@Override
	public TagKey<Biome> getSpawnBiomes() {
		return this.spawnBiomes;
	}

	@Override
	public float getRarity() {
		return this.rarity;
	}

	@Override
	public float getFlightDistance() {
		return this.flightDistance;
	}

	@Override
	public boolean isMoth() {
		return this.moth;
	}

	@Override
	public boolean isNocturnal() {
		return this.nocturnal;
	}

	@Override
	public List<Product> getButterflyLoot() {
		return this.products;
	}

	@Override
	public List<Product> getCaterpillarProducts() {
		return this.caterpillarProducts;
	}

	@Override
	public int getSerumColor() {
		return this.serumColor;
	}
}
