package forestry.apiculture.genetics.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class PotionBeeEffectExclusive extends PotionBeeEffect{

    private final MobEffect exclude;

    public PotionBeeEffectExclusive(boolean dominant, MobEffect potion, int duration, int throttle, float chance, MobEffect exclude) {
        super(dominant, potion, duration, throttle, chance);
        this.exclude=exclude;
    }

    @Override
    public boolean secondaryEntityCheck(LivingEntity entity) {
        return !entity.hasEffect(exclude);
    }
}
