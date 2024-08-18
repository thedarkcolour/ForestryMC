package forestry.api.genetics.pollen;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public interface IPollen<P> {
	IPollenType<P> getType();

	P getPollen();

	/**
	 * NOTE: Do not write the pollen type ID as part of the returned NBT object, it is handled by Forestry.
	 *
	 * @return The serialized NBT of this pollen. Should match whatever is deserialized by {@link IPollenType#readNbt}.
	 */
	@Nullable
	Tag writeNbt();

	/**
	 * Used by the Alveary Sieve and butterflies to collect item forms of pollen.
	 *
	 * @return An item form of this pollen, or {@link ItemStack#EMPTY} if this pollen has no item form.
	 */
	ItemStack createStack();

	/**
	 * @return The value contained in this pollen, cast to the appropriate type. Workaround for generics issues.
	 */
	default <T> T castPollen() {
		return (T) getPollen();
	}

	default boolean tryPollinate(LevelAccessor level, BlockPos pos, @Nullable Object pollinator) {
		return getType().tryPollinate(level, pos, castPollen(), pollinator);
	}
}
