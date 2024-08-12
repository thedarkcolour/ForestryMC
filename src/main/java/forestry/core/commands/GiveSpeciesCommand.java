package forestry.core.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;

public class GiveSpeciesCommand {
	public static LiteralArgumentBuilder<CommandSourceStack> register(ISpeciesType<?, ?> type) {
		return Commands.literal("give").requires(CommandHelpers.ADMIN)
				.then(Commands.argument("species", new SpeciesArgument(type))
						.then(Commands.argument("stage", new LifeStageArgument(type))
								.then(Commands.argument("player", EntityArgument.player())
										.executes(a -> execute(a.getSource(), a.getArgument("species", ISpecies.class), a.getArgument("stage", ILifeStage.class), EntityArgument.getPlayer(a, "player"))))
								.executes(a -> execute(a.getSource(), a.getArgument("species", ISpecies.class), a.getArgument("stage", ILifeStage.class), a.getSource().getPlayerOrException())))
						.executes(a -> execute(a.getSource(), a.getArgument("species", ISpecies.class), type.getDefaultStage(), a.getSource().getPlayerOrException())))
				.executes(a -> execute(a.getSource(), type.getDefaultSpecies(), type.getDefaultStage(), a.getSource().getPlayerOrException()));
	}

	public static int execute(CommandSourceStack source, ISpecies<?> species, ILifeStage stage, Player player) {
		ItemStack stack = species.createStack(stage);
		Component displayName = stack.getDisplayName();
		player.getInventory().add(stack);

		CommandHelpers.sendLocalizedChatMessage(source, "for.chat.command.forestry.bee.give.given", player.getName(), displayName);
		return 1;
	}
}
