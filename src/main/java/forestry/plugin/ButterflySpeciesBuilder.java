package forestry.plugin;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.core.Product;
import forestry.api.lepidopterology.genetics.IButterflySpecies;
import forestry.api.lepidopterology.genetics.IButterflySpeciesType;
import forestry.api.plugin.IButterflySpeciesBuilder;
import forestry.lepidopterology.ButterflySpecies;

public class ButterflySpeciesBuilder extends SpeciesBuilder<IButterflySpeciesType, IButterflySpecies, IButterflySpeciesBuilder> implements IButterflySpeciesBuilder {
	private float flightDistance = 5.0f;
	private int serumColor;
	private boolean nocturnal;
	private boolean moth;
	private float rarity = 0.1f;
	@Nullable
	private TagKey<Biome> spawnBiomes = null;
	@Nullable
	private ResourceLocation itemTexture;
	@Nullable
	private ResourceLocation entityTexture;

	public ButterflySpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	public IButterflySpeciesBuilder setSerumColor(Color color) {
		this.serumColor = color.getRGB();
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setFlightDistance(float flightDistance) {
		this.flightDistance = flightDistance;
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setNocturnal(boolean nocturnal) {
		this.nocturnal = nocturnal;
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setMoth(boolean moth) {
		this.moth = moth;
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setSpawnBiomes(TagKey<Biome> biomeTag) {
		this.spawnBiomes = biomeTag;
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setRarity(float rarity) {
		if (rarity < 0 || rarity > 1) {
			throw new IllegalArgumentException("Invalid rarity " + rarity + " - must be within [0,1]");
		}
		this.rarity = rarity;
		return this;
	}

	@Override
	public IButterflySpeciesBuilder setTexture(String texturePath) {
		return null;
	}

	@Override
	public int getSerumColor() {
		return this.serumColor;
	}

	@Override
	public float getFlightDistance() {
		return this.flightDistance;
	}

	@Override
	public boolean isNocturnal() {
		return this.nocturnal;
	}

	@Override
	public boolean isMoth() {
		return this.moth;
	}

	@Nullable
	@Override
	public TagKey<Biome> getSpawnBiomes() {
		return this.spawnBiomes;
	}

	@Override
	public float getRarity() {
		return this.rarity;
	}

	@Override
	public List<Product> buildProducts() {
		return null;
	}

	@Override
	public List<Product> buildCaterpillarProducts() {
		return null;
	}

	@Override
	public ISpeciesFactory<IButterflySpeciesType, IButterflySpecies, IButterflySpeciesBuilder> createSpeciesFactory() {
		return ButterflySpecies::new;
	}

	@Override
	public ResourceLocation getItemTexture() {
		return this.itemTexture == null ? new ResourceLocation(id.getNamespace(), "item/butterfly/" + this.id.getPath()) : this.itemTexture;
	}

	@Override
	public ResourceLocation getEntityTexture() {
		return this.entityTexture == null ? new ResourceLocation(id.getNamespace(), "textures/entity/butterfly/" + this.id.getPath()) : this.entityTexture;
	}
}
