package forestry.arboriculture.blocks;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;

import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.core.IBlockSubtype;
import forestry.api.genetics.alleles.TreeChromosomes;

/**
 * Used for the default leaf, fruit, and decorative blocks.
 */
public enum ForestryLeafType implements IBlockSubtype {
	OAK(ForestryTreeSpecies.OAK, new Color(4764952), new Color(4764952).brighter()),
	DARK_OAK(ForestryTreeSpecies.DARK_OAK, new Color(4764952), new Color(4764952).brighter()),
	BIRCH(ForestryTreeSpecies.BIRCH, new Color(8431445), new Color(0xb0c648)),
	LIME(ForestryTreeSpecies.LIME, new Color(0x5ea107), new Color(0x5ea107).brighter()),
	WALNUT(ForestryTreeSpecies.WALNUT, new Color(0x798c55), new Color(0xb0c648)),
	CHESTNUT(ForestryTreeSpecies.CHESTNUT, new Color(0x5ea107), new Color(0xb0c648)),
	CHERRY(ForestryTreeSpecies.CHERRY, new Color(0xe691da), new Color(0xe63e59)),
	LEMON(ForestryTreeSpecies.LEMON, new Color(0x88af54), new Color(0xa3b850)),
	PLUM(ForestryTreeSpecies.PLUM, new Color(0x589246), new Color(0xa3b850)),
	MAPLE(ForestryTreeSpecies.MAPLE, new Color(0xd4f425), new Color(0x619a3c)),
	SPRUCE(ForestryTreeSpecies.SPRUCE, new Color(6396257), new Color(0x539d12)),
	LARCH(ForestryTreeSpecies.LARCH, new Color(0x698f90), new Color(0x569896)),
	PINE(ForestryTreeSpecies.PINE, new Color(0xfeff8f), new Color(0xffd98f)),
	SEQUOIA(ForestryTreeSpecies.SEQUOIA, new Color(0x418e71), new Color(0x569896)),
	GIANT_SEQUOIA(ForestryTreeSpecies.GIANT_SEQUOIA, new Color(0x738434), new Color(0x738434).brighter()),
	JUNGLE(ForestryTreeSpecies.JUNGLE, new Color(4764952), new Color(0x658917)),
	TEAK(ForestryTreeSpecies.TEAK, new Color(0xfeff8f), new Color(0xffd98f)),
	IPE(ForestryTreeSpecies.IPE, new Color(0xfdd207), new Color(0xad8f04)),
	KAPOK(ForestryTreeSpecies.KAPOK, new Color(0x89987b), new Color(0x89aa9e)),
	EBONY(ForestryTreeSpecies.EBONY, new Color(0xa2d24a), new Color(0xc4d24a)),
	ZEBRAWOOD(ForestryTreeSpecies.ZEBRAWOOD, new Color(0xa2d24a), new Color(0xc4d24a)),
	MAHOGONY(ForestryTreeSpecies.MAHOGONY, new Color(0x8ab154), new Color(0xa9b154)),
	ACACIA_VANILLA(ForestryTreeSpecies.ACACIA_VANILLA, new Color(0x616101), new Color(0xb3b302)),
	DESERT_ACACIA(ForestryTreeSpecies.DESERT_ACACIA, new Color(0x748C1C), new Color(0xb3b302)),
	PADAUK(ForestryTreeSpecies.PADAUK, new Color(0xd0df8c), new Color(0x435c32)),
	BALSA(ForestryTreeSpecies.BALSA, new Color(0x59ac00), new Color(0xfeff8f)),
	COCOBOLO(ForestryTreeSpecies.COCOBOLO, new Color(0x6aa17a), new Color(0x487d4c)),
	WENGE(ForestryTreeSpecies.WENGE, new Color(0xada157), new Color(0xad8a57)),
	BAOBAB(ForestryTreeSpecies.BAOBAB, new Color(0xfeff8f), new Color(0xffd98f)),
	MAHOE(ForestryTreeSpecies.MAHOE, new Color(0xa0ba1b), new Color(0x79a175)),
	WILLOW(ForestryTreeSpecies.WILLOW, new Color(0xa3b8a5), new Color(0xa3b850)),
	SIPIRI(ForestryTreeSpecies.SIPIRI, new Color(0x678911), new Color(0x79a175)),
	PAPAYA(ForestryTreeSpecies.PAPAYA, new Color(0x6d9f58), new Color(0x75E675)),
	DATE(ForestryTreeSpecies.DATE, new Color(0xcbcd79), new Color(0xB3F370)),
	POPLAR(ForestryTreeSpecies.POPLAR, new Color(0xa3b8a5), new Color(0x539d12));

	private final ResourceLocation speciesId;

	private IFruit fruit;
	private ILeafSpriteProvider leafSprite;
	private ITree individual;

	ForestryLeafType(ResourceLocation speciesId) {
		this.speciesId = speciesId;
	}

	public void setSpecies(ITreeSpecies species) {
		this.fruit = species.getDefaultGenome().getActiveValue(TreeChromosomes.FRUITS);
		this.leafSprite = species.getLeafSpriteProvider();
		this.individual = species.createIndividual();
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public IFruit getFruit() {
		return this.fruit;
	}

	public ILeafSpriteProvider getLeafSpriteProvider() {
		return this.leafSprite;
	}

	public ITree getIndividual() {
		return this.individual;
	}

	public ResourceLocation getSpeciesId() {
		return this.speciesId;
	}
}
