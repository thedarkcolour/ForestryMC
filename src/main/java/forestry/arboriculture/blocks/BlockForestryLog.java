package forestry.arboriculture.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.arboriculture.IWoodTyped;

public class BlockForestryLog extends RotatedPillarBlock implements IWoodTyped {
	private final WoodBlockKind kind;
	private final boolean fireproof;
	private final IWoodType woodType;

	public BlockForestryLog(WoodBlockKind kind, boolean fireproof, IWoodType woodType) {
		super(BlockForestryPlank.createWoodProperties(woodType));
		this.kind = kind;
		this.fireproof = fireproof;
		this.woodType = woodType;
	}

	@Override
	public WoodBlockKind getBlockKind() {
		return this.kind;
	}

	@Override
	public final boolean isFireproof() {
		return fireproof;
	}

	@Override
	public IWoodType getWoodType() {
		return woodType;
	}

	@Override
	public final int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (fireproof) {
			return 0;
		} else if (face == Direction.DOWN) {
			return 20;
		} else if (face != Direction.UP) {
			return 10;
		} else {
			return 5;
		}
	}

	@Override
	public final int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (fireproof) {
			return 0;
		}
		return 5;
	}
}
