package forestry.plugin.species;

import java.awt.Color;

import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.arboriculture.ForestryWoodType;
import forestry.api.arboriculture.LeafType;
import forestry.api.plugin.IArboricultureRegistration;

import static forestry.api.genetics.ForestryTaxa.*;

// todo fix IRL inaccuracies
public class DefaultTreeSpecies {
	public static void register(IArboricultureRegistration arboriculture) {
		arboriculture.registerSpecies(ForestryTreeSpecies.OAK, GENUS_QUERCUS, SPECIES_OAK, false, LeafType.OAK, new Color(4764952), new Color(4764952).brighter(), EnumVanillaWoodType.OAK);
		arboriculture.registerSpecies(ForestryTreeSpecies.DARK_OAK, GENUS_QUERCUS, SPECIES_DARK_OAK, false, LeafType.OAK, new Color(4764952), new Color(4764952).brighter(), EnumVanillaWoodType.DARK_OAK);
		arboriculture.registerSpecies(ForestryTreeSpecies.BIRCH, GENUS_BETULA, SPECIES_BIRCH, false, LeafType.BIRCH, new Color(8431445), new Color(0xb0c648), EnumVanillaWoodType.BIRCH);
		arboriculture.registerSpecies(ForestryTreeSpecies.LIME, GENUS_TILIA, SPECIES_LIME, true, LeafType.BIRCH, new Color(0x5ea107), new Color(0x5ea107).brighter(), ForestryWoodType.LIME);
		arboriculture.registerSpecies(ForestryTreeSpecies.WALNUT, GENUS_JUGLANS, SPECIES_WALNUT, true, LeafType.ACACIA, new Color(0x798c55), new Color(0xb0c648), ForestryWoodType.WALNUT);
		arboriculture.registerSpecies(ForestryTreeSpecies.CHESTNUT, GENUS_CASTANEA, SPECIES_CHESTNUT, true, LeafType.BIRCH, new Color(0x5ea107), new Color(0xb0c648), ForestryWoodType.CHESTNUT);
		arboriculture.registerSpecies(ForestryTreeSpecies.CHERRY, GENUS_PRUNUS, SPECIES_CHERRY, true, LeafType.BIRCH, new Color(0xe691da), new Color(0xe63e59), ForestryWoodType.CHERRY);
		arboriculture.registerSpecies(ForestryTreeSpecies.LEMON, GENUS_CITRUS, SPECIES_LEMON, true, LeafType.OAK, new Color(0x88af54), new Color(0xa3b850), ForestryWoodType.CITRUS);
		arboriculture.registerSpecies(ForestryTreeSpecies.PLUM, GENUS_PRUNUS, SPECIES_PLUM, true, LeafType.OAK, new Color(0x589246), new Color(0xa3b850), ForestryWoodType.PLUM);
		arboriculture.registerSpecies(ForestryTreeSpecies.MAPLE, GENUS_ACER, SPECIES_MAPLE, true, LeafType.MAPLE, new Color(0xd4f425), new Color(0x619a3c), ForestryWoodType.MAPLE);
		arboriculture.registerSpecies(ForestryTreeSpecies.SPRUCE, GENUS_PICEA, SPECIES_SPRUCE, false, LeafType.SPRUCE, new Color(6396257), new Color(0x539d12), EnumVanillaWoodType.SPRUCE);
		arboriculture.registerSpecies(ForestryTreeSpecies.LARCH, GENUS_LARIX, SPECIES_LARCH, true, LeafType.SPRUCE, new Color(0x698f90), new Color(0x569896), ForestryWoodType.LARCH);
		arboriculture.registerSpecies(ForestryTreeSpecies.PINE, GENUS_PINUS, SPECIES_PINE, true, LeafType.SPRUCE, new Color(0xfeff8f), new Color(0xffd98f), ForestryWoodType.PINE);
		arboriculture.registerSpecies(ForestryTreeSpecies.SEQUOIA, GENUS_SEQUOIA, SPECIES_SEQUOIA, false, LeafType.SPRUCE, new Color(0x418e71), new Color(0x569896), ForestryWoodType.SEQUOIA);
		arboriculture.registerSpecies(ForestryTreeSpecies.GIANT_SEQUOIA, GENUS_SEQUOIADENDRON, SPECIES_GIANT_SEQUOIA, false, LeafType.SPRUCE, new Color(0x738434), new Color(0x738434).brighter(), ForestryWoodType.GIGANTEUM);
		arboriculture.registerSpecies(ForestryTreeSpecies.JUNGLE, GENUS_TROPICAL, SPECIES_JUNGLE, false, LeafType.JUNGLE, new Color(4764952), new Color(0x658917), EnumVanillaWoodType.JUNGLE);
		arboriculture.registerSpecies(ForestryTreeSpecies.TEAK, GENUS_TECTONA, SPECIES_TEAK, true, LeafType.JUNGLE, new Color(0xfeff8f), new Color(0xffd98f), ForestryWoodType.TEAK);
		arboriculture.registerSpecies(ForestryTreeSpecies.IPE, GENUS_TABEBUIA, SPECIES_IPE, true, LeafType.JUNGLE, new Color(0xfdd207), new Color(0xad8f04), ForestryWoodType.IPE);
		arboriculture.registerSpecies(ForestryTreeSpecies.KAPOK, GENUS_CEIBA, SPECIES_KAPOK, true, LeafType.JUNGLE, new Color(0x89987b), new Color(0x89aa9e), ForestryWoodType.KAPOK);
		arboriculture.registerSpecies(ForestryTreeSpecies.EBONY, GENUS_EBONY, SPECIES_EBONY, true, LeafType.JUNGLE, new Color(0xa2d24a), new Color(0xc4d24a), ForestryWoodType.EBONY);
		arboriculture.registerSpecies(ForestryTreeSpecies.ZEBRAWOOD, GENUS_ASTRONIUM, SPECIES_ZEBRAWOOD, false, LeafType.JUNGLE, new Color(0xa2d24a), new Color(0xc4d24a), ForestryWoodType.ZEBRAWOOD);
		arboriculture.registerSpecies(ForestryTreeSpecies.MAHOGONY, GENUS_MAHOGANY, SPECIES_MAHOGONY, true, LeafType.JUNGLE, new Color(0x8ab154), new Color(0xa9b154), ForestryWoodType.MAHOGANY);
		arboriculture.registerSpecies(ForestryTreeSpecies.ACACIA, GENUS_ACACIA, SPECIES_ACACIA, true, LeafType.ACACIA, new Color(0x616101), new Color(0xb3b302), EnumVanillaWoodType.ACACIA);
		arboriculture.registerSpecies(ForestryTreeSpecies.DESERT_ACACIA, GENUS_ACACIA, SPECIES_DESERT_ACACIA, true, LeafType.ACACIA, new Color(0x748C1C), new Color(0xb3b302), ForestryWoodType.ACACIA_DESERT);
		arboriculture.registerSpecies(ForestryTreeSpecies.PADAUK, GENUS_PTEROCARPUS, SPECIES_PADAUK, true, LeafType.ACACIA, new Color(0xd0df8c), new Color(0x435c32), ForestryWoodType.PADAUK);
		arboriculture.registerSpecies(ForestryTreeSpecies.BALSA, GENUS_OCHROMA, SPECIES_BALSA, true, LeafType.ACACIA, new Color(0x59ac00), new Color(0xfeff8f), ForestryWoodType.BALSA);
		arboriculture.registerSpecies(ForestryTreeSpecies.COCOBOLO, GENUS_DALBERGIA, SPECIES_COCOBOLO, false, LeafType.MANGROVE, new Color(0x6aa17a), new Color(0x487d4c), ForestryWoodType.COCOBOLO);
		arboriculture.registerSpecies(ForestryTreeSpecies.WENGE, GENUS_MILLETTIA, SPECIES_WENGE, true, LeafType.OAK, new Color(0xada157), new Color(0xad8a57), ForestryWoodType.WENGE);
		arboriculture.registerSpecies(ForestryTreeSpecies.BAOBAB, GENUS_ADANSONIA, SPECIES_BAOBAB, true, LeafType.ACACIA, new Color(0xfeff8f), new Color(0xffd98f), ForestryWoodType.BAOBAB);
		arboriculture.registerSpecies(ForestryTreeSpecies.MAHOE, GENUS_TALIPARITI, SPECIES_MAHOE, true, LeafType.OAK, new Color(0xa0ba1b), new Color(0x79a175), ForestryWoodType.MAHOE);
		arboriculture.registerSpecies(ForestryTreeSpecies.WILLOW, GENUS_SALIX, SPECIES_WILLOW, true, LeafType.WILLOW, new Color(0xa3b8a5), new Color(0xa3b850), ForestryWoodType.WILLOW);
		arboriculture.registerSpecies(ForestryTreeSpecies.SIPIRI, GENUS_CHLOROCARDIUM, SPECIES_SIPIRI, true, LeafType.MANGROVE, new Color(0x678911), new Color(0x79a175), ForestryWoodType.GREENHEART);
		arboriculture.registerSpecies(ForestryTreeSpecies.PAPAYA, GENUS_CARICA, SPECIES_PAPAYA, true, LeafType.PALM, new Color(0x6d9f58), new Color(0x75E675), ForestryWoodType.PAPAYA);
		arboriculture.registerSpecies(ForestryTreeSpecies.DATE, GENUS_PHOENIX, SPECIES_DATE, true, LeafType.PALM, new Color(0xcbcd79), new Color(0xB3F370), ForestryWoodType.PALM);
		arboriculture.registerSpecies(ForestryTreeSpecies.POPLAR, GENUS_POPULUS, SPECIES_POPLAR, true, LeafType.BIRCH, new Color(0xa3b8a5), new Color(0x539d12), ForestryWoodType.POPLAR);
	}
}
