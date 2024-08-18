package forestry.api.genetics.pollen;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

/**
 * @param <P> The type of pollen
 * @see ForestryPollenTypes For default pollen types
 */
public interface IPollenType<P> {
	/**
	 * @return The unique ID of this pollen type.
	 */
	ResourceLocation id();

	/**
	 * Used to determine whether a block can be pollinated or have pollen of this type.
	 * Only used by butterflies in base Forestry.
	 *
	 * @param level      The world to find pollen from.
	 * @param pos        The position to check for pollen.
	 * @param pollinator The individual or player (or something else) transporting the pollen.
	 * @return {@code true} if the target block has pollen of this type.
	 */
	boolean canPollinate(LevelAccessor level, BlockPos pos, @Nullable Object pollinator);

	/**
	 * Tries to collect pollen from a block in the world.
	 *
	 * @param level      The world to try gathering pollen from. Always on the logical server.
	 * @param pos        The position of the block to try collecting pollen from. Always in a loaded chunk.
	 * @param pollinator The individual or player (or something else) responsible for gathering the pollen.
	 * @return A new pollen object, or {@code null} if the block did not have valid pollen for this type.
	 */
	@Nullable
	IPollen<P> tryCollectPollen(LevelAccessor level, BlockPos pos, @Nullable Object pollinator);

	/**
	 * Tries to pollinate the block at the position.
	 * This method should check whether the pollen is compatible with the target, such as checking if
	 * the block holds the same pollen type, and if the two pollens should mix.
	 *
	 * @param level      The world to try pollinating in. Always on the logical server.
	 * @param pos        The position of the block to try pollinating. Always in a loaded chunk.
	 * @param pollen     The pollen to pollinate the block with.
	 * @param pollinator The individual or player (or something else) responsible for the pollination.
	 * @return {@code true} if the pollination succeeded and the pollen should be consumed.
	 */
	boolean tryPollinate(LevelAccessor level, BlockPos pos, P pollen, @Nullable Object pollinator);

	/**
	 * Reads a pollen of this type from NBT. Should match whatever is written by {@link IPollen#writeNbt}.
	 *
	 * @param nbt The NBT object representing a pollen object of this type.
	 */
	IPollen<P> readNbt(Tag nbt);
}
