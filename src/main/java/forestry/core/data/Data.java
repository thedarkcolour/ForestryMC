package forestry.core.data;

import net.minecraft.data.DataGenerator;

import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import forestry.api.IForestryApi;
import forestry.apiimpl.plugin.PluginManager;
import forestry.core.data.models.ForestryBlockStateProvider;
import forestry.core.data.models.ForestryItemModelProvider;
import forestry.core.data.models.ForestryWoodModelProvider;
import forestry.modules.ForestryModuleManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		ForestryBlockTagsProvider blockTagsProvider = new ForestryBlockTagsProvider(generator, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTagsProvider);
		generator.addProvider(event.includeServer(), new ForestryAdvancementProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryBackpackTagProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryBiomeTagsProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryFluidTagsProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryPoiTypeTagProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryLootTableProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryMachineRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryLootModifierProvider(generator));

		generator.addProvider(event.includeClient(), new ForestryBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new ForestryWoodModelProvider(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new ForestryItemModelProvider(generator, existingFileHelper));
	}

	// Hack fix to make API work in data generation environment
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void setupDataGenApi(GatherDataEvent event) {
		((ForestryModuleManager) IForestryApi.INSTANCE.getModuleManager()).setupApi();

		PluginManager.registerClient();
	}
}
