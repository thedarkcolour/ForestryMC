package forestry.api.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

public record ValueAllele<V>(ResourceLocation id, V value, boolean dominant) implements IValueAllele<V> {
}
