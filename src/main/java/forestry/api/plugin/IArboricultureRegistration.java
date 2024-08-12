package forestry.api.plugin;

import java.awt.Color;

import net.minecraft.resources.ResourceLocation;

import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITreeEffect;

public interface IArboricultureRegistration {
	/**
	 * Register a new tree species.
	 *
	 * @param id        The unique ID of this species. The path must start with "tree_" due to the species allele reusing this ID.
	 * @param genus     The genus of this species. See {@link forestry.api.genetics.ForestryTaxa} for examples.
	 * @param species   The species name of this species, used for scientific naming.
	 * @param dominant  Whether the allele for this species is dominant or recessive.
	 * @param primary   The primary color of this tree species. Used for pollen colors and tree leaf tinting.
	 * @param woodType  The wood type of this tree species. TODO why is this used for leaves placement?
	 */
	ITreeSpeciesBuilder registerSpecies(ResourceLocation id, String genus, String species, boolean dominant, Color primary, IWoodType woodType);

	/**
	 * Register a new type of fruit.
	 * @param id    The unique ID of this fruit.
	 * @param fruit
	 */
	void registerFruit(ResourceLocation id, IFruit fruit);

	void registerTreeEffect(ResourceLocation id, ITreeEffect effect);
}
