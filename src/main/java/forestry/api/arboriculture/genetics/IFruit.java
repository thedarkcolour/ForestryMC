/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.Product;
import forestry.api.genetics.alleles.IRegistryAlleleValue;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.core.utils.SpeciesUtil;

/**
 * Provides all information that is needed to spawn a fruit leaves / pod block in the world.
 */
public interface IFruit extends IRegistryAlleleValue {
	/**
	 * Returns the color of the fruit spite based on the ripening time of the fruit.
	 *
	 * @param genome       The genome of the tree of the pod / leaves block.
	 * @param ripeningTime The ripening time of the leaves / pod block. From 0 to {@link #getRipeningPeriod()}.
	 */
	int getColour(IGenome genome, BlockGetter world, BlockPos pos, int ripeningTime);

	/**
	 * return the color to use for decorative leaves. Usually the ripe color.
	 */
	int getDecorativeColor();

	/**
	 * Determines if fruit block of this provider is considered a leaf block.
	 *
	 * @param genome The genome of the tree of the pod / leaves block.
	 * @param level  The world in that the pod / leaves block is located.
	 * @param pos    The position of the pod / leaves block.
	 * @return True if this provider provides a fruit leaf for the given genome at the given position.
	 */
	boolean isFruitLeaf(IGenome genome, LevelAccessor level, BlockPos pos);

	/**
	 * The chance that this leaves contains fruits or the chance that a pod block spawns.
	 *
	 * @param genome The genome of the tree of the pod / leaves block.
	 * @param level
	 * @return The chance that this leaves contains fruits or the chance that a pod block spawns.
	 */
	default float getFruitChance(IGenome genome, LevelAccessor level, BlockPos pos) {
		ITreeSpeciesType treeRoot = SpeciesUtil.TREE_TYPE.get();
		if (treeRoot == null) {
			return 0.0F;
		}
		//float yieldModifier = treeRoot.getTreekeepingMode(level).getYieldModifier(genome, 1.0F);
		return genome.getActiveValue(TreeChromosomes.YIELD) * 2.5F;// * yieldModifier;
	}

	/**
	 * @return How many successful ripening block ticks a fruit needs to be ripe.
	 */
	int getRipeningPeriod();

	/**
	 * A unmodifiable list that contains all products and their associated drop chances.
	 *
	 * @return A unmodifiable list that contains all products and their associated drop chances.
	 */
	List<Product> getProducts();

	/**
	 * A unmodifiable list that contains all specialties and their associated drop chances.
	 *
	 * @return A unmodifiable list that contains all products and their associated drop chances.
	 */
	List<Product> getSpecialty();

	/**
	 * Returns all drops of this block if you harvest it.
	 *
	 * @param genome       The genome of the tree of the leaves / pod.
	 * @param ripeningTime The repining time of the block. From 0 to {@link #getRipeningPeriod()}.
	 */
	List<ItemStack> getFruits(IGenome genome, Level level, BlockPos pos, int ripeningTime);

	/**
	 * @return The location of the pod model in the "modid:pods/" folder.
	 */
	@Nullable
	String getModelName();

	/**
	 * @return The mod id of that adds this fruit provider. Needed for the allele of this fruit.
	 */
	String getModId();

	/* TEXTURE OVERLAY */

	/**
	 * @param ripeningTime Elapsed ripening time for the fruit.
	 * @return ResourceLocation of the texture to overlay on the leaf block.
	 */
	@Nullable
	ResourceLocation getSprite(IGenome genome, BlockGetter world, BlockPos pos, int ripeningTime);

	@Nullable
	default ResourceLocation getDecorativeSprite() {
		return null;
	}

	/**
	 * @return true if this fruit provider requires fruit blocks to spawn, false otherwise.
	 */
	boolean requiresFruitBlocks();

	/**
	 * Tries to spawn a fruit block at the potential position when the tree generates.
	 * Spawning a fruit has a random chance of success based on {@link TreeChromosomes#SAPPINESS}
	 *
	 * @return true if a fruit block was spawned, false otherwise.
	 */
	boolean trySpawnFruitBlock(IGenome genome, LevelAccessor world, RandomSource rand, BlockPos pos);

	@OnlyIn(Dist.CLIENT)
	default void registerSprites(TextureStitchEvent.Pre event) {
	}

	/**
	 * Tag for the log that the is placed on
	 */
	default TagKey<Block> getLogTag() {
		return BlockTags.JUNGLE_LOGS;
	}
}
