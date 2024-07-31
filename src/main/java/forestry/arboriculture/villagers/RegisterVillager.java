package forestry.arboriculture.villagers;

import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import forestry.api.ForestryConstants;
import forestry.arboriculture.ForestryWoodType;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.genetics.ILifeStage;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.registration.VillagerTrade;
import forestry.core.utils.SpeciesUtil;

import org.jetbrains.annotations.NotNull;

public class RegisterVillager {
	public static final ResourceLocation ARBORIST = ForestryConstants.forestry("arborist");

	public static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister.create(ForgeRegistries.POI_TYPES, ForestryConstants.MOD_ID);
	public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ForestryConstants.MOD_ID);

	public static final RegistryObject<PoiType> POI_TREE_CHEST = POINTS_OF_INTEREST.register("tree_chest", () -> new PoiType(Set.copyOf(ArboricultureBlocks.TREE_CHEST.block().getStateDefinition().getPossibleStates()), 1, 1));
	public static final RegistryObject<VillagerProfession> PROF_BEEKEEPER = PROFESSIONS.register(ARBORIST.getPath(), () -> new VillagerProfession(ARBORIST.toString(), e -> e.is(POI_TREE_CHEST.getKey()), e -> e.is(POI_TREE_CHEST.getKey()), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FISHERMAN));

	public static void villagerTrades(VillagerTradesEvent event) {
		if (event.getType().equals(PROF_BEEKEEPER.get())) {
			event.getTrades().get(1).add(new GivePlanksForEmeralds(new VillagerTrade.PriceInterval(1, 4), new VillagerTrade.PriceInterval(10, 32), 8, 2, 0F));
			event.getTrades().get(1).add(new GivePollenForEmeralds(new VillagerTrade.PriceInterval(1, 1), new VillagerTrade.PriceInterval(1, 3), TreeLifeStage.SAPLING, 4, 8, 2, 0F));

			event.getTrades().get(2).add(new GivePlanksForEmeralds(new VillagerTrade.PriceInterval(1, 4), new VillagerTrade.PriceInterval(10, 32), 8, 6, 0F));
			event.getTrades().get(2).add(new GivePollenForEmeralds(new VillagerTrade.PriceInterval(2, 3), new VillagerTrade.PriceInterval(1, 1), TreeLifeStage.POLLEN, 6, 8, 6, 0F));
			event.getTrades().get(2).add(new VillagerTrade.GiveItemForEmeralds(ArboricultureItems.GRAFTER_PROVEN.item(), new VillagerTrade.PriceInterval(1, 1), new VillagerTrade.PriceInterval(1, 4), 8, 6));

			event.getTrades().get(3).add(new GiveLogsForEmeralds(new VillagerTrade.PriceInterval(2, 5), new VillagerTrade.PriceInterval(6, 18), 8, 2, 0F));

			event.getTrades().get(3).add(new GiveLogsForEmeralds(new VillagerTrade.PriceInterval(2, 5), new VillagerTrade.PriceInterval(6, 18), 8, 2, 0F));

			event.getTrades().get(4).add(new GivePollenForEmeralds(new VillagerTrade.PriceInterval(5, 20), new VillagerTrade.PriceInterval(1, 1), TreeLifeStage.POLLEN, 10, 8, 15, 0F));
			event.getTrades().get(4).add(new GivePollenForEmeralds(new VillagerTrade.PriceInterval(5, 20), new VillagerTrade.PriceInterval(1, 1), TreeLifeStage.SAPLING, 10, 8, 15, 0F));
		}
	}

	private record GivePlanksForEmeralds(VillagerTrade.PriceInterval emeraldsPriceInfo,
										 VillagerTrade.PriceInterval sellingPriceInfo, int maxUses, int xp,
										 float priceMult) implements VillagerTrades.ItemListing {

		@Override
		public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
			ForestryWoodType woodType = ForestryWoodType.getRandom(rand);
			ItemStack sellStack = TreeManager.woodAccess.getStack(woodType, WoodBlockKind.PLANKS, false);
			sellStack.setCount(sellingPriceInfo.getPrice(rand));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldsPriceInfo.getPrice(rand)), sellStack, maxUses, xp, priceMult);
		}
	}

	private record GiveLogsForEmeralds(VillagerTrade.PriceInterval emeraldsPriceInfo,
									   VillagerTrade.PriceInterval sellingPriceInfo, int maxUses, int xp,
									   float priceMult) implements VillagerTrades.ItemListing {

		@Override
		public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
			ForestryWoodType woodType = ForestryWoodType.getRandom(rand);
			ItemStack sellStack = TreeManager.woodAccess.getStack(woodType, WoodBlockKind.LOG, false);
			sellStack.setCount(sellingPriceInfo.getPrice(rand));

			return new MerchantOffer(new ItemStack(Items.EMERALD, emeraldsPriceInfo.getPrice(rand)), sellStack, maxUses, xp, priceMult);
		}
	}

	private record GivePollenForEmeralds(VillagerTrade.PriceInterval buyingPriceInfo,
										 VillagerTrade.PriceInterval sellingPriceInfo, ILifeStage stage,
										 int maxComplexity, int maxUses, int xp,
										 float priceMult) implements VillagerTrades.ItemListing {

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			// todo this could be optimized to just pick random entries from tree species until one with suitable complexity is found
			// instead of copying the whole thing and then picking once
			List<ITreeSpecies> registeredSpecies = SpeciesUtil.getAllTreeSpecies();
			ArrayList<ITreeSpecies> potentialSpecies = new ArrayList<>();
			for (ITreeSpecies species : registeredSpecies) {
				if (species.getComplexity() <= maxComplexity) {
					potentialSpecies.add(species);
				}
			}

			if (potentialSpecies.isEmpty()) {
				return null;
			}

			ITreeSpecies chosenSpecies = potentialSpecies.get(rand.nextInt(potentialSpecies.size()));
			ItemStack sellStack = chosenSpecies.createStack(stage);
			sellStack.setCount(sellingPriceInfo.getPrice(rand));

			return new MerchantOffer(new ItemStack(Items.EMERALD, buyingPriceInfo.getPrice(rand)), sellStack, maxUses, xp, priceMult);
		}
	}
}
