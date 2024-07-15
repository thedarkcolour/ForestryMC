package forestry.apiculture;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.genetics.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.BeeManager;
import forestry.api.genetics.ForestryComponentKeys;
import forestry.api.genetics.ForestrySpeciesType;
import forestry.api.genetics.IResearchHandler;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.apiculture.features.ApicultureItems;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.genetics.BeeDisplayHandler;
import forestry.apiculture.genetics.BeeHelper;
import forestry.apiculture.genetics.BeeSpeciesType;
import forestry.apiculture.items.ItemHoneyComb;
import forestry.core.genetics.root.IResearchPlugin;
import forestry.core.genetics.root.ResearchHandler;
import forestry.core.items.ItemOverlay;
import forestry.core.utils.ItemStackUtil;

import genetics.api.GeneticPlugin;
import genetics.api.IGeneticApiInstance;
import genetics.api.IGeneticFactory;
import genetics.api.IGeneticPlugin;

import forestry.api.genetics.alleles.IAlleleSpecies;

import genetics.api.individual.IIndividual;
import genetics.api.organism.IOrganismTypes;
import genetics.api.root.IGeneticListenerRegistry;
import genetics.api.root.IIndividualRootBuilder;
import genetics.api.root.IRootManager;
import genetics.api.root.components.ComponentKeys;

@GeneticPlugin(modId = ForestryConstants.MOD_ID)
public class BeePlugin implements IGeneticPlugin {

	@Override
	public void registerListeners(IGeneticListenerRegistry registry) {
		registry.add(ForestrySpeciesType.BEE, BeeDefinition.values());
	}

	@Override
	public void createRoot(IRootManager rootManager, IGeneticFactory geneticFactory) {
		IIndividualRootBuilder<IBee> rootBuilder = rootManager.createRoot(ForestrySpeciesType.BEE);
		rootBuilder
			.setRootFactory(BeeSpeciesType::new)
			.setSpeciesType(BeeChromosomes.SPECIES)
			.addListener(ComponentKeys.TYPES, (IOrganismTypes<IBee> builder) -> {
				builder.registerType(BeeLifeStage.DRONE, ApicultureItems.BEE_DRONE::stack);
				builder.registerType(BeeLifeStage.PRINCESS, ApicultureItems.BEE_PRINCESS::stack);
				builder.registerType(BeeLifeStage.QUEEN, ApicultureItems.BEE_QUEEN::stack);
				builder.registerType(BeeLifeStage.LARVAE, ApicultureItems.BEE_LARVAE::stack);
			})
			.addComponent(ComponentKeys.TRANSLATORS)
			.addComponent(ComponentKeys.MUTATIONS)
			.addComponent(ForestryComponentKeys.RESEARCH, ResearchHandler::new)
			.addListener(ForestryComponentKeys.RESEARCH, (IResearchHandler<IBee> builder) -> builder.addPlugin(new IResearchPlugin() {
				@Override
				public float getResearchSuitability(IAlleleSpecies species, ItemStack itemStack) {
					Item item = itemStack.getItem();
					if (item instanceof ItemOverlay && ApicultureItems.HONEY_DROPS.itemEqual(item)) {
						return 0.5f;
					} else if (ApicultureItems.HONEYDEW.itemEqual(item)) {
						return 0.7f;
						//TODO tag lookup?
					} else if (item instanceof ItemHoneyComb) {
						return 0.4f;
					}

					IAlleleBeeSpecies beeSpecies = (IAlleleBeeSpecies) species;
					for (ItemStack stack : beeSpecies.getProducts().getPossibleStacks()) {
						if (stack.sameItem(itemStack)) {
							return 1.0f;
						}
					}
					for (ItemStack stack : beeSpecies.getSpecialties().getPossibleStacks()) {
						if (stack.sameItem(itemStack)) {
							return 1.0f;
						}
					}

					return 0.0F;
				}

				@Override
				public NonNullList<ItemStack> getResearchBounty(IAlleleSpecies species, Level world, GameProfile researcher, IIndividual individual, int bountyLevel) {
					IAlleleBeeSpecies beeSpecies = (IAlleleBeeSpecies) species;
					NonNullList<ItemStack> bounty = NonNullList.create();
					if (bountyLevel > 10) {
						for (ItemStack stack : beeSpecies.getSpecialties().getPossibleStacks()) {
							bounty.add(ItemStackUtil.copyWithRandomSize(stack, (int) ((float) bountyLevel / 2), world.random));
						}
					}
					for (ItemStack stack : beeSpecies.getProducts().getPossibleStacks()) {
						bounty.add(ItemStackUtil.copyWithRandomSize(stack, (int) ((float) bountyLevel / 2), world.random));
					}
					return bounty;
				}
			}))
			.setDefaultTemplate(BeeHelper::createDefaultTemplate);
	}

	@Override
	public void onFinishRegistration(IRootManager manager, IGeneticApiInstance instance) {
		BeeManager.beeRoot = instance.getRoot(ForestrySpeciesType.BEE);

		BeeDisplayHandler.init(DisplayHelper.getInstance());
	}
}
