/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import forestry.api.arboriculture.IArboristTracker;
import forestry.api.arboriculture.ILeafTickHandler;
import forestry.api.arboriculture.ITreeSpecies;
import forestry.api.arboriculture.ITreekeepingMode;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ICheckPollinatable;
import forestry.api.genetics.IFruitFamily;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import forestry.api.genetics.IPollinatableSpeciesType;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpeciesType;

public interface ITreeSpeciesType extends ISpeciesType<ITreeSpecies> {

	/**
	 * @return {@link IArboristTracker} associated with the passed world.
	 */
	@Override
	IBreedingTracker<ITreeSpecies> getBreedingTracker(LevelAccessor world, @Nullable GameProfile player);

	/* TREE SPECIFIC */

	/**
	 * Register a leaf tick handler.
	 *
	 * @param handler the {@link ILeafTickHandler} to register.
	 */
	void registerLeafTickHandler(ILeafTickHandler handler);

	Collection<ILeafTickHandler> getLeafTickHandlers();

	@Nullable
	ITree getTree(Level world, BlockPos pos);

	//TODO: Why is there a world ?
	ITree getTree(Level world, IGenome genome);

	@Nullable
	ITree getTree(BlockEntity tileEntity);

	boolean plantSapling(Level world, ITree tree, GameProfile owner, BlockPos pos);

	boolean setFruitBlock(LevelAccessor world, IGenome genome, IFruit allele, float yield, BlockPos pos);

	/* GAME MODE */
	List<ITreekeepingMode> getTreekeepingModes();

	ITreekeepingMode getTreekeepingMode(LevelAccessor world);

	@Nullable
	ITreekeepingMode getTreekeepingMode(String name);

	void registerTreekeepingMode(ITreekeepingMode mode);

	void setTreekeepingMode(LevelAccessor world, ITreekeepingMode mode);

	Collection<IFruit> getFruitProvidersForFruitFamily(IFruitFamily fruitFamily);

	ICheckPollinatable createPollinatable(IIndividual individual);

	@Nullable
	IPollinatable tryConvertToPollinatable(@Nullable GameProfile owner, Level world, final BlockPos pos, final IIndividual pollen);
}
