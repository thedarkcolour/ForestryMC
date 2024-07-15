package forestry.api.client;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;

/**
 * All sprites loaded by forestry for use in the {@link ITextureManager}.
 */
public class ForestrySprites {
	/**
	 * Used for menu rendering with {@link com.mojang.blaze3d.systems.RenderSystem#setShaderTexture(int, ResourceLocation)}
	 */
	public static final ResourceLocation TEXTURE_ATLAS = new ResourceLocation(ForestryConstants.MOD_ID, "textures/atlas/gui.png");
	
	public static final ResourceLocation HABITAT_DESERT = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/desert");
	public static final ResourceLocation HABITAT_END = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/end");
	public static final ResourceLocation HABITAT_FOREST = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/forest");
	public static final ResourceLocation HABITAT_HILLS = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/hills");
	public static final ResourceLocation HABITAT_JUNGLE = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/jungle");
	public static final ResourceLocation HABITAT_MUSHROOM = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/mushroom");
	public static final ResourceLocation HABITAT_NETHER = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/nether");
	public static final ResourceLocation HABITAT_OCEAN = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/ocean");
	public static final ResourceLocation HABITAT_PLAINS = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/plains");
	public static final ResourceLocation HABITAT_SNOW = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/snow");
	public static final ResourceLocation HABITAT_SWAMP = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/swamp");
	public static final ResourceLocation HABITAT_TAIGA = new ResourceLocation(ForestryConstants.MOD_ID, "habitats/taiga");
	public static final ResourceLocation MISC_ACCESS_SHARED = new ResourceLocation(ForestryConstants.MOD_ID, "misc/access.shared");
	public static final ResourceLocation MISC_ENERGY = new ResourceLocation(ForestryConstants.MOD_ID, "misc/energy");
	public static final ResourceLocation MISC_HINT = new ResourceLocation(ForestryConstants.MOD_ID, "misc/hint");
	public static final ResourceLocation ANALYZER_ANYTHING = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/anything");
	public static final ResourceLocation ANALYZER_BEE = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/bee");
	public static final ResourceLocation ANALYZER_CAVE = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/cave");
	public static final ResourceLocation ANALYZER_CLOSED = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/closed");
	public static final ResourceLocation ANALYZER_DRONE = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/drone");
	public static final ResourceLocation ANALYZER_FLYER = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/flyer");
	public static final ResourceLocation ANALYZER_ITEM = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/item");
	public static final ResourceLocation ANALYZER_NOCTURNAL = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/nocturnal");
	public static final ResourceLocation ANALYZER_PRINCESS = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/princess");
	public static final ResourceLocation ANALYZER_PURE_BREED = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/pure_breed");
	public static final ResourceLocation ANALYZER_PURE_CAVE = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/pure_cave");
	public static final ResourceLocation ANALYZER_PURE_FLYER = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/pure_flyer");
	public static final ResourceLocation ANALYZER_PURE_NOCTURNAL = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/pure_nocturnal");
	public static final ResourceLocation ANALYZER_QUEEN = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/queen");
	public static final ResourceLocation ANALYZER_TREE = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/tree");
	public static final ResourceLocation ANALYZER_SAPLING = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/sapling");
	public static final ResourceLocation ANALYZER_POLLEN = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/pollen");
	public static final ResourceLocation ANALYZER_FLUTTER = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/flutter");
	public static final ResourceLocation ANALYZER_BUTTERFLY = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/butterfly");
	public static final ResourceLocation ANALYZER_SERUM = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/serum");
	public static final ResourceLocation ANALYZER_CATERPILLAR = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/caterpillar");
	public static final ResourceLocation ANALYZER_COCOON = new ResourceLocation(ForestryConstants.MOD_ID, "analyzer/cocoon");
	public static final ResourceLocation ERROR_ERRORED = new ResourceLocation(ForestryConstants.MOD_ID, "errors/errored");
	public static final ResourceLocation ERROR_UNKNOWN = new ResourceLocation(ForestryConstants.MOD_ID, "errors/unknown");
	public static final ResourceLocation SLOT_BLOCKED = new ResourceLocation(ForestryConstants.MOD_ID, "slots/blocked");
	public static final ResourceLocation SLOT_BLOCKED_2 = new ResourceLocation(ForestryConstants.MOD_ID, "slots/blocked_2");
	public static final ResourceLocation SLOT_LIQUID = new ResourceLocation(ForestryConstants.MOD_ID, "slots/liquid");
	public static final ResourceLocation SLOT_CONTAINER = new ResourceLocation(ForestryConstants.MOD_ID, "slots/container");
	public static final ResourceLocation SLOT_LOCKED = new ResourceLocation(ForestryConstants.MOD_ID, "slots/locked");
	public static final ResourceLocation SLOT_COCOON = new ResourceLocation(ForestryConstants.MOD_ID, "slots/cocoon");
	public static final ResourceLocation SLOT_BEE = new ResourceLocation(ForestryConstants.MOD_ID, "slots/bee");
	public static final ResourceLocation MAIL_CARRIER_PLAYER = new ResourceLocation(ForestryConstants.MOD_ID, "mail/carrier.player");
	public static final ResourceLocation MAIL_CARRIER_TRADER = new ResourceLocation(ForestryConstants.MOD_ID, "mail/carrier.trader");
}
