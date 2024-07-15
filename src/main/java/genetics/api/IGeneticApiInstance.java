package genetics.api;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.IAlleleRegistry;

import forestry.api.genetics.alleles.IAllele;
import genetics.api.alleles.IAlleleHelper;
import genetics.api.individual.IChromosomeList;
import genetics.api.root.IIndividualRootHelper;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.components.IRootComponentRegistry;

public interface IGeneticApiInstance {
	/**
	 * This instance of the allele registry can be used to get {@link IAllele}s.
	 * It's available after all alleles where registered at {@link IGeneticPlugin#registerAlleles(IAlleleRegistry)}.
	 *
	 * @throws IllegalStateException if the method gets called before {@link IGeneticPlugin#registerAlleles(IAlleleRegistry)}  was called at all plugins.
	 */
	IAlleleRegistry getAlleleRegistry();

	IAlleleHelper getAlleleHelper();

	/**
	 * This instance is available before any method of a {@link IGeneticPlugin} was called.
	 *
	 * @throws IllegalStateException if the method gets called before the pre-init phase of fml.
	 */
	IGeneticSaveHandler getSaveHandler();

	/**
	 * This instance is available before any method of a {@link IGeneticPlugin} was called.
	 *
	 * @throws IllegalStateException if the method gets called before the pre-init phase of fml.
	 */
	IIndividualRootHelper getRootHelper();

	/**
	 * This instance is available before any method of a {@link IGeneticPlugin} was called.
	 *
	 * @throws IllegalStateException if the method gets called before the pre-init phase of fml.
	 */
	IRootComponentRegistry getComponentRegistry();

	<R extends ISpeciesType<?>> R getRoot(ResourceLocation rootUID);

	IChromosomeList getChromosomeList(String rootUID);

	/**
	 * @return A map that contains every root definition that was created by calling {@link #getRoot(ResourceLocation)}.
	 */
	Map<String, ISpeciesType<?>> getRoots();

	/**
	 * @return Checks if the genetics mod is present.
	 */
	boolean isModPresent();
}
