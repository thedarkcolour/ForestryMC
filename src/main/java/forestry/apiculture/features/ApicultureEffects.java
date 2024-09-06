package forestry.apiculture.features;

import forestry.api.modules.ForestryModuleIds;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@FeatureProvider
public class ApicultureEffects {
    private static final IFeatureRegistry REGISTRY= ModFeatureRegistry.get(ForestryModuleIds.APICULTURE);

    private static final DeferredRegister<MobEffect> MOB_EFFECTS=REGISTRY.getRegistry(Registry.MOB_EFFECT_REGISTRY);

    public static final RegistryObject<MobEffect> HAKUNA_MATATA=MOB_EFFECTS.register("hakuna_matata",()->{
        return new ForestryMobEffect(MobEffectCategory.NEUTRAL, 0x069af3)
                .addAttributeModifier(Attributes.FOLLOW_RANGE, "07FB7192-49C7-4f77-BE0B-D182BD391AFD", 0D,AttributeModifier.Operation.MULTIPLY_TOTAL);
    });
    public static final RegistryObject<MobEffect> MATATA=MOB_EFFECTS.register("matata", ()->{
        return new ForestryMobEffect(MobEffectCategory.NEUTRAL, 0x380835);
    });

    public static class ForestryMobEffect extends MobEffect{

        protected ForestryMobEffect(MobEffectCategory pCategory, int pColor) {
            super(pCategory, pColor);
        }

        //we have no ongoing effects
        @Override
        public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
            return false;
        }
    }
}
