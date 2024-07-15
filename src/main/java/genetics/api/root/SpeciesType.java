package genetics.api.root;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import genetics.api.GeneticsAPI;
import genetics.api.IGeneticFactory;

import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.ILifeStage;

import genetics.api.individual.IKaryotype;
import genetics.api.organism.IOrganismTypes;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.ComponentKeys;
import genetics.api.root.components.IRootComponent;
import genetics.api.root.components.IRootComponentContainer;
import genetics.api.root.translator.IIndividualTranslator;
import genetics.root.RootComponentContainer;

/**
 * Abstract implementation of the {@link ISpeciesType} interface.
 *
 * @param <I> The type of the individual that this root provides.
 */
public abstract class SpeciesType<I extends IIndividual> implements ISpeciesType<I> {
	protected final IOrganismTypes<I> types;
	protected final ITemplateContainer<I> templates;
	protected final IKaryotype karyotype;
	protected final ResourceLocation id;
	private ImmutableList<I> individualTemplates;
	private I defaultMember;
	private final IRootComponentContainer<I> components;
	@Nullable
	private IDisplayHelper<I> displayHelper;

	public SpeciesType(ResourceLocation id, IKaryotype karyotype) {
		this.id = id;
		this.karyotype = karyotype;
		this.components = new RootComponentContainer<>(context.createComponents(this), context.getComponentListeners(), context.getListeners());
		this.types = components.get(ComponentKeys.TYPES);
		this.templates = components.get(ComponentKeys.TEMPLATES);
		createDefault();
	}

	protected void createDefault() {
		this.defaultMember = create(karyotype.getDefaultGenome());
		ImmutableList.Builder<I> templateBuilder = new ImmutableList.Builder<>();
		for (IAllele[] template : templates.getTemplates()) {
			templateBuilder.add(templateAsIndividual(template));
		}
		this.individualTemplates = templateBuilder.build();
	}

	@Override
	public final ResourceLocation id() {
		return this.id;
	}

	@Override
	public List<I> getIndividualTemplates() {
		return individualTemplates;
	}

	@Override
	public I create(String templateIdentifier) {
		IAllele[] template = templates.getTemplate(templateIdentifier);
		return template.length == 0 ? null : create(karyotype.templateAsGenome(template));
	}

	@Override
	public ItemStack createStack(IAllele allele, ILifeStage type) {
		I individual = create(allele.id().toString());
		return individual == null ? ItemStack.EMPTY : types.createStack(individual, type);
	}

	@Override
	public boolean isMember(ItemStack stack) {
		return types.getType(stack) != null;
	}

	@Override
	public ITemplateContainer<I> getTemplates() {
		return templates;
	}

	@Override
	public IKaryotype getKaryotype() {
		return this.karyotype;
	}

	@Override
	public IIndividualTranslator<I> getTranslator() {
		IIndividualTranslator<I> translator = getComponentSafe(ComponentKeys.TRANSLATORS);
		if (translator == null) {
			throw new IllegalStateException(String.format("No translator component was added to the root with the uid '%s'.", id()));
		}
		return translator;
	}

	@Override
	public IOrganismTypes<I> getTypes() {
		return types;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ISpeciesType<?>> T cast() {
		return (T) this;
	}

	@Override
	public boolean hasComponent(ComponentKey<?> key) {
		return components.has(key);
	}

	@Nullable
	@Override
	public <C extends IRootComponent<I>> C getComponentSafe(ComponentKey<?> key) {
		return components.getSafe(key);
	}

	@Override
	public <C extends IRootComponent<I>> C getComponent(ComponentKey<?> key) {
		return components.get(key);
	}

	@Override
	public IRootComponentContainer<I> getComponentContainer() {
		return components;
	}

	@Override
	public IDisplayHelper<I> getDisplayHelper() {
		if (displayHelper == null) {
			IGeneticFactory geneticFactory = GeneticsAPI.apiInstance.getGeneticFactory();
			displayHelper = geneticFactory.createDisplayHelper(this);
		}
		return displayHelper;
	}
}
