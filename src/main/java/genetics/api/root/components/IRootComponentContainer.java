package genetics.api.root.components;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

import genetics.api.individual.IIndividual;

public interface IRootComponentContainer<I extends IIndividual> {

	void onStage(IStage stage);

	boolean has(ComponentKey key);

	<C extends IRootComponent<I>> C get(ComponentKey key);

	@Nullable
	<C extends IRootComponent<I>> C getSafe(ComponentKey key);

	Map<ComponentKey, IRootComponent<I>> getComponents();
}
