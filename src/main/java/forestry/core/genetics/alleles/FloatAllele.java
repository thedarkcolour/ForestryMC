package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.genetics.alleles.IFloatAllele;

record FloatAllele(ResourceLocation alleleId, float value, boolean dominant) implements IFloatAllele {
	FloatAllele(float value, boolean dominant) {
		this(createId(value, dominant), value, dominant);
	}

	private static ResourceLocation createId(float value, boolean dominant) {
		return ForestryConstants.forestry(value + (dominant ? "fd" : "f"));
	}
}
