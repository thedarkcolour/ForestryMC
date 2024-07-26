package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;

import forestry.api.ForestryCapabilities;

public interface IIndividualHandler {
	ILifeStage getStage();

	IIndividual getIndividual();

	ItemStack getContainer();

	static void ifPresent(ItemStack stack, BiConsumer<IIndividual, ILifeStage> action) {
		stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).ifPresent(handler -> action.accept(handler.getIndividual(), handler.getStage()));
	}

	static void ifPresent(ItemStack stack, Consumer<IIndividual> action) {
		stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).ifPresent(handler -> action.accept(handler.getIndividual()));
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
		IIndividualHandler handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).orElse(null);
		return handler != null && predicate.test(handler.getIndividual());
	}

	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	static boolean filter(ItemStack stack, BiPredicate<IIndividual, ILifeStage> predicate) {
		IIndividualHandler handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).orElse(null);
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
	static IIndividualHandler get(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).orElse(null);
	}

	@Nullable
	@SuppressWarnings({"ConstantValue", "DataFlowIssue"})
	static IIndividual getIndividual(ItemStack stack) {
		IIndividualHandler handler = stack.getCapability(ForestryCapabilities.INDIVIDUAL, null).orElse(null);
		return handler != null ? handler.getIndividual() : null;
	}
}
