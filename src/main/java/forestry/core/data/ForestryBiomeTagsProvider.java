package forestry.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;

import net.minecraftforge.common.data.ExistingFileHelper;

import forestry.api.ForestryConstants;
import forestry.api.ForestryTags;

import org.jetbrains.annotations.Nullable;

public class ForestryBiomeTagsProvider extends BiomeTagsProvider {
	public ForestryBiomeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper helper) {
		super(generator, ForestryConstants.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(ForestryTags.Biomes.WARM_TEMPERATURE)
				.add(Biomes.WOODED_BADLANDS)
				.add(Biomes.SAVANNA)
				.add(Biomes.SAVANNA_PLATEAU)
				.add(Biomes.WINDSWEPT_SAVANNA);

		tag(ForestryTags.Biomes.HELLISH_TEMPERATURE)
				.add(Biomes.CRIMSON_FOREST)
				.add(Biomes.WARPED_FOREST)
				.add(Biomes.BASALT_DELTAS)
				.add(Biomes.NETHER_WASTES)
				.add(Biomes.SOUL_SAND_VALLEY);
	}
}
