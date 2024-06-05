package forestry.database.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketIdServer;
import forestry.database.gui.ContainerDatabase;

public record PacketInsertItem(boolean single) implements IForestryPacketServer {
	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(single);
	}

	@Override
	public ResourceLocation id() {
		return PacketIdServer.INSERT_ITEM;
	}

	public static PacketInsertItem decode(FriendlyByteBuf buffer) {
		return new PacketInsertItem(buffer.readBoolean());
	}

	// todo ensure usages of containerMenu/inventoryMenu are correct
	public static void handle(PacketInsertItem msg, ServerPlayer player) {
		boolean single = msg.single();

		if (player.containerMenu instanceof ContainerDatabase databaseMenu) {
			IItemHandler itemHandler = databaseMenu.getItemHandler();

			ItemStack playerStack = player.inventoryMenu.getCarried();
			ItemStack itemStack = playerStack.copy();

			if (single) {
				itemStack.setCount(1);
			}

			ItemStack remaining = ItemHandlerHelper.insertItemStacked(itemHandler, itemStack, false);
			if (single && remaining.isEmpty()) {
				playerStack.shrink(1);
				if (playerStack.isEmpty()) {
					player.inventoryMenu.setCarried(ItemStack.EMPTY);
				}
			} else {
				player.inventoryMenu.setCarried(remaining);
			}

			databaseMenu.sendContainerToListeners();
		}
	}
}
