package forestry.lepidopterology;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import genetics.api.individual.IIndividual;

import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.filter.IFilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.sorting.DefaultFilterRuleType;

public enum LepidopterologyFilterRule implements IFilterRule {
	PURE_BREED(DefaultFilterRuleType.PURE_BREED) {
		@Override
		protected boolean isValid(IButterfly butterfly) {
			return butterfly.isPureBred(ButterflyChromosomes.SPECIES);
		}
	},
	NOCTURNAL(DefaultFilterRuleType.NOCTURNAL) {
		@Override
		protected boolean isValid(IButterfly butterfly) {
			return butterfly.getGenome().getActiveValue(ButterflyChromosomes.NEVER_SLEEPS);
		}
	},
	PURE_NOCTURNAL(DefaultFilterRuleType.PURE_NOCTURNAL) {
		@Override
		protected boolean isValid(IButterfly butterfly) {
			return butterfly.getGenome().getActiveValue(ButterflyChromosomes.NEVER_SLEEPS) && butterfly.isPureBred(ButterflyChromosomes.NEVER_SLEEPS);
		}
	},
	FLYER(DefaultFilterRuleType.FLYER) {
		@Override
		protected boolean isValid(IButterfly butterfly) {
			return butterfly.getGenome().getActiveValue(ButterflyChromosomes.TOLERATES_RAIN);
		}
	},
	PURE_FLYER(DefaultFilterRuleType.PURE_FLYER) {
		@Override
		protected boolean isValid(IButterfly butterfly) {
			return butterfly.getGenome().getActiveValue(ButterflyChromosomes.TOLERATES_RAIN) && butterfly.isPureBred(ButterflyChromosomes.TOLERATES_RAIN);
		}
	};

	LepidopterologyFilterRule(IFilterRuleType rule) {
		rule.addLogic(this);
	}

	public static void init() {
	}

	@Override
	public boolean isValid(ItemStack itemStack, IFilterData data) {
		if (!data.isPresent()) {
			return false;
		}
		IIndividual individual = data.individual();
		if (!(individual instanceof IButterfly)) {
			return false;
		}
		return isValid((IButterfly) individual);
	}

	protected boolean isValid(IButterfly butterfly) {
		return false;
	}

	@Override
	public ResourceLocation getSpeciesTypeId() {
		return ButterflyManager.butterflyRoot.id();
	}

}
