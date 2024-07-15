package forestry.sorting;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.client.ForestrySprites;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.filter.IFilterData;
import forestry.api.genetics.filter.IFilterRule;
import forestry.api.genetics.filter.IFilterRuleType;

public enum DefaultFilterRuleType implements IFilterRuleType {
	CLOSED(false, ForestrySprites.ANALYZER_CLOSED) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return false;
		}
	},
	ANYTHING(false, ForestrySprites.ANALYZER_ANYTHING) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return true;
		}
	},
	ITEM(false, ForestrySprites.ANALYZER_ITEM) {
		@Override
		public boolean isValid(ItemStack itemStack, IFilterData data) {
			return !data.isPresent();
		}
	},
	PURE_BREED(ForestrySprites.ANALYZER_PURE_BREED),
	NOCTURNAL(ForestrySprites.ANALYZER_NOCTURNAL),
	PURE_NOCTURNAL(ForestrySprites.ANALYZER_PURE_NOCTURNAL),
	FLYER(ForestrySprites.ANALYZER_FLYER),
	PURE_FLYER(ForestrySprites.ANALYZER_PURE_FLYER),
	CAVE(ForestrySprites.ANALYZER_CAVE),
	PURE_CAVE(ForestrySprites.ANALYZER_PURE_CAVE),
	;

	private final String id;
	private final Set<IFilterRule> logic;
	private final boolean isContainer;
	private final ResourceLocation sprite;

	DefaultFilterRuleType(ResourceLocation sprite) {
		this(true, sprite);
	}

	DefaultFilterRuleType(boolean isContainer, ResourceLocation sprite) {
		this.sprite = sprite;
		this.id = "forestry.default." + name().toLowerCase(Locale.ENGLISH);
		this.logic = new HashSet<>();
		this.isContainer = isContainer;
	}

	public static void init() {
		for (DefaultFilterRuleType rule : values()) {
			AlleleManager.filterRegistry.registerFilter(rule);
		}
	}

	@Override
	public boolean isValid(ItemStack itemStack, IFilterData data) {
		for (IFilterRule logic : logic) {
			if (logic.isValid(itemStack, data)) {
				return true;
			}
		}
		return false;
	}

	// todo this is suspicious
	@Override
	public void addLogic(IFilterRule logic) {
		if (logic == this) {
			throw new IllegalArgumentException();
		}
		this.logic.add(logic);
	}

	@Override
	public boolean isContainer() {
		return this.isContainer;
	}

	@Override
	public ResourceLocation getSprite() {
		return this.sprite;
	}

	@Override
	public String getId() {
		return this.id;
	}
}
