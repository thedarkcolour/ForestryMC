package forestry.apiculture.genetics.alleles;

import java.util.Arrays;
import java.util.List;

import net.minecraft.world.effect.MobEffects;

import forestry.api.apiculture.genetics.IBeeEffect;

import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.alleles.BeeChromosomes;

public class AlleleEffects {
	public static final IBeeEffect effectNone;
	public static final IBeeEffect effectAggressive;
	public static final IBeeEffect effectHeroic;
	public static final IBeeEffect effectBeatific;
	public static final IBeeEffect effectMiasmic;
	public static final IBeeEffect effectMisanthrope;
	public static final IBeeEffect effectGlacial;
	public static final IBeeEffect effectRadioactive;
	public static final IBeeEffect effectCreeper;
	public static final IBeeEffect effectIgnition;
	public static final IBeeEffect effectExploration;
	public static final IBeeEffect effectFestiveEaster;
	public static final IBeeEffect effectSnowing;
	public static final IBeeEffect effectDrunkard;
	public static final IBeeEffect effectReanimation;
	public static final IBeeEffect effectResurrection;
	public static final IBeeEffect effectRepulsion;
	public static final IBeeEffect effectFertile;
	public static final IBeeEffect effectMycophilic;
	private static final List<IBeeEffect> beeEffects;

	static {
		beeEffects = Arrays.asList(
			effectNone = new NoneBeeEffect("none", true),
			effectAggressive = new AggressiveBeeEffect(),
			effectHeroic = new HeroicBeeEffect(),
			effectBeatific = new PotionBeeEffect("beatific", false, MobEffects.REGENERATION, 100),
			effectMiasmic = new PotionBeeEffect("miasmic", false, MobEffects.POISON, 600, 100, 0.1f),
			effectMisanthrope = new MisanthropeBeeEffect(),
			effectGlacial = new GlacialBeeEffect(),
			effectRadioactive = new RadioactiveBeeEffect(),
			effectCreeper = new Creeper(),
			effectIgnition = new IgnitionBeeEffect(),
			effectExploration = new ExplorationBeeEffect(),
			effectFestiveEaster = new NoneBeeEffect("festive_easter", true),
			effectSnowing = new SnowingBeeEffect(),
			effectDrunkard = new PotionBeeEffect("drunkard", false, MobEffects.CONFUSION, 100),
			effectReanimation = new ResurrectionBeeEffect("reanimation", ResurrectionBeeEffect.getReanimationList()),
			effectResurrection = new ResurrectionBeeEffect("resurrection", ResurrectionBeeEffect.getResurrectionList()),
			effectRepulsion = new RepulsionBeeEffect(),
			effectFertile = new FertileBeeEffect(),
			effectMycophilic = new FungificationBeeEffect()
		);
	}

	public static void registerAlleles(IAlleleRegistry registry) {
		for (IBeeEffect beeEffect : beeEffects) {
			registry.registerAllele(beeEffect, BeeChromosomes.EFFECT);
		}
	}
}
