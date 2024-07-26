package forestry.lepidopterology.genetics;

import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.api.lepidopterology.ForestryButterflySpecies;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.config.Config;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.core.utils.SpeciesUtil;

public class ButterflyPlugin extends DatabasePlugin<IButterfly> {
	public static final ButterflyPlugin INSTANCE = new ButterflyPlugin();

	private ButterflyPlugin() {
		super(new ButterflyDatabaseTab(DatabaseMode.ACTIVE),
				new ButterflyDatabaseTab(DatabaseMode.INACTIVE),
				new ButterflyProductsTab(),
				new MutationsTab<>(() -> SpeciesUtil.getButterflySpecies(ForestryButterflySpecies.GLASSWING).createStack(ButterflyLifeStage.COCOON)));
	}

	@Override
	public Map<ResourceLocation, ItemStack> getIndividualStacks() {
		return ButterflyAlyzerPlugin.INSTANCE.getIconStacks();
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("flutterlyzer");
	}
}
