package forestry.core.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import forestry.api.modules.IForestryPacketClient;
import forestry.core.network.PacketIdClient;
import forestry.core.recipes.RecipeManagers;

public class RecipeCachePacket implements IForestryPacketClient {
	@Override
	public ResourceLocation id() {
		return PacketIdClient.RECIPE_CACHE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
	}

	public static RecipeCachePacket decode(FriendlyByteBuf buffer) {
		return new RecipeCachePacket();
	}

	public static void handle(RecipeCachePacket msg, Player player) {
		RecipeManagers.invalidateCaches();
	}
}
