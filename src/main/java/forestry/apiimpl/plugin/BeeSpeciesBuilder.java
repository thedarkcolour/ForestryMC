package forestry.apiimpl.plugin;

import com.google.common.collect.ImmutableList;

import java.awt.Color;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.IBeeJubilance;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.Product;
import forestry.api.plugin.IBeeSpeciesBuilder;
import forestry.apiculture.BeeSpecies;
import forestry.apiculture.genetics.DefaultBeeJubilance;

import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;

public class BeeSpeciesBuilder extends SpeciesBuilder<IBeeSpeciesType, IBeeSpecies, IBeeSpeciesBuilder> implements IBeeSpeciesBuilder {
	private final Reference2FloatOpenHashMap<ItemStack> products = new Reference2FloatOpenHashMap<>();
	private final Reference2FloatOpenHashMap<ItemStack> specialties = new Reference2FloatOpenHashMap<>();
	private int bodyColor = 0xffdc16;
	private int stripesColor = 0;
	private int outlineColor = -1;
	private boolean nocturnal;
	private IBeeJubilance jubilance = DefaultBeeJubilance.INSTANCE;

	public BeeSpeciesBuilder(ResourceLocation id, String genus, String species, MutationsRegistration mutations) {
		super(id, genus, species, mutations);
	}

	@Override
	public ISpeciesFactory<IBeeSpeciesType, IBeeSpecies, IBeeSpeciesBuilder> createSpeciesFactory() {
		return BeeSpecies::new;
	}

	@Override
	public IBeeSpeciesBuilder addProduct(ItemStack stack, float chance) {
		this.products.put(stack, chance);
		return this;
	}

	@Override
	public IBeeSpeciesBuilder addSpecialty(ItemStack stack, float chance) {
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
		return buildProductsFromStacks(this.products);
	}

	@Override
	public List<Product> buildSpecialties() {
		return buildProductsFromStacks(this.specialties);
	}

	private static List<Product> buildProductsFromStacks(Reference2FloatOpenHashMap<ItemStack> stacks) {
		ImmutableList.Builder<Product> builder = ImmutableList.builderWithExpectedSize(stacks.size());
		for (Reference2FloatMap.Entry<ItemStack> entry : stacks.reference2FloatEntrySet()) {
			ItemStack stack = entry.getKey();
			builder.add(new Product(stack.getItem(), stack.getCount(), stack.getTag(), entry.getFloatValue()));
		}
		return builder.build();
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
