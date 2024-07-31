package forestry.apiculture.villagers;

import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.apiculture.blocks.BlockTypeApiculture;
import forestry.apiculture.features.ApicultureBlocks;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.items.EnumPropolis;
import forestry.apiculture.items.ItemHoneyComb;
import forestry.core.registration.VillagerTrade;
import forestry.core.registration.VillagerTrade.GiveEmeraldForItem;
import forestry.core.registration.VillagerTrade.GiveItemForEmeralds;
import forestry.core.registration.VillagerTrade.GiveItemForItemAndEmerald;
import forestry.core.registration.VillagerTrade.GiveItemForLogAndEmerald;
import forestry.core.registration.VillagerTrade.GiveItemForTwoItems;
import forestry.core.utils.ForgeUtils;
import forestry.core.utils.SpeciesUtil;
import forestry.modules.features.FeatureProvider;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

@FeatureProvider
public class RegisterVillager {
	public static final ResourceLocation BEEKEEPER = ForestryConstants.forestry("beekeeper");

	private static final DeferredRegister<PoiType> POINTS_OF_INTEREST = DeferredRegister.create(ForgeRegistries.POI_TYPES, ForestryConstants.MOD_ID);
	private static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ForestryConstants.MOD_ID);

	public static final RegistryObject<PoiType> POI_APIARY = POINTS_OF_INTEREST.register("apiary", () -> new PoiType(Set.copyOf(ApicultureBlocks.BASE.get(BlockTypeApiculture.APIARY).block().getStateDefinition().getPossibleStates()), 1, 1));
	@SuppressWarnings("DataFlowIssue")
	public static final RegistryObject<VillagerProfession> PROF_BEEKEEPER = PROFESSIONS.register(BEEKEEPER.getPath(), () -> new VillagerProfession(BEEKEEPER.toString(), e -> e.is(POI_APIARY.getKey()), e -> e.is(POI_APIARY.getKey()), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_FISHERMAN));

	static {
		MinecraftForge.EVENT_BUS.addListener(RegisterVillager::villagerTrades);

		IEventBus modBus = ForgeUtils.modBus();
		POINTS_OF_INTEREST.register(modBus);
		PROFESSIONS.register(modBus);
	}

	private static void villagerTrades(VillagerTradesEvent event) {
		if (event.getType().equals(PROF_BEEKEEPER.get())) {
			Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
			trades.get(1).add(new GiveHoneyCombForItem(ApicultureItems.BEE_COMBS.getItems(), Items.WHEAT, new VillagerTrade.PriceInterval(2, 4), new VillagerTrade.PriceInterval(8, 12), 8, 2, 0F));
			trades.get(1).add(new GiveHoneyCombForItem(ApicultureItems.BEE_COMBS.getItems(), Items.CARROT, new VillagerTrade.PriceInterval(2, 4), new VillagerTrade.PriceInterval(8, 12), 8, 2, 0F));
			trades.get(1).add(new GiveHoneyCombForItem(ApicultureItems.BEE_COMBS.getItems(), Items.POTATO, new VillagerTrade.PriceInterval(2, 4), new VillagerTrade.PriceInterval(8, 12), 8, 2, 0F));

			trades.get(2).add(new GiveItemForEmeralds(ApicultureItems.SMOKER.item(), new VillagerTrade.PriceInterval(1, 1), new VillagerTrade.PriceInterval(1, 4), 8, 6));
			trades.get(2).add(new GiveDroneForItems(ApicultureItems.PROPOLIS.stack(EnumPropolis.NORMAL).getItem(), new VillagerTrade.PriceInterval(2, 4), new VillagerTrade.PriceInterval(1, 1), 8, 6, 0F));

			trades.get(3).add(new GiveEmeraldForItem(ApicultureItems.BEE_PRINCESS.item(), new VillagerTrade.PriceInterval(1, 1), new VillagerTrade.PriceInterval(1, 1), 8, 10));
			trades.get(3).add(new GiveItemForEmeralds(ApicultureItems.FRAME_PROVEN.item(), new VillagerTrade.PriceInterval(1, 2), new VillagerTrade.PriceInterval(1, 6), 8, 10));
			trades.get(3).add(new GiveItemForLogAndEmerald(new VillagerTrade.PriceInterval(32, 64), new VillagerTrade.PriceInterval(16, 32), ApicultureBlocks.BASE.get(BlockTypeApiculture.APIARY).stack().getItem(), new VillagerTrade.PriceInterval(1, 1), 8, 10));

			trades.get(4).add(new GiveItemForItemAndEmerald(ApicultureItems.BEE_PRINCESS.item(), new VillagerTrade.PriceInterval(1, 1), new VillagerTrade.PriceInterval(10, 64), SpeciesUtil.getBeeSpecies(ForestryBeeSpecies.MONASTIC).createStack(BeeLifeStage.DRONE), new VillagerTrade.PriceInterval(1, 1), 8, 15));
			trades.get(4).add(new GiveItemForTwoItems(ApicultureItems.BEE_DRONE.item(), new VillagerTrade.PriceInterval(1, 1), Items.ENDER_EYE, new VillagerTrade.PriceInterval(12, 16), SpeciesUtil.getBeeSpecies(ForestryBeeSpecies.ENDED).createStack(BeeLifeStage.DRONE), new VillagerTrade.PriceInterval(1, 1), 8, 15));
		}
	}

	// todo support modded combs
	public record GiveHoneyCombForItem(List<ItemHoneyComb> itemHoneyCombs,
									   Item buying,
									   VillagerTrade.PriceInterval sellingPriceInfo,
									   VillagerTrade.PriceInterval buyingPriceInfo,
									   int maxUses,
									   int xp,
									   float priceMult) implements VillagerTrades.ItemListing {

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			ItemStack buy = new ItemStack(buying, buyingPriceInfo.getPrice(rand));
			ItemStack sell = new ItemStack(itemHoneyCombs.get(rand.nextInt(itemHoneyCombs.size())), sellingPriceInfo.getPrice(rand));
			return new MerchantOffer(buy, sell, maxUses, xp, priceMult);
		}
	}

	public record GiveDroneForItems(Item buying, VillagerTrade.PriceInterval buyingPriceInfo,
									VillagerTrade.PriceInterval sellingPriceInfo, int maxUses, int xp,
									float priceMult) implements VillagerTrades.ItemListing {

		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			ResourceLocation[] forestryMundane = new ResourceLocation[]{ForestryBeeSpecies.FOREST, ForestryBeeSpecies.MEADOWS, ForestryBeeSpecies.MODEST, ForestryBeeSpecies.WINTRY, ForestryBeeSpecies.TROPICAL, ForestryBeeSpecies.MARSHY};
			ItemStack randomHiveDrone = SpeciesUtil.getBeeSpecies(forestryMundane[rand.nextInt(forestryMundane.length)]).createStack(BeeLifeStage.DRONE);
			randomHiveDrone.setCount(sellingPriceInfo.getPrice(rand));

			return new MerchantOffer(new ItemStack(buying, buyingPriceInfo.getPrice(rand)), randomHiveDrone, maxUses, xp, priceMult);
		}
	}
}
