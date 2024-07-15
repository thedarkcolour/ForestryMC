package forestry.core.genetics.alleles;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.genetics.alleles.IBooleanAllele;

public record BooleanAllele(ResourceLocation id, boolean value, boolean dominant) implements IBooleanAllele {
	BooleanAllele(boolean value, boolean dominant) {
		this(createId(value, dominant), value, dominant);
	}

	private static ResourceLocation createId(boolean value, boolean dominant) {
		return new ResourceLocation(ForestryConstants.MOD_ID, dominant ? Boolean.toString(value) + 'd' : Boolean.toString(value));
	}
}
