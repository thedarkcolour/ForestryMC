package forestry.arboriculture;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.client.ForestrySprites;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.filter.IFilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;

public enum ArboricultureFilterRuleType implements IFilterRuleType {
	TREE(ForestrySprites.ANALYZER_TREE) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent();
		}
	},
	SAPLING(ForestrySprites.ANALYZER_SAPLING) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == TreeLifeStage.SAPLING;
		}
	},
	POLLEN(ForestrySprites.ANALYZER_POLLEN) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return data.isPresent() && data.type() == TreeLifeStage.POLLEN;
		}
	};

	private final String id;
	private final ResourceLocation sprite;

	ArboricultureFilterRuleType(ResourceLocation sprite) {
		this.sprite = sprite;
		this.id = "forestry.arboriculture." + name().toLowerCase(Locale.ENGLISH);
	}

	public static void init() {
		for (ArboricultureFilterRuleType rule : values()) {
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
	public String getSpeciesTypeId() {
		return TreeManager.treeRoot.id();
	}

	@Override
	public String getId() {
		return id;
	}
}
