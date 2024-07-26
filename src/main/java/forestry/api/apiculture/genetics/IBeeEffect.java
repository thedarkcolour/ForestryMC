/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.apiculture.genetics;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.genetics.IEffectData;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.api.genetics.alleles.IRegistryAlleleValue;
import forestry.core.render.ParticleRender;
import forestry.core.utils.SpeciesUtil;
import forestry.core.utils.VecUtil;

public interface IBeeEffect extends IEffect, IRegistryAlleleValue {
	static AABB getBounding(IBeeHousing housing, IGenome genome) {
		IBeeModifier beeModifier = SpeciesUtil.BEE_TYPE.get().createBeeHousingModifier(housing);
		float territoryModifier = beeModifier.getTerritoryModifier(genome, 1.0f);

		Vec3i area = VecUtil.scale(genome.getActiveValue(BeeChromosomes.TERRITORY), territoryModifier);
		Vec3i offset = VecUtil.scale(area, -1 / 2.0f);

		BlockPos min = housing.getCoordinates().offset(offset);
		BlockPos max = min.offset(area);

		return new AABB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

	static <T extends Entity> List<T> getEntitiesInRange(IGenome genome, IBeeHousing housing, Class<T> entityClass) {
		AABB boundingBox = getBounding(housing, genome);
		return housing.getWorldObj().getEntitiesOfClass(entityClass, boundingBox);
	}

	@Override
	default IEffectData validateStorage(IEffectData storedData) {
		return storedData;
	}

	@Override
	default boolean isCombinable() {
		return false;
	}

	/**
	 * Called by apiaries to cause an effect in the world. (server)
	 *
	 * @param genome     Genome of the bee queen causing this effect
	 * @param storedData Object containing the stored effect data for the apiary/hive the bee is in.
	 * @param housing    {@link IBeeHousing} the bee currently resides in.
	 * @return storedData, may have been manipulated.
	 */
	default IEffectData doEffect(IGenome genome, IEffectData storedData, IBeeHousing housing) {
		return storedData;
	}

	/**
	 * Called on the client side to produce visual bee effects.
	 *
	 * @param genome     Genome of the bee queen causing this effect
	 * @param storedData Object containing the stored effect data for the apiary/hive the bee is in.
	 * @param housing    {@link IBeeHousing} the bee currently resides in.
	 * @return storedData, may have been manipulated.
	 */
	default IEffectData doFX(IGenome genome, IEffectData storedData, IBeeHousing housing) {
		IBeekeepingLogic beekeepingLogic = housing.getBeekeepingLogic();
		List<BlockPos> flowerPositions = beekeepingLogic.getFlowerPositions();

		ParticleRender.addBeeHiveFX(housing, genome, flowerPositions);
		return storedData;
	}
}
