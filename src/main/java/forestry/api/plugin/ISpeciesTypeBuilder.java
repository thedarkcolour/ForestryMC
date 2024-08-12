package forestry.api.plugin;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.item.Item;

import forestry.api.genetics.ILifeStage;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;

public interface ISpeciesTypeBuilder {
	/**
	 * Defines the default karyotype for members of this species type. Although the default genome can be customized
	 * on a per-species basis, all members of the same species type have the same set of chromosomes.
	 * Multiple calls to this method will be chained and can override results of previous calls.
	 */
	ISpeciesTypeBuilder setKaryotype(Consumer<IKaryotypeBuilder> karyotype);

	/**
	 * Adds possible life stages to this species type.
	 * Make sure to also call {@link #setDefaultStage}.
	 */
	ISpeciesTypeBuilder addStages(ILifeStage... stages);

	/**
	 * Allows adding new Escritoire research materials or removing the default ones.
	 */
	ISpeciesTypeBuilder addResearchMaterials(Consumer<Reference2FloatMap<Item>> materials);

	/**
	 * Sets the default life stage of this species type.
	 *
	 * @param stage The default stage for use in menus.
	 */
	ISpeciesTypeBuilder setDefaultStage(ILifeStage stage);

	ILifeStage getDefaultStage();

	List<ILifeStage> getStages();

	void buildResearchMaterials(Reference2FloatMap<Item> materialMap);
}
