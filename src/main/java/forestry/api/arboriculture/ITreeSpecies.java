package forestry.api.arboriculture;

import net.minecraft.world.item.ItemStack;

import forestry.api.arboriculture.genetics.ITree;
import forestry.api.genetics.ISpecies;

public interface ITreeSpecies extends ISpecies<ITree> {
	ITreeGenerator getGenerator();

	ILeafSpriteProvider getLeafSpriteProvider();

	ItemStack getDecorativeLeaves();
}
