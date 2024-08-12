package forestry.core.commands;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
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

public class SpeciesArgument implements ISpeciesArgumentType<ISpecies<?>> {
	private final ISpeciesType<?, ?> type;
	private final Collection<String> examples;

	public SpeciesArgument(ISpeciesType<?, ?> type) {
		this.type = type;

		// Generate 3 random examples
		this.examples = new ArrayList<>(3);

		Random rand = new Random();
		ImmutableList<ResourceLocation> ids = type.getAllSpeciesIds().asList();

		// if there are fewer than 3 registered species, don't loop forever
		int numExamples = Math.min(3, ids.size());

		while (this.examples.size() < numExamples) {
			String example = ids.get(rand.nextInt(ids.size())).toString();

			if (!this.examples.contains(example)) {
				this.examples.add(example);
			}
		}
	}

	@Override
	public ISpeciesType<?, ?> getType() {
		return this.type;
	}

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

	@Override
	public Collection<String> getExamples() {
		return this.examples;
	}
}
