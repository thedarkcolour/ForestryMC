package forestry.arboriculture.genetics;

import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.config.Config;
import forestry.core.features.CoreItems;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.core.genetics.analyzer.ProductsTab;
import forestry.core.items.ItemFruit;
import forestry.core.utils.GeneticsUtil;
import forestry.core.utils.SpeciesUtil;

public class TreePlugin extends DatabasePlugin<ITree> {
	public static final TreePlugin INSTANCE = new TreePlugin();

	protected final Map<ISpecies<?>, ItemStack> iconStacks;

	private TreePlugin() {
		super(new TreeDatabaseTab(DatabaseMode.ACTIVE),
				new TreeDatabaseTab(DatabaseMode.INACTIVE),
				new ProductsTab(() -> CoreItems.FRUITS.stack(ItemFruit.EnumFruit.CHERRY, 1)),
				new MutationsTab(ArboricultureItems.GRAFTER::stack));
		this.iconStacks = GeneticsUtil.getIconStacks(TreeLifeStage.SAPLING, SpeciesUtil.TREE_TYPE.get());
	}

	@Override
	public Map<ISpecies<?>, ItemStack> getIndividualStacks() {
		return iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("treealyzer");
	}
}
