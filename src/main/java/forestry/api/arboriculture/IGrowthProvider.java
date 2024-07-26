package forestry.api.arboriculture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import forestry.api.arboriculture.genetics.ITree;

public interface IGrowthProvider {

	boolean canSpawn(ITree tree, Level world, BlockPos pos);

	boolean isBiomeValid(ITree tree, Holder.Reference<Biome> biome);
}
