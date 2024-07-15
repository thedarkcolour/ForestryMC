package forestry.core.climate;

import javax.annotation.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import forestry.api.climate.IClimateListener;
import forestry.api.climate.IClimatised;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;

public enum FakeClimateListener implements IClimateListener {
	INSTANCE;

	@Override
	public IClimatised getClimateState() {
		return AbsentClimateState.INSTANCE;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void updateClientSide(boolean spawnParticles) {
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void setClimateState(IClimatised climateState) {
	}

	@Override
	public Holder<Biome> getBiome() {
		return ForgeRegistries.BIOMES.getDelegateOrThrow(Biomes.PLAINS).value();
	}

	@Override
	public TemperatureType temperature() {
		return TemperatureType.NORMAL;
	}

	@Override
	public HumidityType humidity() {
		return HumidityType.NORMAL;
	}

	@Override
	public float getExactTemperature() {
		return 0.0F;
	}

	@Override
	public float getExactHumidity() {
		return 0.0F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void syncToClient() {
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void syncToClient(ServerPlayer player) {
	}

	@Override
	public BlockPos getCoordinates() {
		return BlockPos.ZERO;
	}

	@Nullable
	@Override
	public Level getWorldObj() {
		return null;
	}

	@Override
	public void markLocatableDirty() {
	}
}
