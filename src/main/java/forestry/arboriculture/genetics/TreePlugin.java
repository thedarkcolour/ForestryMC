package forestry.arboriculture.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import genetics.api.GeneticHelper;
import genetics.api.organism.IIndividualCapability;

import forestry.api.arboriculture.genetics.IAlleleTreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.config.Config;
import forestry.core.features.CoreItems;
import forestry.core.genetics.ItemGE;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.core.genetics.analyzer.ProductsTab;
import forestry.core.items.ItemFruit;
import forestry.core.utils.SpeciesUtil;

//TODO: Add support for the alyzer
@OnlyIn(Dist.CLIENT)
public class TreePlugin extends DatabasePlugin<ITree> {
	public static final TreePlugin INSTANCE = new TreePlugin();
	protected final Map<String, ItemStack> iconStacks = new HashMap<>();

	private TreePlugin() {
		super(new TreeDatabaseTab(DatabaseMode.ACTIVE),
			new TreeDatabaseTab(DatabaseMode.INACTIVE),
			new ProductsTab(() -> CoreItems.FRUITS.stack(ItemFruit.EnumFruit.CHERRY, 1)),
			new MutationsTab(ArboricultureItems.GRAFTER::stack));
		ArrayList<ItemStack> stacks = new ArrayList<>();
		ItemGE.addCreativeItems(TreeLifeStage.SAPLING, stacks, false, SpeciesUtil.TREE_TYPE.get());
		for (ItemStack treeStack : stacks) {
			IIndividualCapability<?> organism = GeneticHelper.getOrganism(treeStack);
			if (organism.isEmpty()) {
				continue;
			}
			IAlleleTreeSpecies species = organism.getAllele(TreeChromosomes.SPECIES, true);
			iconStacks.put(species.getId().toString(), treeStack);
		}
	}

	@Override
	public Map<ResourceLocation, ItemStack> getIndividualStacks() {
		return iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("treealyzer");
	}
}
