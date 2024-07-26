package forestry.arboriculture.genetics;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.IForestryApi;
import forestry.api.genetics.ClimateHelper;
import forestry.api.genetics.IGenome;

import forestry.api.arboriculture.IGrowthProvider;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.core.ToleranceType;
import forestry.api.genetics.alleles.TreeChromosomes;

public class ClimateGrowthProvider implements IGrowthProvider {
	@Nullable
	private TemperatureType temperature;
	@Nullable
	private HumidityType humidity;
	private final ToleranceType temperatureTolerance;
	private final ToleranceType humidityTolerance;

	public ClimateGrowthProvider(TemperatureType temperature, ToleranceType temperatureTolerance, HumidityType humidity, ToleranceType humidityTolerance) {
		this.temperature = temperature;
		this.temperatureTolerance = temperatureTolerance;
		this.humidity = humidity;
		this.humidityTolerance = humidityTolerance;
	}

	public ClimateGrowthProvider() {
		this.temperature = null;
		this.temperatureTolerance = ToleranceType.NONE;
		this.humidity = null;
		this.humidityTolerance = ToleranceType.NONE;
	}

	@Override
	public boolean canSpawn(ITree tree, Level world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isBiomeValid(ITree tree, Holder.Reference<Biome> biome) {
		TemperatureType biomeTemperature = IForestryApi.INSTANCE.getClimateManager().getTemperature(biome);
		HumidityType biomeHumidity = IForestryApi.INSTANCE.getClimateManager().getHumidity(biome);
		IGenome genome = tree.getGenome();
		if (temperature == null) {
			temperature = genome.getActiveValue(TreeChromosomes.SPECIES).getTemperature();
		}
		if (humidity == null) {
			humidity = genome.getActiveValue(TreeChromosomes.SPECIES).getHumidity();
		}
		return ClimateHelper.isWithinLimits(biomeTemperature, biomeHumidity, temperature, temperatureTolerance, humidity, humidityTolerance);
	}

}
