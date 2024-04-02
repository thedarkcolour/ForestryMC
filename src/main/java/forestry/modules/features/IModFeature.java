package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * A feature can be used to provide an game object like a block, an item or an fluid. There are different implementations
 * of this class for every game objects.
 * <p>
 * Features are automatically loaded by the modules if you annotate the class that contains the public static final fields.
 *
 * @see IBlockFeature
 * @see IItemFeature
 * @see IFluidFeature
 */
public interface IModFeature {
	String getIdentifier();

	String getModId();

	String getModuleId();

	ResourceKey<? extends Registry<?>> getRegistry();
}
