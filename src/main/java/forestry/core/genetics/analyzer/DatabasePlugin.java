package forestry.core.genetics.analyzer;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.gatgets.IDatabasePlugin;
import forestry.api.genetics.gatgets.IDatabaseTab;

public abstract class DatabasePlugin<I extends IIndividual> implements IDatabasePlugin {
	private final IDatabaseTab<I> activeTab;
	private final IDatabaseTab<I> inactiveTab;
	private final IDatabaseTab<?> productsTab;
	private final IDatabaseTab<?> mutationsTab;

	public DatabasePlugin(IDatabaseTab<I> activeTab, IDatabaseTab<I> inactiveTab, IDatabaseTab<?> productsTab, IDatabaseTab<?> mutationsTab) {
		this.activeTab = activeTab;
		this.inactiveTab = inactiveTab;
		this.productsTab = productsTab;
		this.mutationsTab = mutationsTab;
	}

	@Override
	public IDatabaseTab<?>[] getTabs() {
		return new IDatabaseTab[]{
				activeTab,
				inactiveTab,
				productsTab,
				mutationsTab
		};
	}
}
