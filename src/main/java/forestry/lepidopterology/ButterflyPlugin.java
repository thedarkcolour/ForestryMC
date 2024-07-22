package forestry.lepidopterology;

import java.util.List;

import forestry.api.ForestryConstants;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.genetics.alleles.ForestryChromosomes;
import forestry.api.lepidopterology.genetics.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.genetics.ForestryComponentKeys;
import forestry.api.genetics.IResearchHandler;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.core.genetics.alleles.MetabolismAllele;
import forestry.core.genetics.alleles.SizeAllele;
import forestry.core.genetics.root.IResearchPlugin;
import forestry.core.genetics.root.ResearchHandler;
import forestry.lepidopterology.features.LepidopterologyItems;
import forestry.lepidopterology.genetics.ButterflyBranchDefinition;
import forestry.lepidopterology.genetics.ButterflyDefinition;
import forestry.lepidopterology.genetics.ButterflyHelper;
import forestry.lepidopterology.genetics.ButterflySpeciesType;
import forestry.lepidopterology.genetics.MothDefinition;
import forestry.lepidopterology.genetics.alleles.ButterflyAlleles;

import genetics.api.GeneticPlugin;
import genetics.api.IGeneticApiInstance;
import genetics.api.IGeneticFactory;
import genetics.api.IGeneticPlugin;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.alleles.ISpecies<?>;
import forestry.api.genetics.IClassificationRegistry;
import genetics.api.individual.IIndividual;
import genetics.api.organism.IOrganismTypes;
import genetics.api.root.IGeneticListenerRegistry;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IIndividualRootBuilder;
import genetics.api.root.IRootManager;
import genetics.api.root.components.ComponentKeys;

@GeneticPlugin(modId = ForestryConstants.MOD_ID)
public class ButterflyPlugin implements IGeneticPlugin {
	@Override
	public void registerListeners(IGeneticListenerRegistry registry) {
		registry.add(ForestrySpeciesTypes.BUTTERFLY, ButterflyDefinition.values());
		registry.add(ForestrySpeciesTypes.BUTTERFLY, MothDefinition.values());
	}

	@Override
	public void registerAlleles(IAlleleRegistry registry) {
		registry.registerAlleles(SizeAllele.values(), ButterflyChromosomes.SIZE);
		registry.registerAlleles(MetabolismAllele.values(), ButterflyChromosomes.METABOLISM);
		ButterflyAlleles.registerAlleles(registry);
	}

	@Override
	public void createRoot(IRootManager rootManager, IGeneticFactory geneticFactory) {
		IIndividualRootBuilder<IButterfly> rootBuilder = rootManager.createRoot(ForestrySpeciesTypes.BUTTERFLY);
		rootBuilder
			.setRootFactory(ButterflySpeciesType::new)
			.setSpeciesType(ButterflyChromosomes.SPECIES)
			.addListener(ComponentKeys.TYPES, (IOrganismTypes<IButterfly> builder) -> {
				builder.registerType(ButterflyLifeStage.SERUM, LepidopterologyItems.SERUM_GE::stack);
				builder.registerType(ButterflyLifeStage.CATERPILLAR, LepidopterologyItems.CATERPILLAR_GE::stack);
				builder.registerType(ButterflyLifeStage.COCOON, LepidopterologyItems.COCOON_GE::stack);
				builder.registerType(ButterflyLifeStage.BUTTERFLY, LepidopterologyItems.BUTTERFLY_GE::stack);
			})
			.addComponent(ComponentKeys.TRANSLATORS)
			.addComponent(ComponentKeys.MUTATIONS)
			.addComponent(ForestryComponentKeys.RESEARCH, ResearchHandler::new)
			.addListener(ForestryComponentKeys.RESEARCH, (IResearchHandler<IButterfly> component) -> {
				component.addPlugin(new IResearchPlugin() {
					@Override
					public float getResearchSuitability(ISpecies<?> species, ItemStack stack) {
						if (stack.isEmpty() || !(species instanceof IButterflySpecies butterflySpecies)) {
							return -1;
						}

						if (stack.getItem() == Items.GLASS_BOTTLE) {
							return 0.9f;
						}

						for (Product product : butterflySpecies.getButterflyLoot()) {
							if (stack.is(product.item())) {
								return 1.0f;
							}
						}
						for (Product product : butterflySpecies.getCaterpillarLoot()) {
							if (stack.is(product.item())) {
								return 1.0f;
							}
						}
						return -1;
					}

					@Override
					public List<ItemStack> getResearchBounty(ISpecies<?> species, Level world, GameProfile researcher, IIndividual individual, int bountyLevel) {
						ItemStack serum = individual.copyWithStage(ButterflyLifeStage.SERUM);

						return List.of(serum);
					}
				});
			})
			.setDefaultTemplate(ButterflyHelper::createDefaultTemplate);
	}

	@Override
	public void onFinishRegistration(IRootManager manager, IGeneticApiInstance instance) {
		ButterflyManager.butterflyRoot = instance.<IButterflySpeciesType>getRoot(ForestrySpeciesTypes.BUTTERFLY).get();
	}
}
