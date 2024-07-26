package forestry.arboriculture.models;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.datafixers.util.Pair;

import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import forestry.api.IForestryApi;
import forestry.api.arboriculture.ForestryTreeSpecies;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeSpeciesType;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.tiles.TileSapling;
import forestry.core.utils.SpeciesUtil;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public class ModelSapling implements IUnbakedGeometry<ModelSapling> {
	private final Reference2ObjectOpenHashMap<ITreeSpecies, Pair<ResourceLocation, ResourceLocation>> modelsBySpecies;

	public ModelSapling() {
		this.modelsBySpecies = new Reference2ObjectOpenHashMap<>();

		for (ITreeSpecies species : SpeciesUtil.getAllTreeSpecies()) {
			// todo separate the model location code from the main species
			this.modelsBySpecies.put(species, Pair.of(species.getBlockModel(), species.getItemModel()));
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
		ImmutableMap.Builder<ITreeSpecies, BakedModel> itemModels = new ImmutableMap.Builder<>();
		ImmutableMap.Builder<ITreeSpecies, BakedModel> blockModels = new ImmutableMap.Builder<>();
		for (Map.Entry<ITreeSpecies, Pair<ResourceLocation, ResourceLocation>> entry : this.modelsBySpecies.reference2ObjectEntrySet()) {
			BakedModel blockModel = bakery.bake(entry.getValue().getFirst(), BlockModelRotation.X0_Y0, spriteGetter);
			if (blockModel != null) {
				blockModels.put(entry.getKey(), blockModel);
			}
			BakedModel itemModel = bakery.bake(entry.getValue().getSecond(), BlockModelRotation.X0_Y0, spriteGetter);
			if (itemModel != null) {
				itemModels.put(entry.getKey(), itemModel);
			}
		}
		return new Baked(itemModels.build(), blockModels.build());
	}

	public Collection<ResourceLocation> getDependencies() {
		return modelsBySpecies.values().stream()
				.flatMap(pair -> Stream.of(pair.getFirst(), pair.getSecond())).collect(Collectors.toSet());
	}

	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return getDependencies().stream()
				.flatMap(location -> modelGetter.apply(location).getMaterials(modelGetter, missingTextureErrors).stream())
				.collect(Collectors.toSet());
	}

	public static class Baked implements BakedModel {
		private final ImmutableMap<ITreeSpecies, BakedModel> itemModels;
		private final ImmutableMap<ITreeSpecies, BakedModel> blockModels;
		private final BakedModel defaultBlock;
		private final BakedModel defaultItem;
		@Nullable
		private ItemOverrides overrideList;

		public Baked(ImmutableMap<ITreeSpecies, BakedModel> itemModels, ImmutableMap<ITreeSpecies, BakedModel> blockModels) {
			this.itemModels = itemModels;
			this.blockModels = blockModels;
			ITreeSpeciesType speciesType = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.TREE);
			this.defaultBlock = Objects.requireNonNull(blockModels.get(speciesType.getSpecies(ForestryTreeSpecies.OAK)));
			this.defaultItem = Objects.requireNonNull(itemModels.get(speciesType.getSpecies(ForestryTreeSpecies.OAK)));
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
			ITreeSpecies species = extraData.get(TileSapling.TREE_SPECIES);
			if (species == null) {
				species = SpeciesUtil.getTreeSpecies(ForestryTreeSpecies.OAK);
			}
			return blockModels.get(species).getQuads(state, side, rand);
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
			return getQuads(state, side, rand, ModelData.EMPTY, null);
		}

		@Override
		public boolean useAmbientOcclusion() {
			return defaultBlock.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return defaultItem.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return false;
		}

		@Override
		public boolean isCustomRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleIcon() {
			return defaultBlock.getParticleIcon();
		}

		@Override
		public TextureAtlasSprite getParticleIcon(ModelData data) {
			ITreeSpecies species = data.get(TileSapling.TREE_SPECIES);

			return blockModels.getOrDefault(species, defaultBlock).getParticleIcon();
		}

		@Override
		public ItemOverrides getOverrides() {
			if (overrideList == null) {
				overrideList = new OverrideList();
			}
			return overrideList;
		}

		public class OverrideList extends ItemOverrides {

			@Nullable
			@Override
			public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int p_173469_) {
				IIndividualCapability<ITree> organism = GeneticHelper.getOrganism(stack);
				if (organism.isEmpty()) {
					return model;
				}
				IAlleleTreeSpecies species = organism.getAllele(TreeChromosomes.SPECIES, true);
				return itemModels.getOrDefault(species, model);
			}
		}
	}
}
