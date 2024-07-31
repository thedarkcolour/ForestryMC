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
package forestry.arboriculture.models;

import com.google.common.base.Preconditions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.blocks.BlockAbstractLeaves;
import forestry.arboriculture.blocks.BlockDecorativeLeaves;
import forestry.core.models.ModelBlockCached;
import forestry.core.models.baker.ModelBaker;
import forestry.core.utils.ResourceUtil;
import forestry.core.utils.SpeciesUtil;

// todo this is duplicate of ModelDefaultLeavesFruit.
@OnlyIn(Dist.CLIENT)
public class ModelDecorativeLeaves extends ModelBlockCached<BlockDecorativeLeaves, ModelDefaultLeaves.Key> {
	public ModelDecorativeLeaves() {
		super(BlockDecorativeLeaves.class);
	}

	@Override
	protected ModelDefaultLeaves.Key getInventoryKey(ItemStack stack) {
		Block block = Block.byItem(stack.getItem());
		Preconditions.checkArgument(block instanceof BlockDecorativeLeaves, "ItemStack must be for decorative leaves.");
		BlockDecorativeLeaves bBlock = (BlockDecorativeLeaves) block;
		return new ModelDefaultLeaves.Key(bBlock.getSpeciesId(), Minecraft.useFancyGraphics());
	}

	@Override
	protected ModelDefaultLeaves.Key getWorldKey(BlockState state, ModelData extraData) {
		Block block = state.getBlock();
		Preconditions.checkArgument(block instanceof BlockDecorativeLeaves, "state must be for decorative leaves.");
		BlockDecorativeLeaves bBlock = (BlockDecorativeLeaves) block;
		return new ModelDefaultLeaves.Key(bBlock.getSpeciesId(), Minecraft.useFancyGraphics());
	}

	@Override
	protected void bakeBlock(BlockDecorativeLeaves block, ModelData extraData, ModelDefaultLeaves.Key key, ModelBaker baker, boolean inventory) {
		ResourceLocation speciesId = key.speciesId;

		ITreeSpecies species = SpeciesUtil.getTreeSpecies(speciesId);
		ILeafSpriteProvider leafSpriteProvider = species.getLeafSpriteProvider();

		ResourceLocation leafSpriteLocation = leafSpriteProvider.getSprite(false, key.fancy);
		TextureAtlasSprite leafSprite = ResourceUtil.getBlockSprite(leafSpriteLocation);

		// Render the plain leaf block.
		baker.addBlockModel(leafSprite, BlockAbstractLeaves.FOLIAGE_COLOR_INDEX);

		// Render overlay for fruit leaves.
		ResourceLocation fruitSpriteLocation = species.getDefaultGenome().getActiveValue(TreeChromosomes.FRUITS).getDecorativeSprite();
		if (fruitSpriteLocation != null) {
			TextureAtlasSprite fruitSprite = ResourceUtil.getBlockSprite(fruitSpriteLocation);
			baker.addBlockModel(fruitSprite, BlockAbstractLeaves.FRUIT_COLOR_INDEX);
		}

		// Set the particle sprite
		baker.setParticleSprite(leafSprite);
	}

	@Override
	protected BakedModel bakeModel(BlockState state, ModelDefaultLeaves.Key key, BlockDecorativeLeaves block, ModelData extraData) {
		ModelBaker baker = new ModelBaker();

		bakeBlock(block, extraData, key, baker, false);

		blockModel = baker.bake(false);
		onCreateModel(blockModel);
		return blockModel;
	}

	@Override
	public ItemTransforms getTransforms() {
		return ModelLeaves.TRANSFORMS;
	}
}
