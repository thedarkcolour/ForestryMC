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
package forestry.apiculture.hives;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;

import forestry.api.apiculture.hives.IHiveGen;
import forestry.core.utils.BlockUtil;

// todo replace with HiveGenGroundTag
public class HiveGenGround implements IHiveGen {
	private final Set<Material> groundMaterials = new HashSet<>();

	public HiveGenGround(Block... groundBlocks) {
		for (Block block : groundBlocks) {
			BlockState blockState = block.defaultBlockState();
			Material blockMaterial = blockState.getMaterial();
			groundMaterials.add(blockMaterial);
		}
	}

	@Override
	public boolean isValidLocation(WorldGenLevel world, BlockPos pos) {
		BlockState groundBlockState = world.getBlockState(pos.below());
		Material groundBlockMaterial = groundBlockState.getMaterial();
		return groundMaterials.contains(groundBlockMaterial);
	}

	@Override
	public BlockPos getPosForHive(WorldGenLevel level, int posX, int posZ) {
		// get to the ground
		int groundY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, posX, posZ);
		int minBuildHeight = level.getMinBuildHeight();
		if (groundY == minBuildHeight) {
			return null;
		}

		final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(posX, groundY, posZ);

		BlockState blockState = level.getBlockState(pos);
		while (IHiveGen.isTreeBlock(blockState) || canReplace(blockState, level, pos)) {
			pos.move(Direction.DOWN);
			if (pos.getY() <= minBuildHeight) {
				return null;
			}
			blockState = level.getBlockState(pos);
		}

		return pos.above();
	}

	@Override
	public boolean canReplace(BlockState blockState, WorldGenLevel world, BlockPos pos) {
		return BlockUtil.canReplace(blockState, world, pos);
	}
}
