package forestry.core.genetics;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

public class AlleleUtil {
	@Nullable
	public static <S extends ISpecies<?>> S getSpecies(ISpeciesType<S> speciesType, CompoundTag nbt, String key) {
		String idString = nbt.getString(key);
		if (idString.isEmpty()) {
			return null;
		}
		ResourceLocation id = new ResourceLocation(idString);
		return speciesType.getSpeciesById(id);
	}
}
