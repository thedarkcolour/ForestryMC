package forestry.cultivation;

import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import forestry.api.climate.IClimatised;
import forestry.api.farming.IFarmHousing;
import forestry.core.fluids.ITankManager;
import forestry.core.tiles.ILiquidTankTile;
import forestry.farming.FarmTarget;
import forestry.farming.multiblock.IFarmInventoryInternal;

public interface IFarmHousingInternal extends IFarmHousing, ILiquidTankTile, IClimatised {
	@Override
	ITankManager getTankManager();

	@Override
	IFarmInventoryInternal getFarmInventory();

	void setUpFarmlandTargets(Map<Direction, List<FarmTarget>> targets);

	BlockPos getTopCoord();
}
