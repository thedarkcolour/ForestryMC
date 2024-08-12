package forestry.api.genetics.filter;

import net.minecraft.resources.ResourceLocation;

/**
 * A filter rule type is a filter option that can be set in the Genetic Filter block.
 * <p>
 * Some filter rule types can be marked as "containers" which means that other {@link IFilterRule}
 * can be added to their filtering logic. An example would the the default "cave" filter type, which is supposed to filter
 * by organisms that live in caves. It doesn't filter anything by default, but the apiculture module adds a filter logic
 * that filters bees depending on if they have the {@link forestry.api.genetics.alleles.BeeChromosomes#CAVE_DWELLING} trait.
 * Modded species types could also take advantage of the cave filter type by adding another IFilterRule to the cave rule type.
 */
public interface IFilterRuleType extends IFilterRule {
	default void addLogic(IFilterRule logic) {
	}

	/**
	 * @return True if logic can be added to this type through {@link #addLogic}.
	 */
	default boolean isContainer() {
		return false;
	}

	/**
	 * @return A unique ID for the rule.
	 */
	String getId();

	/**
	 * @return The resource location for the sprite used to select this rule type in the Genetic Filter screen.
	 */
	ResourceLocation getSprite();
}
