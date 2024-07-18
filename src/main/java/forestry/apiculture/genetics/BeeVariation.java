package forestry.apiculture.genetics;

import forestry.api.apiculture.BeeManager;
import net.minecraft.world.item.ItemStack;

import genetics.api.alleles.IAlleleTemplate;
import forestry.api.genetics.IGenome;

import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IChromosome;

public abstract class BeeVariation implements IBeeDefinition {

	private final IAlleleTemplate template;
	private final IGenome genome;

	protected BeeVariation(IBeeDefinition bee) {
		template = initializeTemplate(bee.getTemplate());
		genome = template.toGenome();
	}

	protected abstract IAlleleTemplate initializeTemplate(IAlleleTemplate template);

	@Override
	public IAlleleTemplate getTemplate() {
		return template;
	}

	@Override
	public IGenome getGenome() {
		return genome;
	}

	@Override
	public IBee createIndividual() {
		return template.toIndividual(BeeManager.beeRoot);
	}

	@Override
	public final ItemStack getMemberStack(BeeLifeStage beeType) {
		IBee bee = createIndividual();
		return BeeManager.beeRoot.getTypes().createStack(bee, beeType);
	}

	@Override
	public ISpeciesType<IBee> getSpecies() {
		return genome.getActiveAllele(BeeChromosomes.SPECIES);
	}

	public static class RainResist extends BeeVariation {
		public RainResist(IBeeDefinition bee) {
			super(bee);
		}

		@Override
		protected IAlleleTemplate initializeTemplate(IAlleleTemplate template) {
			return template.createBuilder().set(BeeChromosomes.TOLERATES_RAIN, true).build();
		}
	}
}
