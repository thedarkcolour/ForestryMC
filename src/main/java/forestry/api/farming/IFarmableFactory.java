package forestry.api.farming;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.item.Item;

/**
 * Used to construct a farmable when farm registry is complete.
 */
public interface IFarmableFactory {
	/**
	 * Creates a new instance of farmable when farm registration is finished.
	 *
	 * @param germling Germling for this farmable. Used for {@link IFarmable#isGermling}, but feel free to ignore.
	 * @param windfall The set of all windfall items produced by this farmable. Used for {@link IFarmable#isWindfall}.
	 * @return A new instance of farmable.
	 */
	IFarmable create(Item germling, ImmutableSet<Item> windfall);
}
