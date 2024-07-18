package forestry.plugin.species;

import java.awt.Color;

import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.lepidopterology.ForestryButterflySpecies;
import forestry.api.plugin.ILepidopterologyRegistration;

import static forestry.api.genetics.ForestryTaxa.*;

public class DefaultButterflySpecies {
	public static void register(ILepidopterologyRegistration butterflies) {
		butterflies.registerSpecies(ForestryButterflySpecies.CABBAGE_WHITE, GENUS_PIERIS, SPECIES_CABBAGE_WHITE, true, new Color(0xccffee), 1.0f)
				.setGenome(genome -> {
					genome.set(ButterflyChromosomes.SIZE, ForestryAlleles.SIZE_AVERAGE);
				});
		butterflies.registerSpecies(ForestryButterflySpecies.BRIMSTONE, GENUS_GONEPTERYX, SPECIES_BRIMSTONE, true, new Color(0xf0ee38), 1.0f);
		butterflies.registerSpecies(ForestryButterflySpecies.AURORA, GENUS_ANTHOCHARIS, SPECIES_AURORA, true, new Color(0xe34f05), 0.5f)
				.setGenome();
		butterflies.registerSpecies(ForestryButterflySpecies.POSTILLION, GENUS_COLIAS, SPECIES_POSTILLION, true, new Color(0xd77e04), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.PALAENO_SULPHUR, GENUS_COLIAS, SPECIES_PALAENO_SULPHUR, true, new Color(0xf8fba3), 0.4f);
		butterflies.registerSpecies(ForestryButterflySpecies.RESEDA, GENUS_PONTIA, SPECIES_RESEDA, true, new Color(0x747d48), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.SPRING_AZURE, GENUS_CELASTRINA, SPECIES_SPRING_AZURE, true, new Color(0xb8cae2), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.GOZORA_AZURE, GENUS_CELASTRINA, SPECIES_GOZORA_AZURE, true, new Color(0x6870e7), 0.2f);
		butterflies.registerSpecies(ForestryButterflySpecies.CITRUS_SWALLOWTAIL, GENUS_PAPILIO, SPECIES_CITRUS_SWALLOWTAIL, false, new Color(0xeae389), 1.0f);
		butterflies.registerSpecies(ForestryButterflySpecies.EMERALD_PEACOCK, GENUS_PAPILIO, SPECIES_EMERALD_PEACOCK, true, new Color(0x7cfe80), 0.1f);
		butterflies.registerSpecies(ForestryButterflySpecies.THOAS_SWALLOWTAIL, GENUS_PAPILIO, SPECIES_THOAS_SWALLOWTAIL, false, new Color(0xeac783), 0.2f);
		butterflies.registerSpecies(ForestryButterflySpecies.SPICEBUSH_SWALLOWTAIL, GENUS_PAPILIO, SPECIES_SPICEBUSH_SWALLOWTAIL, true, new Color(0xeefeff), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.BLACK_SWALLOWTAIL, GENUS_PAPILIO, SPECIES_BLACK_SWALLOWTAIL, true, new Color(0xeac783), 1.0f);
		butterflies.registerSpecies(ForestryButterflySpecies.ZEBRA_SWALLOWTAIL, GENUS_PROTOGRAPHIUM, SPECIES_ZEBRA_SWALLOWTAIL, true, new Color(0xeafeff), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.GLASSWING, GENUS_GRETA, SPECIES_GLASSWING, true, new Color(0x583732), 0.1f);
		butterflies.registerSpecies(ForestryButterflySpecies.SPECKLED_WOOD, GENUS_PARARGE, SPECIES_SPECKLED_WOOD, true, new Color(0x947245), 1.0f);
		butterflies.registerSpecies(ForestryButterflySpecies.MADEIRAN_SPECKLED_WOOD, GENUS_PARARGE, SPECIES_MADEIRAN_SPECKLED_WOOD, true, new Color(0x402919), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.CANARY_SPECKLED_WOOD, GENUS_PARARGE, SPECIES_CANARY_SPECKLED_WOOD, true, new Color(0x51372a), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.MENELAUS_BLUE_MORPHO, GENUS_MORPHO, SPECIES_MENELAUS_BLUE_MORPHO, true, new Color(0x72e1fd), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.PELEIDES_BLUE_MORPHO, GENUS_MORPHO, SPECIES_PELEIDES_BLUE_MORPHO, true, new Color(0x6ecce8), 0.25f);
		butterflies.registerSpecies(ForestryButterflySpecies.RHETENOR_BLUE_MORPHO, GENUS_MORPHO, SPECIES_RHETENOR_BLUE_MORPHO, true, new Color(0x00bef8), 0.1f);
		butterflies.registerSpecies(ForestryButterflySpecies.COMMA, GENUS_POLYGONIA, SPECIES_COMMA, true, new Color(0xf89505), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.BATESIA, GENUS_BATESIA, SPECIES_BATESIA, true, new Color(0xfe7763), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.BLUE_WING, GENUS_MYSCELIA, SPECIES_BLUE_WING, true, new Color(0x3a93cc), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.MONARCH, GENUS_DANAUS, SPECIES_MONARCH, true, new Color(0xffa722), 0.2f);
		butterflies.registerSpecies(ForestryButterflySpecies.BLUE_DUKE, GENUS_BASSARONA, SPECIES_BLUE_DUKE, true, new Color(0x304240), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.GLASSY_TIGER, GENUS_PARANTICA, SPECIES_GLASSY_TIGER, true, new Color(0x5b3935), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.POSTMAN, GENUS_HELICONIUS, SPECIES_POSTMAN, true, new Color(0xf7302d), 0.3f);
		butterflies.registerSpecies(ForestryButterflySpecies.MALACHITE, GENUS_SIPROETA, SPECIES_MALACHITE, true, new Color(0xbdff53), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.LEOPARD_LACEWING, GENUS_CETHOSIA, SPECIES_LEOPARD_LACEWING, true, new Color(0xfb8a06), 0.7f);
		butterflies.registerSpecies(ForestryButterflySpecies.DIANA_FRITILLARY, GENUS_SPEYERIA, SPECIES_DIANA_FRITILLARY, true, new Color(0xffac05), 0.6f);



		butterflies.registerSpecies(ForestryButterflySpecies.BRIMSTONE_MOTH, GENUS_OPISTHOGRAPTIS, SPECIES_BRIMSTONE_MOTH, true, new Color(0xffea40), 1.0f);
		butterflies.registerSpecies(ForestryButterflySpecies.LATTICED_HEATH, GENUS_CHIASMIA, SPECIES_LATTICED_HEATH, true, new Color(0xf2f0be), 0.5f);
		butterflies.registerSpecies(ForestryButterflySpecies.ATLAS, GENUS_ATTACUS, SPECIES_ATLAS, false, new Color(0xd96e3d), 0.1f);
		// todo rarity should probably not be 0.0f
		butterflies.registerSpecies(ForestryButterflySpecies.BOMBYX_MORI, GENUS_BOMBYX, SPECIES_BOMBYX_MORI, false, new Color(0xDADADA), 0.0f);
	}
}
