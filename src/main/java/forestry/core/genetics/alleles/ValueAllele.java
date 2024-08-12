package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.alleles.IValueAllele;

public record ValueAllele<V>(ResourceLocation alleleId, V value, boolean dominant) implements IValueAllele<V> {
}
