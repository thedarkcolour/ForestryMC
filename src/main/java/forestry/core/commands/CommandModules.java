/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import forestry.api.IForestryApi;
import forestry.api.modules.ForestryModule;
import forestry.api.modules.IForestryModule;

import genetics.commands.CommandHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class CommandModules {
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return LiteralArgumentBuilder.<CommandSourceStack>literal("module")
				.then(CommandPluginsInfo.register())
				.executes(CommandModules::listModulesForSender);
	}

	private static int listModulesForSender(CommandContext<CommandSourceStack> context) {
		StringBuilder pluginList = new StringBuilder();
		for (IForestryModule module : IForestryApi.INSTANCE.getModuleManager().getLoadedModules()) {
			if (!pluginList.isEmpty()) {
				pluginList.append(", ");
			}
			pluginList.append(module.getId());
		}
		CommandHelpers.sendChatMessage(context.getSource(), Component.literal(pluginList.toString()));

		return 1;
	}

	public static class CommandPluginsInfo {
		public static ArgumentBuilder<CommandSourceStack, ?> register() {
			return Commands.literal("info")
					.then(Commands.argument("module", ModuleArgument.modules())
							.executes(CommandPluginsInfo::listModuleInfoForSender));
		}

		public static class ModuleArgument implements ArgumentType<IForestryModule> {
			@Override
			public IForestryModule parse(StringReader reader) throws CommandSyntaxException {
				String pluginUid = reader.readUnquotedString();

				IForestryModule found = null;
				for (IForestryModule module : IForestryApi.INSTANCE.getModuleManager().getLoadedModules()) {
					ResourceLocation id = module.getId();
					if (id.toString().equalsIgnoreCase(pluginUid)) {
						found = module;
						break;
					}
				}

				if (found != null) {
					return found;
				} else {
					throw new SimpleCommandExceptionType(Component.translatable("for.chat.modules.error", pluginUid)).createWithContext(reader);
				}

			}

			public static ModuleArgument modules() {
				return new ModuleArgument();
			}

			@Override
			public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
				for (IForestryModule module : IForestryApi.INSTANCE.getModuleManager().getLoadedModules()) {
					builder.suggest(module.getId().toString());
				}

				return builder.buildFuture();
			}
		}

		private static int listModuleInfoForSender(CommandContext<CommandSourceStack> context) throws CommandRuntimeException {
			IForestryModule found = context.getArgument("module", IForestryModule.class);

			ChatFormatting formatting = ChatFormatting.GREEN;

			ForestryModule info = found.getClass().getAnnotation(ForestryModule.class);
			if (info != null) {
				CommandSourceStack sender = context.getSource();

				// todo replacement
/*

				CommandHelpers.sendChatMessage(sender, Component.literal("Module: " + info.name()).withStyle(formatting));
				if (!info.version().isEmpty()) {
					CommandHelpers.sendChatMessage(sender, Component.literal("Version: " + info.version()).withStyle(ChatFormatting.BLUE));
				}
				if (!info.author().isEmpty()) {
					CommandHelpers.sendChatMessage(sender, Component.literal("Author(s): " + info.author()).withStyle(ChatFormatting.BLUE));
				}
				if (!info.unlocalizedDescription().isEmpty()) {
					CommandHelpers.sendChatMessage(sender, Component.translatable(info.unlocalizedDescription()));
				}
*/

				return 1;
			}

			return 0;
		}
	}
}
