/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology;

import java.util.List;

import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.IRegistryAlleleValue;

public interface IButterflyCocoon extends IRegistryAlleleValue {
	List<Product> getProducts();
}
