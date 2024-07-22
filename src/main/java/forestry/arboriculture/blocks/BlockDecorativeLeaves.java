package forestry.arboriculture.blocks;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.arboriculture.ILeafSpriteProvider;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.core.blocks.IColoredBlock;
import forestry.core.utils.BlockUtil;

public class BlockDecorativeLeaves extends Block implements IColoredBlock, IForgeShearable {
	private final ForestryLeafType definition;

	public BlockDecorativeLeaves(ForestryLeafType definition) {
		super(Properties.of(Material.LEAVES)
				.strength(0.2f)
				.sound(SoundType.GRASS)
				.noOcclusion()
				.isSuffocating(BlockUtil::alwaysTrue)
				.isRedstoneConductor(BlockUtil::alwaysFalse)
		);
		//		this.setLightOpacity(1);	//TODO block stuff);
		this.definition = definition;
	}

	public TreeDefinition getDefinition() {
		return definition;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		if (TreeDefinition.Willow.equals(definition)) {
			return Shapes.empty();
		}
		return super.getCollisionShape(state, worldIn, pos, context);
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		super.entityInside(state, worldIn, pos, entityIn);
		Vec3 motion = entityIn.getDeltaMovement();
		entityIn.setDeltaMovement(motion.multiply(0.4D, 1.0D, 0.4D));
	}

	/* PROPERTIES */
	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return true;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (face == Direction.DOWN) {
			return 20;
		} else if (face != Direction.UP) {
			return 10;
		} else {
			return 5;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int colorMultiplier(BlockState state, @Nullable BlockGetter worldIn, @Nullable BlockPos pos, int tintIndex) {
		IGenome genome = definition.getGenome();

		if (tintIndex == BlockAbstractLeaves.FRUIT_COLOR_INDEX) {
			IFruit fruitProvider = genome.getActiveAllele(TreeChromosomes.FRUITS).getProvider();
			return fruitProvider.getDecorativeColor();
		}
		ILeafSpriteProvider spriteProvider = genome.getActiveAllele(TreeChromosomes.SPECIES).getLeafSpriteProvider();
		return spriteProvider.getColor(false);
	}
}
