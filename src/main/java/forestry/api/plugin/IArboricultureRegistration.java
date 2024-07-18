package forestry.api.plugin;

import java.awt.Color;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.LeafType;

public interface IArboricultureRegistration {
	ITreeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, LeafType leafType, Color primary, Color secondary, IWoodType woodType, Function<ITreeGenData, Feature<?>> treeFeature);
}
