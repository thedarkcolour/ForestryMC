package forestry.core.commands;

import java.util.List;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import forestry.Forestry;
import forestry.api.IForestryApi;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesType;

// Used to dump currently registered Forestry data
public class DumpCommand {
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("dump")
				.then(Commands.literal("mutations").executes(DumpCommand::mutations));
	}

	private static int mutations(CommandContext<CommandSourceStack> ctx) {
		IGeneticManager genetics = IForestryApi.INSTANCE.getGeneticManager();

		for (ISpeciesType<?, ?> type : genetics.getSpeciesTypes()) {
			List<? extends IMutation<?>> mutations = type.getMutations().getAllMutations();
			Forestry.LOGGER.debug("There are {} registered mutations for species type {}", mutations.size(), type.id());

			for (IMutation<?> mutation : mutations) {
				Forestry.LOGGER.debug("> {} + {} = {}", mutation.getFirstParent().id(), mutation.getSecondParent().id(), mutation.getResult().id());
			}
			Forestry.LOGGER.debug("=======");
		}
		return 1;
	}
}
