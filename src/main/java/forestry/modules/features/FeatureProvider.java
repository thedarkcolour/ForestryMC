package forestry.modules.features;

import forestry.modules.ForestryPluginUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated classes are loaded during mod construction by {@link ForestryPluginUtil#loadFeatureProviders}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureProvider {
}
