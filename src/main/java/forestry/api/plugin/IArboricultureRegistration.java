package forestry.api.plugin;

import java.awt.Color;

import net.minecraft.resources.ResourceLocation;

import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.LeafType;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITreeEffect;

public interface IArboricultureRegistration {
	ITreeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, LeafType leafType, Color primary, Color secondary, IWoodType woodType);

	void registerFruit(ResourceLocation id, IFruit fruit);

	void registerTreeEffect(ResourceLocation id, ITreeEffect effect);
}
