package forestry.energy.blocks;

import java.util.function.Supplier;

import forestry.core.blocks.IBlockTypeTesr;
import forestry.core.blocks.IMachinePropertiesTesr;
import forestry.core.blocks.MachinePropertiesTesr;
import forestry.core.config.Constants;
import forestry.core.proxy.Proxies;
import forestry.energy.features.EnergyTiles;
import forestry.energy.tiles.EngineBlockEntity;
import forestry.modules.features.FeatureTileType;

public enum EngineBlockType implements IBlockTypeTesr {
    PEAT(createEngineProperties(() -> EnergyTiles.PEAT_ENGINE, "peat", "/engine_copper")),
    BIOGAS(createEngineProperties(() -> EnergyTiles.BIOGAS_ENGINE, "biogas", "/engine_bronze")),
    CLOCKWORK(createEngineProperties(() -> EnergyTiles.CLOCKWORK_ENGINE, "clockwork", "/engine_clock"));

    public static final EngineBlockType[] VALUES = values();

    private final IMachinePropertiesTesr<?> machineProperties;

    EngineBlockType(IMachinePropertiesTesr<?> machineProperties) {
        this.machineProperties = machineProperties;
    }

    private static IMachinePropertiesTesr<?> createEngineProperties(Supplier<FeatureTileType<? extends EngineBlockEntity>> teClass, String name, String textureName) {
        MachinePropertiesTesr<? extends EngineBlockEntity> machinePropertiesEngine = new MachinePropertiesTesr.Builder<>(teClass, name)
                .setParticleTexture(textureName + ".0")
                .setClientTicker(EngineBlockEntity::clientTick)
                .setServerTicker(EngineBlockEntity::serverTick)
                .create();
        Proxies.render.setRenderDefaultEngine(machinePropertiesEngine, Constants.TEXTURE_PATH_BLOCK + textureName + "_");
        return machinePropertiesEngine;
    }

    @Override
    public IMachinePropertiesTesr<?> getMachineProperties() {
        return machineProperties;
    }

    @Override
    public String getSerializedName() {
        return getMachineProperties().getSerializedName();
    }
}
