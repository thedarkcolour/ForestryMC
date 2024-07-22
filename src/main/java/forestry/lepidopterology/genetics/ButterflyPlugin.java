package forestry.lepidopterology.genetics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import genetics.api.GeneticHelper;
import genetics.api.organism.IIndividualCapability;

import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.config.Config;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.lepidopterology.features.LepidopterologyItems;

@OnlyIn(Dist.CLIENT)
public class ButterflyPlugin extends DatabasePlugin<IButterfly> {
	public static final ButterflyPlugin INSTANCE = new ButterflyPlugin();

	protected final Map<String, ItemStack> iconStacks = new HashMap<>();

	private ButterflyPlugin() {
		super(new ButterflyDatabaseTab(DatabaseMode.ACTIVE),
			new ButterflyDatabaseTab(DatabaseMode.INACTIVE),
			new ButterflyProductsTab(),
			new MutationsTab(() -> ButterflyDefinition.Glasswing.getMemberStack(ButterflyLifeStage.COCOON)));
		NonNullList<ItemStack> butterflyList = NonNullList.create();
		LepidopterologyItems.BUTTERFLY_GE.item().addCreativeItems(butterflyList, false);
		for (ItemStack butterflyStack : butterflyList) {
			IIndividualCapability<?> organism = GeneticHelper.getOrganism(butterflyStack);
			if (organism.isEmpty()) {
				continue;
			}
			IButterflySpecies species = organism.getAllele(ButterflyChromosomes.SPECIES, true);
			iconStacks.put(species.getId().toString(), butterflyStack);
		}
	}

	@Override
	public Map<String, ItemStack> getIndividualStacks() {
		return iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("flutterlyzer");
	}
}
