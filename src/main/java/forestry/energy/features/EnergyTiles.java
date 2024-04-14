package forestry.energy.features;

import forestry.energy.ModuleEnergy;
import forestry.energy.blocks.EngineBlockType;
import forestry.energy.tiles.BiogasEngineBlockEntity;
import forestry.energy.tiles.ClockworkEngineBlockEntity;
import forestry.energy.tiles.PeatEngineBlockEntity;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.FeatureTileType;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class EnergyTiles {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleEnergy.class);

	public static final FeatureTileType<BiogasEngineBlockEntity> BIOGAS_ENGINE = REGISTRY.tile(BiogasEngineBlockEntity::new, "biogas_engine", EnergyBlocks.ENGINES.get(EngineBlockType.BIOGAS)::collect);
	public static final FeatureTileType<ClockworkEngineBlockEntity> CLOCKWORK_ENGINE = REGISTRY.tile(ClockworkEngineBlockEntity::new, "clockwork_engine", EnergyBlocks.ENGINES.get(EngineBlockType.CLOCKWORK)::collect);
	public static final FeatureTileType<PeatEngineBlockEntity> PEAT_ENGINE = REGISTRY.tile(PeatEngineBlockEntity::new, "peat_engine", EnergyBlocks.ENGINES.get(EngineBlockType.PEAT)::collect);
}
