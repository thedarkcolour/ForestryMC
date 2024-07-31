package forestry.api.arboriculture;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

public interface ITreeSpecies extends ISpecies<ITree>, ITreeGenData {
	@Override
	ITreeSpeciesType getType();

	ITreeGenerator getGenerator();

	ILeafSpriteProvider getLeafSpriteProvider();

	/**
	 * @return The "decorative" form of this leaf block, with no block entity or genome. Used for pick-block and shears.
	 */
	ItemStack getDecorativeLeaves();

	/**
	 * @return The ideal temperature for this tree to grow in.
	 */
	TemperatureType getTemperature();

	/**
	 * @return The ideal humidity for this tree to grow in.
	 */
	HumidityType getHumidity();

	/**
	 * @return A list of mundane forms of this species's leaf blocks. Example: Vanilla leaves, Forestry decorative leaves
	 */
	List<BlockState> getVanillaLeafStates();

	int getGermlingColor(ILifeStage stage, int renderPass);
}
