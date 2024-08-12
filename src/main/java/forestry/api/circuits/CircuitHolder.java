package forestry.api.circuits;

import net.minecraft.world.item.ItemStack;

/**
 * Replaces the old "CircuitRecipe" that isn't really a recipe.
 * A circuit holder determines the {@link ICircuit} contained by an item when inserted into the layout with the given ID.
 * See {@link forestry.api.plugin.IForestryPlugin#registerCircuits} to register your circuit holders.
 * See {@link forestry.api.IForestryApi#getCircuitManager} to query data about circuits and circuit holders.
 *
 * @param layoutId The unique ID of the circuit layout. See {@link ForestryCircuitLayouts} for examples.
 * @param stack    The item that holds a circuit in this layout. Count is ignored. Usually, it is a type of Electron Tube.
 * @param circuit  The circuit this item holds when inserted into the circuit layout with the given layout ID.
 */
public record CircuitHolder(String layoutId, ItemStack stack, ICircuit circuit) {
}
