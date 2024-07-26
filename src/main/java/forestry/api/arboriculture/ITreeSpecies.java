package forestry.api.arboriculture;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ISpecies;

public interface ITreeSpecies extends ISpecies<ITree> {
	ITreeGenerator getGenerator();

	ILeafSpriteProvider getLeafSpriteProvider();

	ItemStack getDecorativeLeaves();

	TemperatureType getTemperature();

	HumidityType getHumidity();

	List<BlockState> getVanillaStates();

	int getGermlingColour(TreeLifeStage stage, int renderPass);
}
