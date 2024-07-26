package forestry.lepidopterology;

import java.util.Locale;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import forestry.api.client.ForestrySprites;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.filter.FilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.sorting.FilterRegistry;

public enum LepidopterologyFilterRuleType implements IFilterRuleType {
	FLUTTER(ForestrySprites.ANALYZER_FLUTTER) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return true;
		}
	},
	BUTTERFLY(ForestrySprites.ANALYZER_BUTTERFLY) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == ButterflyLifeStage.BUTTERFLY;
		}
	},
	SERUM(ForestrySprites.ANALYZER_SERUM) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == ButterflyLifeStage.SERUM;
		}
	},
	CATERPILLAR(ForestrySprites.ANALYZER_CATERPILLAR) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == ButterflyLifeStage.CATERPILLAR;
		}
	},
	COCOON(ForestrySprites.ANALYZER_COCOON) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == ButterflyLifeStage.COCOON;
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
			FilterRegistry.INSTANCE.registerFilter(rule);
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
	public ResourceLocation getSpeciesTypeId() {
		return ForestrySpeciesTypes.BUTTERFLY;
	}

	@Override
	public String getId() {
		return this.id;
	}
}
