package forestry.api.genetics.capability;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;

/**
 * The item form of {@link IIndividualHandler}.
 * These two interfaces are like IFluidHandlerItem and IFluidHandler, but for Forestry's genetic information.
 */
public interface IIndividualHandlerItem extends IIndividualHandler {
	/**
	 * @return The item containing this individual.
	 */
	ItemStack getContainer();

	static void ifPresent(ItemStack stack, BiConsumer<IIndividual, ILifeStage> action) {
		stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).ifPresent(handler -> action.accept(handler.getIndividual(), handler.getStage()));
	}

	static void ifPresent(ItemStack stack, Consumer<IIndividual> action) {
		stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).ifPresent(handler -> action.accept(handler.getIndividual()));
	}

	/**
	 * @return Whether the given item has an individual capability. (Vanilla saplings have a capability too)
	 */
	static boolean isIndividual(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM).isPresent();
	}

	/**
	 * Checks if the individual in this stack is present and if it matches some predicate.
	 *
	 * @param stack     The item to retrieve the individual from.
	 * @param predicate The predicate to test on the individual.
	 * @return {@code true} if the individual was present and the predicate returned true, false otherwise.
	 */
	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	static boolean filter(ItemStack stack, Predicate<IIndividual> predicate) {
		IIndividualHandlerItem handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).orElse(null);
		return handler != null && predicate.test(handler.getIndividual());
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	static boolean filter(ItemStack stack, BiPredicate<IIndividual, ILifeStage> predicate) {
		IIndividualHandlerItem handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).orElse(null);
		return handler != null && predicate.test(handler.getIndividual(), handler.getStage());
	}

	/**
	 * Retrieves the individual handler capability from the item stack if it is present.
	 *
	 * @param stack The item to get the individual handler from.
	 * @return The individual handler for this item, or null if none was found.
	 */
	@Nullable
	@SuppressWarnings("DataFlowIssue")
	static IIndividualHandlerItem get(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).orElse(null);
	}

	@Nullable
	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	static IIndividual getIndividual(ItemStack stack) {
		IIndividualHandlerItem handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL_HANDLER_ITEM, null).orElse(null);
		return handler != null ? handler.getIndividual() : null;
	}
}
