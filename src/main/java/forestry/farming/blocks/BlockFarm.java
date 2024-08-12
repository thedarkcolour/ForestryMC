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
package forestry.farming.blocks;

import javax.annotation.Nullable;
import java.util.Locale;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import forestry.core.blocks.BlockStructure;
import forestry.farming.tiles.TileFarmControl;
import forestry.farming.tiles.TileFarmGearbox;
import forestry.farming.tiles.TileFarmHatch;
import forestry.farming.tiles.TileFarmPlain;
import forestry.farming.tiles.TileFarmValve;

public class BlockFarm extends BlockStructure implements EntityBlock {
	private final EnumFarmBlockType type;
	private final EnumFarmMaterial farmMaterial;

	// todo replace with boolean "is_band"
	public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

	public enum State implements StringRepresentable {
		PLAIN, BAND;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	public static BlockFarm create(EnumFarmBlockType type, EnumFarmMaterial material) {
		if (type == EnumFarmBlockType.PLAIN) {
			return new Plain(material);
		} else {
			return new BlockFarm(type, material);
		}
	}

	public BlockFarm(EnumFarmBlockType type, EnumFarmMaterial farmMaterial) {
		super(Block.Properties.of(Material.STONE).strength(1.0f));
		this.type = type;
		this.farmMaterial = farmMaterial;
	}

	public EnumFarmBlockType getType() {
		return type;
	}

	public EnumFarmMaterial getFarmMaterial() {
		return farmMaterial;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return switch (type) {
			case GEARBOX -> new TileFarmGearbox(pos, state);
			case HATCH -> new TileFarmHatch(pos, state);
			case VALVE -> new TileFarmValve(pos, state);
			case CONTROL -> new TileFarmControl(pos, state);
			default -> new TileFarmPlain(pos, state);
		};
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return getType() == EnumFarmBlockType.CONTROL;
	}

	public static class Plain extends BlockFarm {
		public Plain(EnumFarmMaterial farmMaterial) {
			super(EnumFarmBlockType.PLAIN, farmMaterial);

			registerDefaultState(this.getStateDefinition().any().setValue(STATE, State.PLAIN));
		}

		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(STATE);
		}
	}
}
