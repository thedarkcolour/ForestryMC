package genetics.api.root;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A empty instance of an {@link IRootDefinition}.
 */
public enum EmptyRootDefinition implements IRootDefinition<IIndividualRoot<?>> {
	INSTANCE;

	public static <R extends IIndividualRoot<?>> IRootDefinition<R> empty() {
		return (IRootDefinition<R>) INSTANCE;
	}

	@Override
	public Optional<IIndividualRoot<?>> maybe() {
		return Optional.empty();
	}

	@Override
	public IIndividualRoot<?> get() {
		throw new NullPointerException();
	}

	@Override
	public <U extends IIndividualRoot> U cast() {
		return (U)get();
	}

	@Override
	public boolean isPresent() {
		return false;
	}

	@Override
	public IIndividualRoot<?> orElse(IIndividualRoot<?> other) {
		return other;
	}

	@Override
	public boolean test(Predicate predicate) {
		return false;
	}

	@Override
	public Optional filter(Predicate predicate) {
		return Optional.empty();
	}

	@Override
	public void ifPresent(Consumer consumer) {
		//The optional is empty, so we have nothing to call.
	}

	@Override
	public Optional map(Function mapper) {
		return Optional.empty();
	}
}
