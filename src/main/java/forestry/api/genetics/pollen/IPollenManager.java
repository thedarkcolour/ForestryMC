package forestry.api.genetics.pollen;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

/**
 * Keeps track of different pollen types, used by bees and butterflies.
 */
public interface IPollenManager {
	/**
	 * Used to determine whether a block can be pollinated or have its pollen collected.
	 *
	 * @param level      The world to find pollen from.
	 * @param pos        The position to check for pollen.
	 * @param pollinator The individual or player (or something else) transporting the pollen.
	 * @return {@code true} if the target can be pollinated or have its pollen collected.
	 */
	boolean canPollinate(LevelAccessor level, BlockPos pos, @Nullable Object pollinator);

	/**
	 * Gets the pollen from the block at the current position.
	 * Use {@link #getPollenOfType} to find a specific type(s) of pollen.
	 *
	 * @param level      The world to find pollen from.
	 * @param pos        The position to check for pollen.
	 * @param pollinator The individual or player (or something else) responsible for gathering the pollen.
	 * @return Pollen of an arbitrary type, or {@code null} if no pollen of any type was found.
	 */
	@Nullable
	IPollen<?> getPollen(LevelAccessor level, BlockPos pos, @Nullable Object pollinator);

	/**
	 * Gets the pollen from the block at the current position, but only pollen of certain types.
	 *
	 * @param level       The world to find pollen from.
	 * @param pos         The position to check for pollen.
	 * @param pollinator  The individual or player (or something else) responsible for gathering the pollen.
	 * @param pollenTypes The types of pollen to check for. Pollen of other types are ignored.
	 * @return The pollen at the position, or {@code null} if no pollen of the given types was found.
	 */
	@Nullable
	IPollen<?> getPollenOfType(LevelAccessor level, BlockPos pos, @Nullable Object pollinator, Set<ResourceLocation> pollenTypes);

	@Nullable
	default IPollen<?> getPollenOfType(LevelAccessor level, BlockPos pos, @Nullable Object pollinator, ResourceLocation pollenType) {
		return getPollenOfType(level, pos, pollinator, Collections.singleton(pollenType));
	}

	/**
	 * @return The pollen type registered with the given ID, or {@code null} if none was found.
	 */
	@Nullable
	IPollenType<?> getPollenType(ResourceLocation id);

	/**
	 * @return A collection of all registered pollen types.
	 */
	Collection<IPollenType<?>> getAllPollenTypes();
}
