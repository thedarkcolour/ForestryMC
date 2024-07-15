package forestry.api.genetics.filter;

import net.minecraft.resources.ResourceLocation;

public interface IFilterRuleType extends IFilterRule {
	void addLogic(IFilterRule logic);

	/**
	 * @return True if  a other logic can be added to this type.
	 */
	boolean isContainer();

	/**
	 * @return A unique identifier for the rule.
	 */
	String getId();

	ResourceLocation getSprite();
}
