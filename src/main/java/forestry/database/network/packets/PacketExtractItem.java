package forestry.database.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketIdServer;
import forestry.database.gui.ContainerDatabase;

public record PacketExtractItem(int invIndex, byte flags) implements IForestryPacketServer {
	public static final int HALF = 1;
	public static final int SHIFT = 2;
	public static final int CLONE = 4;

	// todo rewrite bad code
	public static void handle(PacketExtractItem msg, ServerPlayer player) {
		int invIndex = msg.invIndex();
		byte flags = msg.flags();

		// todo replace flag comparisons with != 0
		if (player.inventoryMenu.getCarried().isEmpty()) {
			if (player.containerMenu instanceof ContainerDatabase databaseMenu) {
				IItemHandler itemHandler = databaseMenu.getItemHandler();
				// Get the item on that position
				ItemStack itemStack = itemHandler.extractItem(invIndex, 64, true);
				// Test if we there is an item on this position
				if (!itemStack.isEmpty()) {
					//Get the max count of this stack
					int maxItemCount = itemStack.getItem().getMaxStackSize(itemStack.copy());
					//Get the count of the stack
					int itemCount = itemStack.getCount();

					if ((flags & CLONE) == CLONE) {
						//Clone the item with the maximal count
						ItemStack extracted = itemStack.copy();
						extracted.setCount(maxItemCount);
						player.inventoryMenu.setCarried(extracted);

						databaseMenu.sendContainerToListeners();
						return;
					}

					int count = 64;
					if ((flags & HALF) == HALF && itemCount > 1) {
						count = itemCount / 2;
					}

					count = Math.min(count, maxItemCount);

					// Simulate an item extraction
					ItemStack extracted = itemHandler.extractItem(invIndex, count, true);

					if (!extracted.isEmpty()) {
						if ((flags & SHIFT) == SHIFT) {
							IItemHandler playerInv = player.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
							// Test if the player has enough space
							ItemStack remaining = ItemHandlerHelper.insertItem(playerInv, extracted, true);
							if (remaining.isEmpty()) {
								// Extract the item
								extracted = itemHandler.extractItem(invIndex, count, false);

								// Give the item to the player into the first valid slot
								ItemHandlerHelper.insertItem(playerInv, extracted, false);
							}
						} else {
							// Extract the item
							extracted = itemHandler.extractItem(invIndex, count, false);

							player.inventoryMenu.setCarried(extracted);

							player.inventoryMenu.broadcastChanges();
						}

						databaseMenu.sendContainerToListeners();
					}
				}
			}
		}
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarInt(invIndex);
		buffer.writeByte(flags);
	}

	@Override
	public ResourceLocation id() {
		return PacketIdServer.EXTRACT_ITEM;
	}

	public static PacketExtractItem decode(FriendlyByteBuf buffer) {
		return new PacketExtractItem(buffer.readVarInt(), buffer.readByte());
	}
}
