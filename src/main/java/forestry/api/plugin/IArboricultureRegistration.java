package forestry.api.plugin;

import java.awt.Color;

import net.minecraft.resources.ResourceLocation;

import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.LeafType;

public interface IArboricultureRegistration {
	ITreeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, LeafType leafType, Color primary, Color secondary, IWoodType woodType);
}
