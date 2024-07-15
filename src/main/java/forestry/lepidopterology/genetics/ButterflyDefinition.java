/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.lepidopterology.genetics;

import com.google.common.base.CaseFormat;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Locale;

import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.ForestryTaxa;
import forestry.api.genetics.IAlleleRegistry;
import genetics.api.alleles.IAlleleTemplate;
import genetics.api.alleles.IAlleleTemplateBuilder;
import forestry.api.genetics.IGenome;
import genetics.api.root.ITemplateContainer;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.ComponentKeys;
import genetics.api.root.components.IRootComponent;

import forestry.api.ForestryConstants;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryAlleles;
import forestry.api.genetics.alleles.ForestryChromosomes;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.ISpeciesChromosome;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.genetics.ButterflyChromosome;
import forestry.api.lepidopterology.genetics.ButterflyLifeStage;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpecies;
import forestry.api.lepidopterology.genetics.IAlleleButterflySpeciesBuilder;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.api.lepidopterology.genetics.IButterflyMutationBuilder;
import forestry.core.genetics.alleles.LifespanAllele;
import forestry.core.genetics.alleles.MetabolismAllele;
import forestry.core.genetics.alleles.SizeAllele;
import forestry.core.genetics.alleles.SpeedAllele;
import forestry.core.genetics.alleles.ToleranceAllele;

public enum ButterflyDefinition implements IButterflyDefinition {
	CabbageWhite(ForestryTaxa.GENUS_PIERIS, "cabbageWhite", "rapae", new Color(0xccffee), true, 1.0f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
		}
	},
	Brimstone(ForestryTaxa.GENUS_GONEPTERYX, "brimstone", "rhamni", new Color(0xf0ee38), true, 1.0f),
	Aurora(ForestryTaxa.GENUS_ANTHOCHARIS, "orangeTip", "cardamines", new Color(0xe34f05), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.SMALLER);
		}
	},
	Postillion(ForestryTaxa.GENUS_COLIAS, "postillion", "croceus", new Color(0xd77e04), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOW);
		}
	},
	PalaenoSulphur(ForestryTaxa.GENUS_COLIAS, "palaenoSulphur", "palaeno", new Color(0xf8fba3), true, 0.4f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
		}
	},
	Reseda(ForestryTaxa.GENUS_PONTIA, "reseda", "edusa", new Color(0x747d48), true, 0.3f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
		}
	},
	SpringAzure(ForestryTaxa.GENUS_CELASTRINA, "springAzure", "argiolus", new Color(0xb8cae2), true, 0.3f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.SMALLER);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORT);
		}
	},
	GozoraAzure(ForestryTaxa.GENUS_CELASTRINA, "gozoraAzure", "gozora", new Color(0x6870e7), true, 0.2f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.SMALLER);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORT);
		}
	},
	CitrusSwallow(ForestryTaxa.GENUS_PAPILIO, "swallowtailC", "demodocus", new Color(0xeae389), false, 1.0f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGE);
			template.set(ButterflyChromosomes.FERTILITY, 10);
			template.set(ButterflyChromosomes.METABOLISM, MetabolismAllele.FASTER);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORTER);
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.DOWN_1);
			template.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.DOWN_1);
		}
	},
	EmeraldPeacock(ForestryTaxa.GENUS_PAPILIO, "emeraldPeacock", "palinurus", new Color(0x7cfe80), true, 0.1f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGE);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.NORMAL);
			template.set(ButterflyChromosomes.FERTILITY, 5);
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.DOWN_1);
			template.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.DOWN_1);
		}
	},
	ThoasSwallow(ForestryTaxa.GENUS_PAPILIO, "swallowtailT", "thoas", new Color(0xeac783), false, 0.2f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGE);
			template.set(ButterflyChromosomes.LIFESPAN, new LifespanAllele("shortest", 10));
		}
	},
	Spicebush(ForestryTaxa.GENUS_PAPILIO, "swallowtailS", "troilus", new Color(0xeefeff), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
		}
	},
	BlackSwallow(ForestryTaxa.GENUS_PAPILIO, "swallowtailB", "polyxenes", new Color(0xeac783), true, 1.0f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOW);
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGE);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORTER);
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.DOWN_1);
			template.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.DOWN_1);
		}
	},
	ZebraSwallow(ForestryTaxa.GENUS_PROTOGRAPHIUM, "swallowtailZ", "marcellus", new Color(0xeafeff), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
		}
	},
	Glasswing(ForestryTaxa.GENUS_GRETA, "glasswing", "oto", new Color(0x583732), true, 0.1f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.SMALLER);
			template.set(ButterflyChromosomes.LIFESPAN, ForestryAlleles.SHORT);
			template.set(ButterflyChromosomes.FERTILITY, 5);
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.DOWN_1);
		}
	},
	SpeckledWood(ForestryTaxa.GENUS_PARARGE, "speckledWood", "aegeria", new Color(0x947245), true, 1.0f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	MSpeckledWood(ForestryTaxa.GENUS_PARARGE, "speckledWoodM", "xiphia", new Color(0x402919), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	CSpeckledWood(ForestryTaxa.GENUS_PARARGE, "speckledWoodC", "xiphioides", new Color(0x51372a), true, 0.5f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	MBlueMorpho(ForestryTaxa.GENUS_MORPHO, "blueMorphoM", "menelaus", new Color(0x72e1fd), true, 0.5f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGER);
			template.set(ButterflyChromosomes.LIFESPAN, new LifespanAllele("shortest", 10));
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	PBlueMorpho(ForestryTaxa.GENUS_MORPHO, "blueMorphoP", "peleides", new Color(0x6ecce8), true, 0.25f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGER);
			template.set(ButterflyChromosomes.LIFESPAN, new LifespanAllele("shortest", 10));
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	RBlueMorpho(ForestryTaxa.GENUS_MORPHO, "blueMorphoR", "rhetenor", new Color(0x00bef8), true, 0.1f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGER);
			template.set(ButterflyChromosomes.LIFESPAN, new LifespanAllele("shortest", 10));
			template.set(ButterflyChromosomes.FERTILITY, 2);
		}
	},
	Comma(ForestryTaxa.GENUS_POLYGONIA, "comma", "c-album", new Color(0xf89505), true, 0.3f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
		}
	},
	Batesia(ForestryTaxa.GENUS_BATESIA, "paintedBeauty", "hypochlora", new Color(0xfe7763), true, 0.3f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.LARGE);
		}
	},
	BlueWing(ForestryTaxa.GENUS_MYSCELIA, "blueWing", "ethusa", new Color(0x3a93cc), true, 0.3f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
			template.set(ButterflyChromosomes.METABOLISM, MetabolismAllele.NORMAL);
		}
	},
	Monarch(ForestryTaxa.GENUS_DANAUS, "monarch", "plexippus", new Color(0xffa722), true, 0.2f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
		}
	},
	BlueDuke(ForestryTaxa.GENUS_BASSARONA, "blueDuke", "durga", new Color(0x304240), true, 0.5f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.COLD);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.BOTH_1);
		}
	},
	GlassyTiger(ForestryTaxa.GENUS_PARANTICA, "glassyTiger", "aglea", new Color(0x5b3935), true, 0.3f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
		}
	},
	Postman(ForestryTaxa.GENUS_HELICONIUS, "postman", "melpomene", new Color(0xf7302d), true, 0.3f),
	Malachite(ForestryTaxa.GENUS_SIPROETA, "malachite", "stelenes", new Color(0xbdff53), true, 0.5f) {
		@Override
		protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {
			species.setTemperature(TemperatureType.WARM)
				.setHumidity(HumidityType.DAMP);
		}

		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SIZE, SizeAllele.AVERAGE);
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.DOWN_1);
			template.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.DOWN_1);
		}
	},
	LLacewing(ForestryTaxa.GENUS_CETHOSIA, "leopardLacewing", "cyane", new Color(0xfb8a06), true, 0.7f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.TEMPERATURE_TOLERANCE, ToleranceAllele.UP_1);
			template.set(ButterflyChromosomes.HUMIDITY_TOLERANCE, ToleranceAllele.UP_1);
		}
	},
	DianaFrit(ForestryTaxa.GENUS_SPEYERIA, "dianaFritillary", "diana", new Color(0xffac05), true, 0.6f) {
		@Override
		protected void setAlleles(IAlleleTemplateBuilder template) {
			template.set(ButterflyChromosomes.SPEED, SpeedAllele.SLOWER);
			template.set(ButterflyChromosomes.SIZE, SizeAllele.SMALLER);
		}
	};

	private final IAlleleButterflySpecies species;
	private final String genus;
	@Nullable
	private IAlleleTemplate template;
	@Nullable
	private IGenome genome;

	ButterflyDefinition(String genusName, String speciesName, String binomial, Color serumColor, boolean dominant, float rarity) {
		this.genus = genusName;

		String uid = "lepi_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name());
		// todo
		//String parent = IForestryApi.INSTANCE.getGeneticManager().getTaxon(genusName).parent();
		String unlocalizedName = "for.butterflies.species." + parent.getId().substring(parent.rank().name().toLowerCase(Locale.ENGLISH).length() + 1) + '.' + speciesName;
		String unlocalizedDescription = "for.description." + uid;

		String texture = "butterflies/" + uid;

		IAlleleButterflySpeciesBuilder speciesBuilder = ButterflyManager.butterflyFactory.createSpecies(ForestryConstants.MOD_ID, uid, speciesName)
			.setDescriptionKey(unlocalizedDescription)
			.setTranslationKey(unlocalizedName)
			.setTexture(texture)
			.setDominant(dominant)
			.setBranch(genus)
			.setBinomial(binomial)
			.setSerumColour(serumColor)
			.setRarity(rarity);
		setSpeciesProperties(speciesBuilder);
		this.species = speciesBuilder.build();
	}

	public static void preInit() {
		// just used to initialize the enums
	}

	public static void initButterflies() {
		for (ButterflyDefinition butterfly : values()) {
			butterfly.registerMutations();
		}
	}

	@Override
	public <C extends IRootComponent<IButterfly>> void onComponentSetup(C component) {
		ComponentKey key = component.getKey();
		if (key == ComponentKeys.TEMPLATES) {
			ITemplateContainer registry = (ITemplateContainer) component;
			IAlleleTemplateBuilder templateBuilder = ButterflyHelper.createTemplate();
			templateBuilder.set(ButterflyChromosomes.SPECIES, species);
			setAlleles(templateBuilder);

			this.template = templateBuilder.build();
			this.genome = template.toGenome();
			registry.registerTemplate(this.template);
		}
	}

	public void registerAlleles(IAlleleRegistry registry) {
		registry.registerAllele(species, ButterflyChromosomes.SPECIES);
	}

	protected void setSpeciesProperties(IAlleleButterflySpeciesBuilder species) {

	}

	protected void setAlleles(IAlleleTemplateBuilder template) {

	}

	protected void registerMutations() {

	}

	protected final IButterflyMutationBuilder registerMutation(IButterflyDefinition parent1, IButterflyDefinition parent2, int chance) {
		IAlleleButterflySpecies species1;
		IAlleleButterflySpecies species2;

		if (parent1 instanceof ButterflyDefinition) {
			species1 = ((ButterflyDefinition) parent1).species;
		} else if (parent1 instanceof MothDefinition) {
			species1 = parent1.getSpecies();
		} else {
			throw new IllegalArgumentException("Unknown parent type " + parent1);
		}

		if (parent2 instanceof ButterflyDefinition) {
			species2 = ((ButterflyDefinition) parent2).species;
		} else if (parent2 instanceof MothDefinition) {
			species2 = parent2.getSpecies();
		} else {
			throw new IllegalArgumentException("Unknown parent type " + parent2);
		}

		return ButterflyManager.butterflyMutationFactory.createMutation(species1, species2, getTemplate().alleles(), chance);
	}

	@Override
	public IButterfly createIndividual() {
		return getTemplate().toIndividual(ButterflyHelper.getRoot());
	}

	@Override
	public final IAlleleTemplate getTemplate() {
		return template;
	}

	@Override
	public final IGenome getGenome() {
		return genome;
	}

	@Override
	public final ItemStack getMemberStack(ButterflyLifeStage flutterType) {
		IButterfly butterfly = createIndividual();
		return ButterflyHelper.getRoot().getTypes().createStack(butterfly, flutterType);
	}

	@Override
	public ISpeciesType<IButterfly> getSpecies() {
		return species;
	}
}
