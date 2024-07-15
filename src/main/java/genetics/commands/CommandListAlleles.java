package genetics.commands;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import genetics.api.GeneticsAPI;
import forestry.api.genetics.alleles.ChromosomePair;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.IGenome;
import genetics.api.individual.IIndividual;

public class CommandListAlleles {

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("listAlleles").requires(PermLevel.ADMIN).executes(CommandListAlleles::execute);
	}

	public static int execute(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
		Player player = context.getSource().getPlayerOrException();

		ItemStack stack = player.getMainHandItem();

		IIndividual individual = GeneticsAPI.apiInstance.getRootHelper().getIndividual(stack);
		if (individual == null) {
			return 0;
		}

		IGenome genome = individual.getGenome();

		for (ChromosomePair chromosome : genome.getChromosomes()) {
			IChromosome type = chromosome.type();

			CommandHelpers.sendChatMessage(context.getSource(), Component.literal(type.getId() + ": ").append(genome.getActiveAllele(type).getDisplayName()).append(Component.literal(" ")).append(genome.getInactiveAllele(type).getDisplayName()));
		}

		GeneticsAPI.apiInstance.getAlleleRegistry().getRegisteredAlleles().forEach(a -> System.out.println(a.id() + ": " + a.getDisplayName().getString()));

		return 1;
	}
}
