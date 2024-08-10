package forestry.core.genetics.analyzer;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.genetics.IBee;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.IProduct;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.DatabaseElement;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.gui.elements.ItemElement;
import forestry.core.gui.elements.layouts.FlexLayout;
import forestry.core.gui.elements.layouts.LayoutHelper;

public class ProductsTab<I extends IIndividual> extends DatabaseTab<I> {
	public ProductsTab(Supplier<ItemStack> stackSupplier) {
		super("products", stackSupplier);
	}

	@Override
	public void createElements(DatabaseElement container, I individual, ILifeStage stage, ItemStack stack) {
		LayoutHelper groupHelper = container.layoutHelper((x, y) -> GuiElementFactory.horizontal(18, 2, FlexLayout.LEFT_MARGIN), 90, 0);
		List<IProduct> products = getProducts(individual);
		if (!products.isEmpty()) {
			container.translated("for.gui.beealyzer.produce").setAlign(Alignment.TOP_CENTER);
			products.forEach(product -> groupHelper.add(new ItemElement(0, 0, product.createStack())));
			groupHelper.finish();
		}

		List<IProduct> specialties = getSpecialties(individual);
		if (specialties.isEmpty()) {
			return;
		}

		container.translated("for.gui.beealyzer.specialty").setAlign(Alignment.TOP_CENTER);
		specialties.forEach(specialty -> groupHelper.add(new ItemElement(0, 0, specialty.createStack())));
		groupHelper.finish();
	}

	private List<IProduct> getSpecialties(IIndividual individual) {
		if (individual instanceof IBee bee) {
			return bee.getSpecies().getSpecialties();
		} else if (individual instanceof ITree tree) {
			return tree.getSpecialties();
		}
		return List.of();
	}

	private List<IProduct> getProducts(IIndividual individual) {
		if (individual instanceof IBee bee) {
			return bee.getSpecies().getProducts();
		} else if (individual instanceof ITree tree) {
			return tree.getProducts();
		}
		return List.of();
	}
}
