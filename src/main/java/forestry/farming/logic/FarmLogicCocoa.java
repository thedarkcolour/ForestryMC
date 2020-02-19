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
package forestry.farming.logic;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import forestry.api.farming.*;
import forestry.farming.logic.farmables.FarmableCocoa;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;


public class FarmLogicCocoa extends FarmLogic {
	private final IFarmable cocoa = new FarmableCocoa();

	public FarmLogicCocoa(IFarmProperties properties, boolean isManual) {
		super(properties, isManual);
	}

	private final Table<BlockPos, BlockPos, Integer> lastExtentsCultivation = HashBasedTable.create();

	@Override
	public boolean cultivate(World world, IFarmHousing farmHousing, BlockPos pos, FarmDirection direction, int extent) {
		BlockPos farmPos = farmHousing.getCoords();
		if (!lastExtentsCultivation.contains(farmPos, pos)) {
			lastExtentsCultivation.put(farmPos, pos, 0);
		}

		int lastExtent = lastExtentsCultivation.get(farmPos, pos);
		if (lastExtent > extent) {
			lastExtent = 0;
		}

		BlockPos position = translateWithOffset(pos.up(), direction, lastExtent);
		boolean result = tryPlantingCocoa(world, farmHousing, position, direction);

		lastExtent++;
		lastExtentsCultivation.put(farmPos, pos, lastExtent);

		return result;
	}

	private final Table<BlockPos, BlockPos, Integer> lastExtentsHarvest = HashBasedTable.create();

	@Override
	public Collection<ICrop> harvest(World world, IFarmHousing housing, FarmDirection direction, int extent, BlockPos pos) {
		BlockPos farmPos = housing.getCoords();
		if (!lastExtentsHarvest.contains(farmPos, pos)) {
			lastExtentsHarvest.put(farmPos, pos, 0);
		}

		int lastExtent = lastExtentsHarvest.get(farmPos, pos);
		if (lastExtent > extent) {
			lastExtent = 0;
		}

		BlockPos position = translateWithOffset(pos.up(), direction, lastExtent);
		Collection<ICrop> crops = getHarvestBlocks(world, position);
		lastExtent++;
		lastExtentsHarvest.put(farmPos, pos, lastExtent);

		return crops;
	}

	private boolean tryPlantingCocoa(World world, IFarmHousing farmHousing, BlockPos position, FarmDirection farmDirection) {
        BlockPos.Mutable current = new BlockPos.Mutable(position);
		BlockState blockState = world.getBlockState(current);
		while (isJungleTreeTrunk(blockState)) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				BlockPos candidate = new BlockPos(current.getX() + direction.getXOffset(), current.getY(), current.getZ() + direction.getZOffset());
				if (world.isBlockLoaded(candidate) && world.isAirBlock(candidate)) {
					return farmHousing.plantGermling(cocoa, world, candidate, farmDirection);
				}
			}

			current.move(Direction.UP);
			if (current.getY() - position.getY() > 1) {
				break;
			}

			blockState = world.getBlockState(current);
		}

		return false;
	}

	private static boolean isJungleTreeTrunk(BlockState blockState) {
		Block block = blockState.getBlock();
		//TODO - hopefully this is OK
		return block == Blocks.JUNGLE_LOG;
	}

	private Collection<ICrop> getHarvestBlocks(World world, BlockPos position) {

		Set<BlockPos> seen = new HashSet<>();
		Stack<ICrop> crops = new Stack<>();

		// Determine what type we want to harvest.
		BlockState blockState = world.getBlockState(position);
		Block block = blockState.getBlock();

		ICrop crop = null;
		if (!block.isIn(BlockTags.LOGS)) {
			crop = cocoa.getCropAt(world, position, blockState);
			if (crop == null) {
				return crops;
			}
		}

		if (crop != null) {
			crops.add(crop);
		}

		List<BlockPos> candidates = processHarvestBlock(world, crops, seen, position, position);
		List<BlockPos> temp = new ArrayList<>();
		while (!candidates.isEmpty() && crops.size() < 20) {
			for (BlockPos candidate : candidates) {
				temp.addAll(processHarvestBlock(world, crops, seen, position, candidate));
			}
			candidates.clear();
			candidates.addAll(temp);
			temp.clear();
		}
		// Log.finest("Logic %s at %s/%s/%s has seen %s blocks.", getClass().getName(), position.x, position.y, position.z, seen.size());

		return crops;
	}

	private List<BlockPos> processHarvestBlock(World world, Stack<ICrop> crops, Set<BlockPos> seen, BlockPos start, BlockPos position) {
		List<BlockPos> candidates = new ArrayList<>();

		// Get additional candidates to return
		for (int i = -1; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					BlockPos candidate = position.add(i, j, k);
					if (candidate.equals(position)) {
						continue;
					}
					if (Math.abs(candidate.getX() - start.getX()) > 5) {
						continue;
					}
					if (Math.abs(candidate.getZ() - start.getZ()) > 5) {
						continue;
					}

					// See whether the given position has already been processed
					if (seen.contains(candidate)) {
						continue;
					}

					if (!world.isBlockLoaded(candidate)) {
						continue;
					}

					BlockState blockState = world.getBlockState(candidate);
					ICrop crop = cocoa.getCropAt(world, candidate, blockState);
					if (crop != null) {
						crops.push(crop);
						candidates.add(candidate);
						seen.add(candidate);
					} else if (blockState.getBlock().isIn(BlockTags.LOGS)) {
						candidates.add(candidate);
						seen.add(candidate);
					}
				}
			}
		}

		return candidates;
	}

}
