package forestry.core.commands;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import forestry.Forestry;
import forestry.api.IForestryApi;
import forestry.api.climate.IClimateManager;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.IGeneticManager;
import forestry.api.genetics.IMutation;
import forestry.api.genetics.ISpeciesType;
import forestry.modules.features.FeatureItem;
import forestry.storage.features.BackpackItems;
import forestry.storage.items.ItemBackpack;

// Used to dump currently registered Forestry data
public class DumpCommand {
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		return Commands.literal("dump")
				.then(Commands.literal("mutations").executes(DumpCommand::mutations))
				.then(Commands.literal("climates").executes(DumpCommand::climates))
				.then(Commands.literal("backpacks").executes(DumpCommand::backpacks));
	}

	// Dumps all registered mutations
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

	private static int climates(CommandContext<CommandSourceStack> ctx) {
		List<Holder.Reference<Biome>> biomes = ctx.getSource().getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).holders()
				.toList();
		Forestry.LOGGER.debug("Listing Forestry climates for {} biomes", biomes.size());

		IClimateManager manager = IForestryApi.INSTANCE.getClimateManager();
		Table<HumidityType, TemperatureType, List<ResourceLocation>> climates = HashBasedTable.create(HumidityType.VALUES.size(), TemperatureType.VALUES.size());

		for (Holder.Reference<Biome> biome : biomes) {
			HumidityType humidity = manager.getHumidity(biome);
			TemperatureType temperature = manager.getTemperature(biome);

			List<ResourceLocation> climateList = climates.get(humidity, temperature);
			if (climateList == null) {
				climates.put(humidity, temperature, climateList = new ArrayList<>());
			}
			climateList.add(biome.key().location());
		}

		for (TemperatureType temperature : TemperatureType.VALUES) {
			for (HumidityType humidity : HumidityType.VALUES) {
				List<ResourceLocation> climateList = climates.get(humidity, temperature);

				if (climateList == null || climateList.isEmpty()) {
					Forestry.LOGGER.error("No biomes found with climate ({} / {})", temperature, humidity);
				} else {
					Forestry.LOGGER.debug("Found {} biomes with climate ({} / {}): ", climateList.size(), temperature, humidity);

					for (ResourceLocation biomeId : climateList) {
						Forestry.LOGGER.debug("  > {}", biomeId);
					}
				}
			}
		}
		Forestry.LOGGER.debug("Finished listing Forestry climates.");

		return 1;
	}

	@SuppressWarnings("deprecation")
	private static int backpacks(CommandContext<CommandSourceStack> ctx) {
		// is there a reason not to hardcode this?
		@SuppressWarnings({"unchecked", "rawtypes"})
		List<FeatureItem<ItemBackpack>> definitions = (List) List.of(
				BackpackItems.MINER_BACKPACK,
				BackpackItems.DIGGER_BACKPACK,
				BackpackItems.FORESTER_BACKPACK,
				BackpackItems.HUNTER_BACKPACK,
				BackpackItems.ADVENTURER_BACKPACK,
				BackpackItems.BUILDER_BACKPACK
		);

		Forestry.LOGGER.debug("Listing Forestry backpack filters for {} backpack types (excluding Naturalist bags)", definitions.size());

		for (FeatureItem<ItemBackpack> backpack : definitions) {
			Predicate<ItemStack> filter = backpack.get().getDefinition().getFilter();
			ArrayList<Item> allowedItems = new ArrayList<>();

			for (Item item : ctx.getSource().registryAccess().registryOrThrow(Registry.ITEM_REGISTRY)) {
				if (filter.test(new ItemStack(item))) {
					allowedItems.add(item);
				}
			}

			Forestry.LOGGER.debug("The backpack definition {} allows {} items:", backpack.item().builtInRegistryHolder().key().location(), allowedItems.size());

			for (Item allowedItem : allowedItems) {
				Forestry.LOGGER.debug("  > {}", allowedItem.builtInRegistryHolder().key().location());
			}
		}

		return 1;
	}
}
