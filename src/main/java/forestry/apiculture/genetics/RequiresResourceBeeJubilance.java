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
package forestry.apiculture.genetics;

import java.util.Collections;
import java.util.HashSet;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeJubilance;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.core.tiles.TileUtil;

import forestry.api.genetics.IGenome;

public class RequiresResourceBeeJubilance implements IBeeJubilance {
	private final HashSet<BlockState> acceptedBlockStates = new HashSet<>();

	public RequiresResourceBeeJubilance(BlockState... acceptedBlockStates) {
		Collections.addAll(this.acceptedBlockStates, acceptedBlockStates);
	}

	@Override
	public boolean isJubilant(IBeeSpecies species, IGenome genome, IBeeHousing housing) {
		Level level = housing.getWorldObj();
		BlockPos pos = housing.getCoordinates();

		BlockEntity tile;
		do {
			pos = pos.below();
			tile = TileUtil.getTile(level, pos);
		} while (tile instanceof IBeeHousing && pos.getY() > 0);

		BlockState blockState = level.getBlockState(pos);
		return this.acceptedBlockStates.contains(blockState);
	}

}
