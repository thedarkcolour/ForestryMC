package forestry.arboriculture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.Product;

public class Fruit extends DummyFruit {
	private final List<Product> products;
	protected final int ripeningPeriod;

	public Fruit(boolean dominant, int ripeningPeriod, List<Product> products) {
		super(dominant);

		this.products = List.copyOf(products);
		this.ripeningPeriod = ripeningPeriod;
	}

	@Override
	public List<ItemStack> getFruits(@Nullable IGenome genome, Level level, BlockPos pos, int ripeningTime) {
		if (ripeningTime >= this.ripeningPeriod) {
			ArrayList<ItemStack> stacks = new ArrayList<>(this.products.size());

			for (Product product : this.products) {
				if (product.chance() == 1.0f || product.chance() < level.random.nextFloat()) {
					stacks.add(product.createStack());
				}
			}

			return stacks;
		}

		return List.of();
	}

	@Override
	public List<Product> getProducts() {
		return this.products;
	}
}
