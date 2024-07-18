package forestry.api.apiculture.hives;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;

import forestry.api.ForestryConstants;
import forestry.api.core.IBlockSubtype;

/* Forestry Hive Names */
public enum HiveType implements IBlockSubtype {
	FOREST("forest", "forestry:species_forest"),
	MEADOWS("meadows", "forestry:species_meadows"),
	DESERT("desert", "forestry:species_modest"),
	JUNGLE("jungle", "forestry:species_tropical"),
	END("end", "forestry:species_ended"),
	SNOW("snow", "forestry:species_wintry"),
	SWAMP("swamp", "forestry:species_marshy"),
	SWARM("swarm", "forestry:species_forest");

	public static final HiveType[] VALUES = values();

	private final ResourceLocation id;
	private final String speciesUid;

	HiveType(String id, String speciesUid) {
		this.id = ForestryConstants.forestry(id);
		this.speciesUid = speciesUid;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public String getSpeciesUid() {
		return speciesUid;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public int getMeta() {
		return ordinal();
	}
}
