package forestry.farming.logic;

import net.minecraft.world.level.block.state.BlockState;

import forestry.api.farming.IFarmType;

public abstract class FarmLogicSoil extends FarmLogic {
	public FarmLogicSoil(IFarmType properties, boolean isManual) {
		super(properties, isManual);
	}

	protected boolean isAcceptedSoil(BlockState blockState) {
		return type.isAcceptedSoil(blockState);
	}
}
