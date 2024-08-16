package forestry.core.commands;

import java.util.concurrent.CompletableFuture;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

public record SpeciesArgument(ISpeciesType<?, ?> type) implements ISpeciesArgumentType<ISpecies<?>> {
	@Override
	public ISpecies<?> parse(final StringReader reader) throws CommandSyntaxException {
		ResourceLocation location = ResourceLocation.read(reader);

		ISpecies<?> species = this.type.getSpeciesSafe(location);

		if (species != null) {
			return species;
		} else {
			throw new SimpleCommandExceptionType(new LiteralMessage("Invalid Bee Type: " + location)).create();
		}
	}

	@Override
	public <SOURCE> CompletableFuture<Suggestions> listSuggestions(final CommandContext<SOURCE> context, final SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggestResource(this.type.getAllSpeciesIds(), builder);
	}
}
