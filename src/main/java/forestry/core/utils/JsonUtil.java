package forestry.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.GsonHelper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import net.minecraftforge.common.util.JsonUtils;

import forestry.Forestry;
import forestry.api.core.Product;

public class JsonUtil {
	public static ItemStack deserializeItemStack(JsonObject object, ItemStack fallback) {
		return deserializeItemStack(object, fallback, false);
	}

	public static ItemStack deserializeItemStack(JsonObject object, ItemStack fallback, boolean logError) {
		if (!object.has("item")) {
			if (logError) {
				Forestry.LOGGER.error("Unsupported icon type, currently only items are supported (add 'item' key)");
			}
			return fallback;
		}
		try {
			Item item = GsonHelper.getAsItem(object, "item");
			int count = GsonHelper.getAsInt(object, "count", 1);
			ItemStack stack = new ItemStack(item, count);
			stack.setTag(JsonUtils.readNBT(object, "nbt"));
			return stack;
		} catch (JsonSyntaxException e) {
			if (logError) {
				Forestry.LOGGER.trace("Filed to parse item.", e);
			}
			return fallback;
		}
	}

	public static <T> T deserialize(Codec<T> codec, JsonElement json) {
		return codec.decode(JsonOps.INSTANCE, json).result().get().getFirst();
	}

	public static <T> JsonElement serialize(Codec<T> codec, T object) {
		return codec.encodeStart(JsonOps.INSTANCE, object).result().get();
	}
}
