package forestry.core.genetics.root;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;

import genetics.api.individual.IIndividual;

public interface IResearchPlugin<S extends ISpecies<?>> {
	float getResearchSuitability(S species, ItemStack itemstack);

	default List<ItemStack> getResearchBounty(S species, Level world, GameProfile researcher, IIndividual individual, int bountyLevel) {
		return List.of();
	}
}
