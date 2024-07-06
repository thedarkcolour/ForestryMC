package genetics.organism;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;

import forestry.Forestry;

import genetics.GeneticFactory;
import genetics.Genetics;
import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import genetics.api.organism.IOrganism;
import genetics.api.organism.IOrganismHandler;
import genetics.api.organism.IOrganismType;
import genetics.api.organism.IOrganismTypes;
import genetics.api.root.IIndividualRoot;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.ComponentKeys;

public class OrganismTypes<I extends IIndividual> implements IOrganismTypes<I> {
	private final Map<IOrganismType, IOrganismHandler<I>> handlers = new LinkedHashMap<>();
	private final IIndividualRoot<I> root;
	@Nullable
	private IOrganismType defaultType;

	public OrganismTypes(IIndividualRoot<I> root) {
		this.root = root;
	}

	@Override
	public IIndividualRoot<I> getRoot() {
		return root;
	}

	@Override
	public IOrganismTypes<I> registerType(IOrganismType type, IOrganismHandler<I> handler, boolean defaultType) {
		handlers.put(type, handler);
		if (defaultType) {
			this.defaultType = type;
		}
		return this;
	}

	@Override
	public IOrganismTypes<I> registerType(IOrganismType type, Supplier<ItemStack> stack, boolean defaultType) {
		return registerType(type, GeneticFactory.INSTANCE.createOrganismHandler(root.getDefinition(), stack), defaultType);
	}

	@Override
	public ItemStack createStack(I individual, IOrganismType type) {
		IOrganismHandler<I> handler = handlers.get(type);
		if (handler == null) {
			return ItemStack.EMPTY;
		}
		return handler.createStack(individual);
	}

	@Nullable
	@Override
	public I createIndividual(ItemStack itemStack) {
		IOrganismType type = getType(itemStack);
		if (type == null) {
			return null;
		}
		IOrganismHandler<I> handler = handlers.get(type);
		if (handler == null) {
			return null;
		}
		return handler.createIndividual(itemStack);
	}

	@Override
	public boolean setIndividual(ItemStack stack, I individual) {
		if (stack.isEmpty()) {
			return false;
		}
		IOrganismType type = getType(stack);

		if (type != null) {
			IOrganismHandler<I> handler = handlers.get(type);

			if (handler != null) {
				return handler.setIndividual(stack, individual);
			}
		}
		return false;
	}

	@Nullable
	@Override
	public IOrganismType getType(ItemStack itemStack) {
		if (itemStack.isEmpty()) {
			return null;
		}
		IOrganism<?> organism = itemStack.getCapability(Genetics.ORGANISM).orElse(GeneticHelper.EMPTY);
		IOrganismType type = organism.getType();

		if (!type.isEmpty() && getHandler(type) != null) {
			return type;
		} else {
			return null;
		}
	}

	@Override
	public IOrganismType getDefaultType() {
		if (defaultType == null) {
			Iterator<IOrganismType> organismTypes = handlers.keySet().iterator();
			if (!organismTypes.hasNext()) {
				String message = String.format("No types were registered for the individual root '%s'.", root.getUID());
				throw new IllegalStateException(message);
			}
			defaultType = organismTypes.next();
			Forestry.LOGGER.debug("No default type was registered for individual root '{}' used first registered type.", new Object[]{root.getUID()});
		}
		return defaultType;
	}

	@Nullable
	@Override
	public IOrganismHandler<I> getHandler(IOrganismType type) {
		return handlers.get(type);
	}

	@Nullable
	@Override
	public IOrganismHandler<I> getHandler(ItemStack itemStack) {
		IOrganismType type = getType(itemStack);
		return type != null ? getHandler(type) : null;
	}

	@Override
	public Collection<IOrganismType> getTypes() {
		return handlers.keySet();
	}

	@Override
	public Collection<IOrganismHandler<I>> getHandlers() {
		return handlers.values();
	}

	@Override
	public ComponentKey<IOrganismTypes> getKey() {
		return ComponentKeys.TYPES;
	}
}
