package forestry.api.genetics;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IKaryotype;

public interface ISpeciesType<S extends ISpecies<?>> {
	/**
	 * @return The unique ID of this species type.
	 */
	ResourceLocation id();

	@Nullable
	default ILifeStage getLifeStage(ItemStack stack) {
		return getTypes().getType(stack);
	}

	ItemStack createStack(IAllele allele, ILifeStage type);

	boolean isMember(ItemStack stack);

	/**
	 * @return The karyotype for all members of this species type.
	 */
	IKaryotype getKaryotype();

	<T extends ISpeciesType<?>> T cast();
}
