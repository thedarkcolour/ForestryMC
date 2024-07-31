package forestry.apiculture;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;

// todo see if there's other code like this and call it BeeVariant ?
public record VillageHive(ResourceLocation speciesId, Map<IChromosome<?>, IAllele> alleles) {
}
