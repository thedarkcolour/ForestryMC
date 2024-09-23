package forestry.api.core;

import java.util.List;

/**
 * Denotes a species that can produce "specialty" products when in a jubilant state,
 * as described by {@link forestry.api.apiculture.IBeeJubilance} or something similar.
 */
public interface ISpecialtyProducer {
	/**
	 * @return The list of possible items that this producer can only produce when in a jubilant state.
	 */
	List<IProduct> getSpecialties();
}
