package forestry.core.commands;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.fml.loading.FMLEnvironment;

import forestry.api.IForestryApi;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.core.utils.Translator;

// Test command to make sure things aren't broken after the API rewrite.
public class DiagnosticsCommand {
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("diagnostics")
				.executes(DiagnosticsCommand::diagnostics);
	}

	private static int diagnostics(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();

		source.sendSystemMessage(Component.literal("Running Forestry diagnostics..."));

		IGeneticManager genetics = IForestryApi.INSTANCE.getGeneticManager();
		ISpeciesType<?, ?>[] types = genetics.getSpeciesTypes().toArray(ISpeciesType<?, ?>[]::new);
		source.sendSystemMessage(Component.literal("Found " + types.length + " species types."));

		for (ISpeciesType<?, ?> type : types) {
			List<? extends ISpecies<?>> allSpecies = type.getAllSpecies();
			source.sendSystemMessage(Component.literal("Diagnostics for species type: " + type.id() + " has " + allSpecies.size() + " registered species"));

			for (ISpecies<?> species : allSpecies) {
				boolean errors = false;

				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (!Translator.canTranslateToLocal(species.getTranslationKey())) {
						source.sendSystemMessage(Component.literal(species.id().toString()));
						errors = true;
						source.sendSystemMessage(Component.literal("  > Translated name: ").append(Component.literal("MISSING").withStyle(ChatFormatting.RED)));
					}
					if (!Translator.canTranslateToLocal(species.getDescriptionTranslationKey())) {
						if (!errors) {
							source.sendSystemMessage(Component.literal(species.id().toString()));
							errors = true;
						}
						source.sendSystemMessage(Component.literal("  > Description: ").append(Component.literal("MISSING").withStyle(ChatFormatting.RED)));
					}
				}
			}
		}

		return 0;
	}
}
