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
package forestry.arboriculture;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import forestry.api.ForestryTags;
import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.core.IProduct;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.blocks.ForestryPodType;
import forestry.core.utils.BlockUtil;
import forestry.core.utils.SpeciesUtil;

// Fruits that grow on the side of a tree's trunk, like cocoa beans
public class PodFruit extends Fruit {
	private final ForestryPodType type;

	public PodFruit(boolean dominant, ForestryPodType type, List<IProduct> products) {
		super(dominant, 2, products);

		this.type = type;
	}

	@Override
	public boolean requiresFruitBlocks() {
		return true;
	}

	@Override
	public boolean trySpawnFruitBlock(IGenome genome, LevelAccessor world, RandomSource rand, BlockPos pos) {
		if (rand.nextFloat() > getFruitChance(genome, world)) {
			return false;
		}

		if (type == ForestryPodType.COCOA) {
			return BlockUtil.tryPlantCocoaPod(world, pos);
		} else {
			IFruit activeAllele = genome.getActiveValue(TreeChromosomes.FRUIT);
			return SpeciesUtil.TREE_TYPE.get().setFruitBlock(world, genome, activeAllele, genome.getActiveValue(TreeChromosomes.YIELD), pos);
		}
	}

	@Override
	public TagKey<Block> getLogTag() {
		return switch (type) {
			case DATES -> ForestryTags.Blocks.PALM_LOGS;
			case PAPAYA -> ForestryTags.Blocks.PAPAYA_LOGS;
			default -> BlockTags.JUNGLE_LOGS;
		};
	}

	public ForestryPodType getType() {
		return this.type;
	}
}
