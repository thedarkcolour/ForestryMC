package forestry.apiculture.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.gatgets.DatabaseMode;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.EnumHoneyComb;
import forestry.core.config.Config;
import forestry.core.genetics.analyzer.DatabasePlugin;
import forestry.core.genetics.analyzer.MutationsTab;
import forestry.core.genetics.analyzer.ProductsTab;

public class BeePlugin extends DatabasePlugin<IBee> {
	public static final BeePlugin INSTANCE = new BeePlugin();

	protected final Map<ResourceLocation, ItemStack> iconStacks = new HashMap<>();

	private BeePlugin() {
		super(new BeeDatabaseTab(DatabaseMode.ACTIVE),
				new BeeDatabaseTab(DatabaseMode.INACTIVE),
				new ProductsTab(() -> ApicultureItems.BEE_COMBS.stack(EnumHoneyComb.HONEY)),
				new MutationsTab<>(ApicultureItems.FRAME_IMPREGNATED::stack));
		ArrayList<ItemStack> beeList = new ArrayList<>();
		ApicultureItems.BEE_DRONE.item().addCreativeItems(beeList, false);
		for (ItemStack stack : beeList) {
			IIndividualHandler.ifPresent(stack, individual -> {
				IBeeSpecies species = individual.getGenome().getActiveSpecies();
				iconStacks.put(species.id(), stack);
			});
		}
	}

	@Override
	public Map<ResourceLocation, ItemStack> getIndividualStacks() {
		return iconStacks;
	}

	@Override
	public List<String> getHints() {
		return Config.hints.get("beealyzer");
	}
}
