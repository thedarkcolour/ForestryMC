package forestry.apiculture;

import java.util.Locale;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.client.ForestrySprites;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.filter.FilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.sorting.FilterRegistry;

public enum ApicultureFilterRuleType implements IFilterRuleType {
	BEE(ForestrySprites.ANALYZER_BEE) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return true;
		}
	},
	DRONE(ForestrySprites.ANALYZER_DRONE) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == BeeLifeStage.DRONE;
		}
	},
	PRINCESS(ForestrySprites.ANALYZER_PRINCESS) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == BeeLifeStage.PRINCESS;
		}
	},
	QUEEN(ForestrySprites.ANALYZER_QUEEN) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == BeeLifeStage.QUEEN;
		}
	};

	private final String id;
	private final ResourceLocation sprite;

	ApicultureFilterRuleType(ResourceLocation sprite) {
		this.id = "forestry.apiculture." + name().toLowerCase(Locale.ENGLISH);
		this.sprite = sprite;
	}

	public static void init() {
		for (ApicultureFilterRuleType rule : values()) {
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
		return ForestrySpeciesTypes.BEE;
	}

	@Override
	public String getId() {
		return this.id;
	}
}
