package forestry.api.core;

import java.util.List;

/**
 * Denotes a species that can produce products.
 */
public interface IProductProducer {
	/**
	 * @return The list of possible items that can be produced by this producer.
	 */
	List<IProduct> getProducts();
}
