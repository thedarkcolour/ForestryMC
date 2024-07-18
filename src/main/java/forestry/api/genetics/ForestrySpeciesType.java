package forestry.api.genetics;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;

/**
 * The three types of species registered by base Forestry.
 */
public class ForestrySpeciesType {
	/**
	 * @see forestry.api.apiculture.genetics.IBeeSpeciesType
	 */
	public static final ResourceLocation BEE = ForestryConstants.forestry("bee_species");
	/**
	 * @see forestry.api.arboriculture.genetics.ITreeSpeciesType
	 */
	public static final ResourceLocation TREE = ForestryConstants.forestry("tree_species");
	/**
	 * @see forestry.api.lepidopterology.genetics.IButterflySpeciesType
	 */
	public static final ResourceLocation BUTTERFLY = ForestryConstants.forestry("butterfly_species");
}
