package forestry.plugin;

import forestry.api.genetics.ForestryTaxa;
import forestry.api.plugin.IGeneticRegistration;

public class ButterflyTaxonomy {
	@SuppressWarnings("CodeBlock2Expr")
	public static void defineTaxa(IGeneticRegistration genetics) {
		genetics.defineTaxon(ForestryTaxa.CLASS_INSECTS, ForestryTaxa.ORDER_LEPIDOPTERA, order -> {
			order.defineSubTaxon(ForestryTaxa.FAMILY_GEOMETER_MOTHS, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_OPISTHOGRAPTIS);
				family.defineSubTaxon(ForestryTaxa.GENUS_CHIASMIA);
			});
			order.defineSubTaxon(ForestryTaxa.FAMILY_SATURNIIDS, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_ATTACUS);
			});
			order.defineSubTaxon(ForestryTaxa.FAMILY_PIERIDAE, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_PIERIS);
				family.defineSubTaxon(ForestryTaxa.GENUS_GONEPTERYX);
				family.defineSubTaxon(ForestryTaxa.GENUS_ANTHOCHARIS);
				family.defineSubTaxon(ForestryTaxa.GENUS_COLIAS);
				family.defineSubTaxon(ForestryTaxa.GENUS_PONTIA);
				family.defineSubTaxon(ForestryTaxa.GENUS_CELASTRINA);
			});
			order.defineSubTaxon(ForestryTaxa.FAMILY_BRUSH_FOOTED_BUTTERFLIES, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_PARARGE);
				family.defineSubTaxon(ForestryTaxa.GENUS_POLYGONIA);
				family.defineSubTaxon(ForestryTaxa.GENUS_MORPHO);
				family.defineSubTaxon(ForestryTaxa.GENUS_GRETA);
				family.defineSubTaxon(ForestryTaxa.GENUS_BATESIA);
				family.defineSubTaxon(ForestryTaxa.GENUS_MYSCELIA);
				family.defineSubTaxon(ForestryTaxa.GENUS_DANAUS);
				family.defineSubTaxon(ForestryTaxa.GENUS_BASSARONA);
				family.defineSubTaxon(ForestryTaxa.GENUS_PARANTICA);
				family.defineSubTaxon(ForestryTaxa.GENUS_HELICONIUS);
				family.defineSubTaxon(ForestryTaxa.GENUS_SIPROETA);
				family.defineSubTaxon(ForestryTaxa.GENUS_CETHOSIA);
				family.defineSubTaxon(ForestryTaxa.GENUS_SPEYERIA);
			});
			order.defineSubTaxon(ForestryTaxa.FAMILY_GOSSAMER_WINGED_BUTTERFLIES);
			order.defineSubTaxon(ForestryTaxa.FAMILY_SWALLOWTAIL_BUTTERFLIES, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_PAPILIO);
				family.defineSubTaxon(ForestryTaxa.GENUS_PROTOGRAPHIUM);
			});
			order.defineSubTaxon(ForestryTaxa.FAMILY_MINECRAFT_BUTTERFLIES);
			order.defineSubTaxon(ForestryTaxa.FAMILY_SILKWORM_MOTHS, family -> {
				family.defineSubTaxon(ForestryTaxa.GENUS_BOMBYX);
			});
		});
	}
}
