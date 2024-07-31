package forestry.arboriculture;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.arboriculture.genetics.TreeLifeStage;
import forestry.api.client.ForestrySprites;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.filter.FilterData;
import forestry.api.genetics.filter.IFilterRuleType;

public enum ArboricultureFilterRuleType implements IFilterRuleType {
	TREE(ForestrySprites.ANALYZER_TREE) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return true;
		}
	},
	SAPLING(ForestrySprites.ANALYZER_SAPLING) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == TreeLifeStage.SAPLING;
		}
	},
	POLLEN(ForestrySprites.ANALYZER_POLLEN) {
		@Override
		public boolean isValid(ItemStack stack, FilterData data) {
			return data.stage() == TreeLifeStage.POLLEN;
		}
	};

	private final String id;
	private final ResourceLocation sprite;

	ArboricultureFilterRuleType(ResourceLocation sprite) {
		this.sprite = sprite;
		this.id = "forestry.arboriculture." + name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public ResourceLocation getSprite() {
		return this.sprite;
	}

	@Override
	public ResourceLocation getSpeciesTypeId() {
		return ForestrySpeciesTypes.TREE;
	}

	@Override
	public String getId() {
		return this.id;
	}
}
