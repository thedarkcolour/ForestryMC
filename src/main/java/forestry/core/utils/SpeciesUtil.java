package forestry.core.utils;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.List;
import java.util.Objects;

import net.minecraft.resources.ResourceLocation;

import forestry.api.IForestryApi;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IGeneticManager;

public class SpeciesUtil {
	public static final Supplier<IBeeSpeciesType> BEE_TYPE = Suppliers.memoize(() -> IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.BEE, IBeeSpeciesType.class));
	public static final Supplier<ITreeSpeciesType> TREE_TYPE = Suppliers.memoize(() -> IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.TREE, ITreeSpeciesType.class));

	public static ITreeSpecies getTreeSpecies(ResourceLocation id) {
		return TREE_TYPE.get().getSpeciesById(id);
	}

	public static List<ITreeSpecies> getAllTreeSpecies() {
		return TREE_TYPE.get().getSpecies();
	}

	public static IBeeSpecies getBeeSpecies(ResourceLocation id) {
		return BEE_TYPE.get().getSpeciesById(id);
	}
}
