package forestry.lepidopterology.genetics;

import java.util.List;

import forestry.api.core.Product;
import forestry.api.lepidopterology.IButterflyCocoon;

public class DefaultCocoon implements IButterflyCocoon {
	private final String texture;
	private final List<Product> products;

	public DefaultCocoon(String texture, List<Product> products) {
		this.texture = texture;
		this.products = products;
	}

	@Override
	public List<Product> getProducts() {
		return this.products;
	}

	@Override
	public boolean isDominant() {
		return false;
	}

	protected static String getAgeKey(int age) {
		if (age == 0) {
			return "early";
		} else if (age == 1) {
			return "middle";
		} else {
			return "late";
		}
	}

}
