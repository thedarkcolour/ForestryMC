package forestry.apiculture.blocks;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.apiculture.ForestryBeeSpecies;
import forestry.api.core.IBlockSubtype;

/* Forestry Hive Names */
public enum BlockHiveType implements IBlockSubtype {
	FOREST(ForestryBeeSpecies.FOREST),
	MEADOWS(ForestryBeeSpecies.MEADOWS),
	DESERT(ForestryBeeSpecies.MODEST),
	JUNGLE(ForestryBeeSpecies.TROPICAL),
	END(ForestryBeeSpecies.ENDED),
	SNOW(ForestryBeeSpecies.WINTRY),
	SWAMP(ForestryBeeSpecies.MARSHY),
	SWARM(ForestryConstants.forestry("none"));

	public static final BlockHiveType[] VALUES = values();

	private final ResourceLocation speciesUid;

	BlockHiveType(ResourceLocation speciesUid) {
		this.speciesUid = speciesUid;
	}

	public ResourceLocation getSpeciesId() {
		return this.speciesUid;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public int getMeta() {
		return ordinal();
	}
}
