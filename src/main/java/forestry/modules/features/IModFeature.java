package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Fancy wrapper around a registry object.
 */
public interface IModFeature {
	/**
	 * @return The registry name of this object without the mod namespace.
	 */
	String getName();

	/**
	 * @return The ID of the module responsible for adding this feature.
	 */
	ResourceLocation getModuleId();

	/**
	 * @return The ID of the mod which this feature is added by.
	 */
	default String getModId() {
		return this.getModuleId().getNamespace();
	}

	/**
	 * @return The primary registry in which this feature resides.
	 */
	ResourceKey<? extends Registry<?>> getRegistry();
}
