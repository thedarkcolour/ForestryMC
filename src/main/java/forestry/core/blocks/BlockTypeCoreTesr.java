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
package forestry.core.blocks;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import forestry.core.features.CoreTiles;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileEscritoire;
import forestry.modules.features.FeatureTileType;

public enum BlockTypeCoreTesr implements IBlockTypeTesr {
	ANALYZER(createAnalyzerProperties(() -> CoreTiles.ANALYZER, "analyzer")),
	ESCRITOIRE(createEscritoireProperties(() -> CoreTiles.ESCRITOIRE, "escritoire"));

	public static final BlockTypeCoreTesr[] VALUES = values();

	private final IMachinePropertiesTesr<?> machineProperties;

	private static IMachinePropertiesTesr<? extends TileAnalyzer> createAnalyzerProperties(Supplier<FeatureTileType<? extends TileAnalyzer>> teClass, String name) {
		return new MachinePropertiesTesr.Builder<>(teClass, name)
				.setParticleTexture(name + ".0")
				.setShape(Shapes::block)
				.setServerTicker(TileAnalyzer::serverTick)
				.create();
	}

	private static IMachinePropertiesTesr<? extends TileEscritoire> createEscritoireProperties(Supplier<FeatureTileType<? extends TileEscritoire>> teClass, String name) {
		final VoxelShape desk = Block.box(0, 8, 0, 16, 16, 16);
		final VoxelShape standRB = Block.box(13, 0, 13, 15, 10, 15);
		final VoxelShape standRF = Block.box(13, 0, 1, 15, 10, 3);
		final VoxelShape standLB = Block.box(1, 0, 13, 3, 10, 15);
		final VoxelShape standLF = Block.box(1, 0, 1, 3, 10, 3);

		return new MachinePropertiesTesr.Builder<>(teClass, name)
				.setParticleTexture(name + ".0")
				.setShape(() -> Shapes.or(desk, standLB, standLF, standRB, standRF))
				.create();
	}

	BlockTypeCoreTesr(IMachinePropertiesTesr<?> machineProperties) {
		this.machineProperties = machineProperties;
	}

	@Override
	public IMachinePropertiesTesr<?> getMachineProperties() {
		return machineProperties;
	}

	@Override
	public String getSerializedName() {
		return getMachineProperties().getSerializedName();
	}
}
