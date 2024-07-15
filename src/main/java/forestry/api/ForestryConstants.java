package forestry.api;

import net.minecraft.resources.ResourceLocation;

public class ForestryConstants {
	public static final String MOD_ID = "forestry";

	/**
	 * @return A new resource location under the Forestry namespace. In most cases, mods should use their own namespace instead.
	 */
	public static ResourceLocation forestry(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
