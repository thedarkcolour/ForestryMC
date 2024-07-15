package forestry.apiculture;

import java.util.Locale;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.client.ForestrySprites;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.filter.IFilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;

public enum ApicultureFilterRuleType implements IFilterRuleType {
	BEE(ForestrySprites.ANALYZER_BEE) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent();
		}
	},
	DRONE(ForestrySprites.ANALYZER_DRONE) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == BeeLifeStage.DRONE;
		}
	},
	PRINCESS(ForestrySprites.ANALYZER_PRINCESS) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == BeeLifeStage.PRINCESS;
		}
	},
	QUEEN(ForestrySprites.ANALYZER_QUEEN) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == BeeLifeStage.QUEEN;
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
		return BeeManager.beeRoot.id();
	}

	@Override
	public String getId() {
		return id;
	}
}
