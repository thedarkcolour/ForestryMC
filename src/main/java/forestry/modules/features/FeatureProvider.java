package forestry.modules.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.eventbus.api.IEventBus;

import forestry.modules.ModuleUtil;

/**
 * Annotated classes are loaded during mod construction after by {@link ModuleUtil#loadFeatureProviders}.
 * At this point, {@link forestry.api.modules.IForestryModule#registerEvents(IEventBus)} has already been called.
 * Note that annotated classes are NOT INSTANTIATED, only classloaded.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureProvider {
}
