package forestry.apiculture.genetics;

import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.EnumHoneyComb;
import forestry.core.config.Config;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.core.genetics.analyzer.ProductsTab;
import forestry.core.utils.GeneticsUtil;
import forestry.core.utils.SpeciesUtil;

public class BeePlugin extends DatabasePlugin<IBee> {
	public static final BeePlugin INSTANCE = new BeePlugin();

	protected final Map<ISpecies<?>, ItemStack> iconStacks;

	private BeePlugin() {
		super(new BeeDatabaseTab(DatabaseMode.ACTIVE),
				new BeeDatabaseTab(DatabaseMode.INACTIVE),
				new ProductsTab(() -> ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.HONEY)),
				new MutationsTab<>(ApicultureItems.FRAME_IMPREGNATED::stack));

		this.iconStacks = GeneticsUtil.getIconStacks(BeeLifeStage.DRONE, SpeciesUtil.BEE_TYPE.get());
	}

	@Override
	public Map<ISpecies<?>, ItemStack> getIndividualStacks() {
		return this.iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("beealyzer");
	}
}
