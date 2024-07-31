package forestry.core.registration;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import net.minecraftforge.items.ItemHandlerHelper;

public class VillagerTrade {
	public record GiveItemForEmeralds(Item sellingItem, PriceInterval sellingAmounts, PriceInterval emeraldAmounts,
									  int maxUses, int xp) implements VillagerTrades.ItemListing {
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldAmounts.getPrice(rand)), new ItemStack(this.sellingItem, this.sellingAmounts.getPrice(rand)), maxUses, xp, 0.05f);
		}
	}

	public record GiveEmeraldForItem(Item buyingItem, PriceInterval buyingAmounts, PriceInterval emeraldAmounts,
									 int maxUses, int xp) implements VillagerTrades.ItemListing {
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			return new MerchantOffer(new ItemStack(this.buyingItem, this.buyingAmounts.getPrice(rand)), new ItemStack(Items.EMERALD, this.emeraldAmounts.getPrice(rand)), maxUses, xp, 0.05f);
		}
	}

	public record GiveItemForItemAndEmerald(Item buyingItem, PriceInterval buyAmounts, PriceInterval emeralsAmounts,
											ItemStack sellingItem, PriceInterval sellingAmounts, int maxUses,
											int xp) implements VillagerTrades.ItemListing {
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			ItemStack buy1 = new ItemStack(this.buyingItem, this.buyAmounts.getPrice(rand));
			ItemStack buy2 = new ItemStack(Items.EMERALD, this.emeralsAmounts.getPrice(rand));
			ItemStack sell = ItemHandlerHelper.copyStackWithSize(this.sellingItem, this.sellingAmounts.getPrice(rand));
			return new MerchantOffer(buy1, buy2, sell, maxUses, xp, 0.05f);
		}
	}

	public record GiveItemForLogAndEmerald(PriceInterval buyAmounts, PriceInterval emeraldAmounts, Item sellingItem,
										   PriceInterval sellingAmounts, int maxUses,
										   int xp) implements VillagerTrades.ItemListing {
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			// should this give forestry logs too?
			Item[] logsBlock = new Item[]{
					Items.ACACIA_LOG,
					Items.BIRCH_LOG,
					Items.DARK_OAK_LOG,
					Items.JUNGLE_LOG,
					Items.OAK_LOG,
					Items.SPRUCE_LOG,
					Items.MANGROVE_LOG,
					// todo 1.20
					//Items.CHERRY_LOG,
			};

			return new MerchantOffer(new ItemStack(logsBlock[rand.nextInt(logsBlock.length)], this.buyAmounts.getPrice(rand)), new ItemStack(Items.EMERALD, this.emeraldAmounts.getPrice(rand)), new ItemStack(this.sellingItem, this.sellingAmounts.getPrice(rand)), maxUses, xp, 0.05f);
		}
	}

	public record GiveItemForTwoItems(Item buyingItem, PriceInterval buyAmounts, Item buyingItem2,
									  PriceInterval buyAmounts2, ItemStack sellingItem, PriceInterval sellingAmounts,
									  int maxUses, int xp) implements VillagerTrades.ItemListing {
		@Override
		public MerchantOffer getOffer(Entity trader, RandomSource rand) {
			ItemStack buy1 = new ItemStack(this.buyingItem, this.buyAmounts.getPrice(rand));
			ItemStack buy2 = new ItemStack(this.buyingItem2, this.buyAmounts2.getPrice(rand));
			ItemStack sell = ItemHandlerHelper.copyStackWithSize(this.sellingItem, this.sellingAmounts.getPrice(rand));
			return new MerchantOffer(buy1, buy2, sell, maxUses, xp, 0.05f);
		}
	}

	public record PriceInterval(int min, int max) {
		public int getPrice(RandomSource rand) {
			return min >= max ? min : min + rand.nextInt(max - min + 1);
		}
	}
}
