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
package forestry.arboriculture.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.arboriculture.ITreeGenData;
import forestry.api.genetics.IGenome;

public class TreeBlockTypeLeaf implements ITreeBlockType {
	private final ITreeGenData tree;
	private final IGenome genome;

	public TreeBlockTypeLeaf(ITreeGenData tree, IGenome genome) {
		this.tree = tree;
		this.genome = genome;
	}

	@Override
	public void setDirection(Direction facing) {
	}

	@Override
	public boolean setBlock(LevelAccessor level, BlockPos pos) {
		return tree.setLeaves(this.genome, level, pos, level.getRandom(), false);
	}
}
