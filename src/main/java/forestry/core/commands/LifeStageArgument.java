package forestry.core.commands;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

public class LifeStageArgument implements ISpeciesArgumentType<ILifeStage> {
	private static final DynamicCommandExceptionType INVALID_VALUE = new DynamicCommandExceptionType(found -> Component.translatable("argument.enum.invalid", found));

	private final ImmutableMap<String, ILifeStage> stages;
	private final ISpeciesType<?, ?> type;

	public LifeStageArgument(ISpeciesType<?, ?> type) {
		ImmutableMap.Builder<String, ILifeStage> builder = ImmutableMap.builderWithExpectedSize(type.getLifeStages().size());

		for (ILifeStage stage : type.getLifeStages()) {
			builder.put(stage.getSerializedName(), stage);
		}

		this.stages = builder.build();
		this.type = type;
	}

	@Override
	public ILifeStage parse(StringReader reader) throws CommandSyntaxException {
		String s = reader.readUnquotedString();
		ILifeStage stage = this.stages.get(s);

		if (stage == null) {
			throw INVALID_VALUE.create(s);
		} else {
			return stage;
		}
	}

	@Override
	public ISpeciesType<?, ?> getType() {
		return this.type;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(this.stages.keySet(), builder);
	}

	@Override
	public Collection<String> getExamples() {
		return this.stages.keySet().stream().limit(2L).toList();
	}
}
