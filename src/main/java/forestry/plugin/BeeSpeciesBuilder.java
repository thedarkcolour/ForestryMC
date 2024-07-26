package forestry.plugin;

import java.awt.Color;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeJubilance;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.genetics.Product;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.apiculture.BeeSpecies;
import forestry.apiculture.genetics.DefaultBeeJubilance;

import deleteme.Todos;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;

public class BeeSpeciesBuilder extends SpeciesBuilder<IBeeSpeciesType, IBeeSpeciesBuilder> implements IBeeSpeciesBuilder {
	private final Reference2FloatOpenHashMap<Supplier<ItemStack>> products = new Reference2FloatOpenHashMap<>();
	private final Reference2FloatOpenHashMap<Supplier<ItemStack>> specialties = new Reference2FloatOpenHashMap<>();
	private int bodyColor = 0xffdc16;
	private int stripesColor = 0;
	private int outlineColor = -1;
	private boolean nocturnal;
	private IBeeJubilance jubilance = DefaultBeeJubilance.INSTANCE;

	public BeeSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	protected ISpeciesFactory<IBeeSpeciesType, IBeeSpeciesBuilder> createSpeciesFactory() {
		return BeeSpecies::new;
	}

	@Override
	public IBeeSpeciesBuilder addProduct(Supplier<ItemStack> stack, float chance) {
		this.products.put(stack, chance);
		return this;
	}

	@Override
	public IBeeSpeciesBuilder addSpecialty(Supplier<ItemStack> stack, float chance) {
		this.specialties.put(stack, chance);
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setBody(Color color) {
		this.bodyColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setStripes(Color color) {
		this.stripesColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setOutline(Color color) {
		this.outlineColor = color.getRGB();
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setJubilance(IBeeJubilance jubilance) {
		this.jubilance = jubilance;
		return this;
	}

	@Override
	public IBeeSpeciesBuilder setNocturnal(boolean nocturnal) {
		this.nocturnal = nocturnal;
		return this;
	}

	@Override
	public boolean isNocturnal() {
		return this.nocturnal;
	}

	@Override
	public List<Product> buildProducts() {
		// todo
		throw Todos.unimplemented();
	}

	@Override
	public List<Product> buildSpecialties() {
		// todo
		throw Todos.unimplemented();
	}

	@Override
	public int getBody() {
		return this.bodyColor;
	}

	@Override
	public int getStripes() {
		return this.stripesColor;
	}

	@Override
	public int getOutline() {
		return this.outlineColor;
	}

	@Override
	public IBeeJubilance getJubilance() {
		return this.jubilance;
	}
}
