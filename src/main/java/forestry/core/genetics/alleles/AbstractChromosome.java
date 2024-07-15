package forestry.core.genetics.alleles;

import javax.annotation.Nullable;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;

public abstract class AbstractChromosome<A extends IAllele> implements IChromosome<A>, IInternalChromosome<A> {
	protected final ResourceLocation id;

	@Nullable
	protected Set<A> allowedAlleles;

	public AbstractChromosome(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public Set<A> getValidAlleles() {
		Set<A> allowedAlleles = this.allowedAlleles;
		if (allowedAlleles != null) {
			return allowedAlleles;
		} else {
			throw new IllegalStateException("Chromosome with ID " + this.id + " not initialized yet.");
		}
	}

	@Override
	public void setAllowedAlleles(Set<A> alleles) {
		if (this.allowedAlleles == null) {
			this.allowedAlleles = alleles;
		} else {
			throw new IllegalStateException("Allowed alleles have already been set for chromosome with ID " + this.id);
		}
	}

	@Override
	public String getTranslationKey(A allele) {
		ResourceLocation alleleId = allele.id();
		// ex: allele.forestry.bee_species.meadows
		return "allele." + alleleId.getNamespace() + '.' + this.id.getPath() + '.' + alleleId.getPath();
	}
}
