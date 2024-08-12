/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import com.mojang.authlib.GameProfile;

import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.core.IProduct;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IIndividual;
import forestry.api.core.Product;

public interface ITree extends IIndividual {
	IEffectData[] doEffect(IEffectData[] storedData, Level level, BlockPos pos);

	IEffectData[] doFX(IEffectData[] storedData, Level level, BlockPos pos);

	List<ITree> getSaplings(Level level, @Nullable GameProfile playerProfile, BlockPos pos, float modifier);

	List<IProduct> getProducts();

	List<IProduct> getSpecialties();

	List<ItemStack> produceStacks(Level level, BlockPos pos, int ripeningTime);

	/**
	 * @return Boolean indicating whether a sapling can stay planted at the given position.
	 */
	boolean canStay(BlockGetter level, BlockPos pos);

	/**
	 * @return Integer denoting the maturity (block ticks) required for a sapling to attempt to grow into a tree.
	 */
	int getRequiredMaturity();

	/**
	 * @return Integer denoting how resilient leaf blocks are against adverse influences (i.e. caterpillars).
	 */
	int getResilience();

	Feature<NoneFeatureConfiguration> getTreeGenerator(WorldGenLevel level, BlockPos pos, boolean wasBonemealed);

	ITree copy();

	@Override
	ITreeSpeciesType getType();

	@Override
	ITreeSpecies getSpecies();

	@Override
	ITreeSpecies getInactiveSpecies();

	boolean canBearFruit();
}
