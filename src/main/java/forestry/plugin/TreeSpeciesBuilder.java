package forestry.plugin;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.arboriculture.ITreeGenerator;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.plugin.ITreeSpeciesBuilder;
import forestry.arboriculture.TreeSpecies;
import forestry.arboriculture.worldgen.DefaultTreeGenerator;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

public class TreeSpeciesBuilder extends SpeciesBuilder<ITreeSpeciesType, ITreeSpecies, ITreeSpeciesBuilder> implements ITreeSpeciesBuilder {
	@Nullable
	private IWoodType woodType = null;
	@Nullable
	private ITreeGenerator generator = null;
	private float rarity = 1.0f;
	private boolean hasFruitLeaves = false;
	private final ReferenceOpenHashSet<BlockState> vanillaStates = new ReferenceOpenHashSet<>();
	private ItemStack decorativeLeaves = ItemStack.EMPTY;
	private int color;

	public TreeSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	public ISpeciesFactory<ITreeSpeciesType, ITreeSpecies, ITreeSpeciesBuilder> createSpeciesFactory() {
		return TreeSpecies::new;
	}

	@Override
	public ITreeSpeciesBuilder setWoodType(IWoodType woodType) {
		this.woodType = woodType;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setTreeFeature(Function<ITreeGenData, Feature<NoneFeatureConfiguration>> factory) {
		Preconditions.checkState(this.woodType != null, "Must call setWoodType before setTreeFeature");

		return setGenerator(new DefaultTreeGenerator(factory, this.woodType));
	}

	@Override
	public ITreeSpeciesBuilder setGenerator(ITreeGenerator generator) {
		this.generator = generator;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder addVanillaStates(List<BlockState> states) {
		this.vanillaStates.addAll(states);
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setHasFruitLeaves(boolean hasFruitLeaves) {
		this.hasFruitLeaves = hasFruitLeaves;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setRarity(float rarity) {
		this.rarity = rarity;
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setEscritoireColor(Color color) {
		this.color = color.getRGB();
		return this;
	}

	@Override
	public ITreeSpeciesBuilder setDecorativeLeaves(ItemStack stack) {
		this.decorativeLeaves = stack;
		return this;
	}

	@Nullable
	@Override
	public ITreeGenerator getGenerator() {
		return this.generator;
	}

	@Override
	public List<BlockState> getVanillaLeafStates() {
		return List.copyOf(this.vanillaStates);
	}

	@Override
	public ItemStack getDecorativeLeaves() {
		return this.decorativeLeaves;
	}

	@Override
	public boolean hasFruitLeaves() {
		return this.hasFruitLeaves;
	}

	@Override
	public float getRarity() {
		return this.rarity;
	}

	@Override
	public int getColor() {
		return this.color;
	}
}
