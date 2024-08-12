package forestry.api.circuits;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.item.ItemStack;

/**
 * Manages data about different circuits and layouts in Forestry.
 * Register data with {@link forestry.api.plugin.IForestryPlugin#registerCircuits}.
 */
public interface ICircuitManager {
	/**
	 * @return A collection of all registered circuit layouts.
	 */
	List<ICircuitLayout> getLayouts();

	/**
	 * Retrieves the item's circuit for the given circuit layout, or null if this item is not applicable for the layout.
	 *
	 * @param layout The circuit layout.
	 * @param stack  The item (usually an electron tube).
	 * @return The circuit associated with the circuit layout and item, or {@code null} if this item doesn't work for the circuit layout.
	 */
	@Nullable
	ICircuit getCircuit(ICircuitLayout layout, ItemStack stack);

	@Nullable
	ICircuit getCircuit(String circuitId);

	/**
	 * @return The circuit layout with the given ID, or {@code null} if none was registered with that ID.
	 */
	@Nullable
	ICircuitLayout getLayout(String layoutId);

	/**
	 * @return The circuit board stored in this item's NBT, {@code null} if this item has no NBT or is not a chipset.
	 */
	@Nullable
	ICircuitBoard getCircuitBoard(ItemStack stack);

	/**
	 * Note: Currently hardcoded to Forestry circuit board.
	 * @return {@code true} if the given item is a circuit board and can have circuits installed in it.
	 */
	boolean isCircuitBoard(ItemStack stack);

	Collection<CircuitHolder> getCircuitHolders();
}
