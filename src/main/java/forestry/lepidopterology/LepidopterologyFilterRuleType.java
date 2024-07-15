package forestry.lepidopterology;

import java.util.Locale;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import forestry.api.client.ForestrySprites;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.filter.IFilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;

public enum LepidopterologyFilterRuleType implements IFilterRuleType {
	FLUTTER(ForestrySprites.ANALYZER_FLUTTER) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent();
		}
	},
	BUTTERFLY(ForestrySprites.ANALYZER_BUTTERFLY) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == ButterflyLifeStage.BUTTERFLY;
		}
	},
	SERUM(ForestrySprites.ANALYZER_SERUM) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == ButterflyLifeStage.SERUM;
		}
	},
	CATERPILLAR(ForestrySprites.ANALYZER_CATERPILLAR) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == ButterflyLifeStage.CATERPILLAR;
		}
	},
	COCOON(ForestrySprites.ANALYZER_COCOON) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == ButterflyLifeStage.COCOON;
		}
	};

	private final String id;
	private final ResourceLocation sprite;

	LepidopterologyFilterRuleType(ResourceLocation sprite) {
		this.sprite = sprite;
		this.id = "forestry.lepidopterology." + name().toLowerCase(Locale.ENGLISH);
	}

	public static void init() {
		for (LepidopterologyFilterRuleType rule : values()) {
			AlleleManager.filterRegistry.registerFilter(rule);
		}
	}

	@Override
	public void addLogic(IFilterRule logic) {
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public ResourceLocation getSprite() {
		return this.sprite;
	}

	@Override
	public String getRootUID() {
		return ButterflyManager.butterflyRoot.id();
	}

	@Override
	public String getId() {
		return this.id;
	}
}
