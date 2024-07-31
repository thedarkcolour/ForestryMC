package forestry.core.climate;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.ForestryTags;
import forestry.api.IForestryApi;
import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateListener;
import forestry.api.climate.IClimateProvider;
import forestry.api.climate.IClimatised;
import forestry.api.core.HumidityType;
import forestry.api.core.ILocatable;
import forestry.api.core.TemperatureType;
import forestry.core.network.packets.PacketClimateListenerUpdate;
import forestry.core.network.packets.PacketClimateListenerUpdateRequest;
import forestry.core.render.ParticleRender;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.TickHelper;

import deleteme.Todos;

public class ClimateListener implements IClimateListener {
	public static final int SERVER_UPDATE = 250;

	private final Object locationProvider;
	@Nullable
	protected Level level;
	@Nullable
	protected BlockPos pos;
	@Nullable
	private ClimateState cachedState;
	@Nullable
	private ClimateState cachedClientState;
	@OnlyIn(Dist.CLIENT)
	private TickHelper tickHelper;
	@OnlyIn(Dist.CLIENT)
	protected boolean needsClimateUpdate;
	//The total world time at the moment the cached state has been updated
	private long cacheTime = 0;
	private long lastUpdate = 0;

	public ClimateListener(Object locationProvider) {
		this.locationProvider = locationProvider;

		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.tickHelper = new TickHelper(0);
			this.needsClimateUpdate = true;
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void updateClientSide(boolean spawnParticles) {
		if (spawnParticles) {
			tickHelper.onTick();
			if (cachedState != null && tickHelper.updateOnInterval(20)) {
				Level level = getWorldObj();
				BlockPos pos = getCoordinates();
				ParticleRender.addTransformParticles(level, pos, level.random);
			}
		}
		if (needsClimateUpdate) {
			NetworkUtil.sendToServer(new PacketClimateListenerUpdateRequest(getCoordinates()));
			needsClimateUpdate = false;
		}
	}

	private void updateState(boolean syncToClient) {
		WorldClimateHolder climateHolder = WorldClimateHolder.get((ServerLevel) getWorldObj());
		long totalTime = getWorldObj().getGameTime();
		if (cacheTime + SERVER_UPDATE > totalTime && climateHolder.getLastUpdate(getCoordinates()) == lastUpdate) {
			return;
		}
		lastUpdate = climateHolder.getLastUpdate(getCoordinates());
		cachedState = climateHolder.getAdjustedState(getCoordinates());
		cacheTime = totalTime;
		if (syncToClient) {
			syncToClient();
		}
	}

	@Nullable
	private IClimatised getState() {
		return getState(true);
	}

	@Nullable
	private IClimatised getState(boolean update) {
		return getState(update, true);
	}

	@Nullable
	private ClimateState getState(boolean update, boolean syncToClient) {
		Level worldObj = getWorldObj();
		if (!worldObj.isClientSide && update) {
			updateState(syncToClient);
		}
		return this.cachedState;
	}

	private IClimateProvider getDefaultProvider() {
		IClimateProvider provider;
		if (locationProvider instanceof IClimateProvider climateProvider) {
			provider = climateProvider;
		} else {
			provider = IForestryApi.INSTANCE.getClimateManager().getDefaultClimate(getWorldObj(), getCoordinates());
		}
		return provider;
	}

	@Override
	public Holder<Biome> getBiome() {
		IClimateProvider provider = getDefaultProvider();
		return provider.getBiome();
	}

	@Override
	public TemperatureType temperature() {
		if (getBiome().is(ForestryTags.Biomes.NETHER_CATEGORY)) {
			return TemperatureType.HELLISH;
		}

		return IForestryApi.INSTANCE.getClimateManager().getTemperature(getBiome());
	}

	@Override
	public HumidityType humidity() {
		return IForestryApi.INSTANCE.getClimateManager().getHumidity(getBiome());
	}

	@Override
	public ClimateState getClimateState() {
		return new ClimateState(temperature(), humidity());
	}

	@Override
	public void setClimateState(ClimateState climateState) {
		this.cachedState = climateState;
	}

	@Override
	public void syncToClient() {
		if (!cachedState.equals(cachedClientState)) {
			Level level = getWorldObj();

			if (!level.isClientSide) {
				BlockPos coordinates = getCoordinates();
				NetworkUtil.sendNetworkPacket(new PacketClimateListenerUpdate(getCoordinates(), cachedState), coordinates, level);
			}
			cachedClientState = cachedState;
		}
	}

	@Override
	public void syncToClient(ServerPlayer player) {
		Level level = getWorldObj();
		if (!level.isClientSide) {
			ClimateState climateState = getState(true, false);
			NetworkUtil.sendToPlayer(new PacketClimateListenerUpdate(getCoordinates(), climateState), player);
		}
	}

	@Override
	public BlockPos getCoordinates() {
		if (this.pos == null) {
			initLocation();
		}
		return this.pos;
	}

	@Nullable
	@Override
	public Level getWorldObj() {
		if (this.level == null) {
			initLocation();
		}
		return this.level;
	}

	@Override
	public void markLocatableDirty() {
		this.level = null;
		this.pos = null;
		Level worldObj = getWorldObj();
		if (!worldObj.isClientSide) {
			updateState(true);
		}
	}

	private void initLocation() {
		if ((this.locationProvider instanceof ILocatable provider)) {
			this.level = provider.getWorldObj();
			this.pos = provider.getCoordinates();
		} else if ((this.locationProvider instanceof BlockEntity provider)) {
			this.level = provider.getLevel();
			this.pos = provider.getBlockPos();
		} else {
			throw new IllegalStateException("no / incompatible location provider");
		}
	}
}
