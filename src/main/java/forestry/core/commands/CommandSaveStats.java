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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraftforge.fml.ModList;

import forestry.api.genetics.IBreedingTracker;
import forestry.core.config.Constants;
import forestry.core.proxy.Proxies;
import forestry.core.utils.Log;
import forestry.core.utils.StringUtil;
import forestry.core.utils.Translator;

import genetics.api.GeneticsAPI;
import genetics.api.alleles.IAlleleSpecies;
import genetics.commands.CommandHelpers;

public final class CommandSaveStats implements Command<CommandSourceStack> {

	private static final Component discoveredSymbol;
	private static final Component blacklistedSymbol;
	private static final Component notCountedSymbol;

	static {
		discoveredSymbol = Component.translatable("for.chat.command.forestry.stats.save.key.discovered.symbol");
		blacklistedSymbol = Component.translatable("for.chat.command.forestry.stats.save.key.blacklisted.symbol");
		notCountedSymbol = Component.translatable("for.chat.command.forestry.stats.save.key.notCounted.symbol");
	}

	private final IStatsSaveHelper saveHelper;
	private final ICommandModeHelper modeHelper;

	public CommandSaveStats(IStatsSaveHelper saveHelper, ICommandModeHelper modeHelper) {
		this.saveHelper = saveHelper;
		this.modeHelper = modeHelper;
	}

	public static ArgumentBuilder<CommandSourceStack, ?> register(IStatsSaveHelper saveHelper, ICommandModeHelper modeHelper) {
		return Commands.literal("save")
				.then(Commands.argument("player", EntityArgument.player())
						.executes(new CommandSaveStats(saveHelper, modeHelper)));

	}

	public int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		String newLine = System.getProperty("line.separator");

		ServerPlayer player = EntityArgument.getPlayer(ctx, "player");

		Level world = ctx.getSource().getLevel();

		Collection<String> statistics = new ArrayList<>();

		String date = DateFormat.getInstance().format(new Date());
		statistics.add(Component.translatable(saveHelper.getUnlocalizedSaveStatsString(), player.getDisplayName(), date).getString());
		statistics.add("");
		statistics.add(Component.translatable("for.chat.command.forestry.stats.save.mode", modeHelper.getModeName(world)).getString());
		statistics.add("");

		IBreedingTracker tracker = saveHelper.getBreedingTracker(world, player.getGameProfile());
		saveHelper.addExtraInfo(statistics, tracker);

		Collection<? extends IAlleleSpecies> species = saveHelper.getSpecies();

		String speciesCount = Component.translatable("for.gui.speciescount").getString();
		String speciesCountLine = String.format("%s (%s):", speciesCount, species.size());
		statistics.add(speciesCountLine);
		statistics.add(StringUtil.line(speciesCountLine.length()));

		statistics.add(discoveredSymbol + ": " + Component.translatable("for.chat.command.forestry.stats.save.key.discovered"));
		statistics.add(blacklistedSymbol + ": " + Component.translatable("for.chat.command.forestry.stats.save.key.blacklisted"));
		statistics.add(notCountedSymbol + ": " + Component.translatable("for.chat.command.forestry.stats.save.key.notCounted"));
		statistics.add("");

		String header = generateSpeciesListHeader();
		statistics.add(header);

		statistics.add(StringUtil.line(header.length()));
		statistics.add("");

		for (IAlleleSpecies allele : species) {
			statistics.add(generateSpeciesListEntry(allele, tracker));
		}

		File file = new File(Proxies.common.getForestryRoot(), "config/" + Constants.MOD_ID + "/stats/" + player.getDisplayName().getString() + '-' + saveHelper.getFileSuffix() + ".log");
		try {
			File folder = file.getParentFile();
			if (folder != null && !folder.exists()) {
				boolean success = file.getParentFile().mkdirs();
				if (!success) {
					CommandHelpers.sendLocalizedChatMessage(ctx.getSource(), "for.chat.command.forestry.stats.save.error1");
					return 0;
				}
			}

			if (!file.exists() && !file.createNewFile()) {
				CommandHelpers.sendLocalizedChatMessage(ctx.getSource(), "for.chat.command.forestry.stats.save.error1");
				return 0;
			}

			if (!file.canWrite()) {
				CommandHelpers.sendLocalizedChatMessage(ctx.getSource(), "for.chat.command.forestry.stats.save.error2");
				return 0;
			}

			FileOutputStream fileout = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileout, StandardCharsets.UTF_8));

			writer.write("# " + Constants.MOD_ID + newLine + "# " + ModList.get().getModContainerById(Constants.MOD_ID).get().getModInfo().getVersion() + newLine);

			for (String line : statistics) {
				writer.write(line + newLine);
			}

			writer.close();

		} catch (IOException ex) {
			CommandHelpers.sendLocalizedChatMessage(ctx.getSource(), "for.chat.command.forestry.stats.save.error3");
			Log.error(Component.translatable("for.for.chat.command.forestry.stats.save.error3").getString(), ex);
			return 0;
		}

		CommandHelpers.sendLocalizedChatMessage(ctx.getSource(), "for.chat.command.forestry.stats.save.saved", player.getDisplayName());

		return 1;
	}

	private static String generateSpeciesListHeader() {
		Component authority = Component.translatable("for.gui.alyzer.authority");
		Component species = Component.translatable("for.gui.species");
		return speciesListEntry(discoveredSymbol, blacklistedSymbol, notCountedSymbol, "UID", species, authority.getString());
	}

	private static String generateSpeciesListEntry(IAlleleSpecies species, IBreedingTracker tracker) {
		Component discovered = Component.empty();
		if (tracker.isDiscovered(species)) {
			discovered = discoveredSymbol;
		}

		Component blacklisted = Component.empty();
		if (GeneticsAPI.apiInstance.getAlleleRegistry().isBlacklisted(species.getRegistryName())) {
			blacklisted = blacklistedSymbol;
		}

		Component notCounted = Component.empty();
		if (!species.isDominant()) {
			notCounted = notCountedSymbol;
		}

		return speciesListEntry(discovered, blacklisted, notCounted, species.getRegistryName().toString(), species.getDisplayName(), species.getAuthority());
	}

	private static String speciesListEntry(Component discovered, Component blacklisted, Component notCounted, String UID, Component speciesName, String authority) {
		return String.format("[ %-2s ] [ %-2s ] [ %-2s ]\t%-40s %-20s %-20s", discovered, blacklisted, notCounted, UID, speciesName, authority);
	}
}
