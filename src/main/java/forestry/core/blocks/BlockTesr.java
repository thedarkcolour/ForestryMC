package forestry.core.blocks;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockTesr<P extends Enum<P> & IBlockType> extends BlockBase<P> {
	public BlockTesr(P blockType, Properties properties) {
		super(blockType, properties);
	}

	public BlockTesr(P blockType, Material material) {
		super(blockType, material);
	}

	public BlockTesr(P blockType) {
		super(blockType);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}
