package forestry.core.commands;

import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import forestry.api.IForestryApi;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;

public record AlleleArgument(ISpeciesType<?, ?> type) implements ISpeciesArgumentType<IAllele> {
	@Override
	public IAllele parse(StringReader reader) throws CommandSyntaxException {
		ResourceLocation id = ResourceLocation.read(reader);
		IAllele allele = IForestryApi.INSTANCE.getAlleleManager().getAllele(id);

		if (allele == null) {
			throw LifeStageArgument.INVALID_VALUE.create(id);
		} else {
			return allele;
		}
	}
}
