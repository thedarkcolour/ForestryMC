package forestry.apiimpl.plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmLogic;
import forestry.api.farming.IFarmType;
import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableFactory;
import forestry.api.farming.IWaterConsumption;
import forestry.api.farming.Soil;
import forestry.api.plugin.IFarmTypeBuilder;
import forestry.api.plugin.IWindfallFarmableBuilder;
import forestry.core.utils.ModUtil;
import forestry.farming.logic.FarmType;

public class FarmTypeBuilder implements IFarmTypeBuilder {
	private final ResourceLocation id;
	private final ImmutableSet.Builder<Soil> soils = new ImmutableSet.Builder<>();
	private final IdentityHashMap<Item, WindfallFarmableBuilder> windfallFarmables = new IdentityHashMap<>();
	private final IdentityHashMap<Item, Consumer<IWindfallFarmableBuilder>> windfallFarmableModifications = new IdentityHashMap<>();
	private final ImmutableList.Builder<ItemStack> germlings = new ImmutableList.Builder<>();
	private final ImmutableList.Builder<ItemStack> products = new ImmutableList.Builder<>();
	private final ImmutableList.Builder<IFarmable> farmables = new ImmutableList.Builder<>();

	private BiFunction<IFarmType, Boolean, IFarmLogic> factory;
	private ItemStack icon;
	@Nullable
	private ToIntFunction<IFarmHousing> fertilizerConsumption;
	@Nullable
	private IWaterConsumption waterConsumption;

	public FarmTypeBuilder(ResourceLocation id, BiFunction<IFarmType, Boolean, IFarmLogic> factory, ItemStack icon) {
		this.id = id;
		this.factory = factory;
		this.icon = icon;
	}

	@Override
	public IFarmTypeBuilder setLogicFactory(BiFunction<IFarmType, Boolean, IFarmLogic> factory) {
		this.factory = factory;
		return this;
	}

	@Override
	public IFarmTypeBuilder setIcon(ItemStack stack) {
		this.icon = stack;
		return this;
	}

	@Override
	public IFarmTypeBuilder setFertilizerConsumption(ToIntFunction<IFarmHousing> consumption) {
		this.fertilizerConsumption = consumption;
		return this;
	}

	@Override
	public IFarmTypeBuilder setWaterConsumption(IWaterConsumption waterConsumption) {
		this.waterConsumption = waterConsumption;
		return this;
	}

	@Override
	public IFarmTypeBuilder addSoil(ItemStack resource, BlockState soilState) {
		this.soils.add(new Soil(resource, soilState));
		return this;
	}

	@Override
	public IFarmTypeBuilder addGermling(ItemStack germling) {
		this.germlings.add(germling);
		return this;
	}

	@Override
	public IFarmTypeBuilder addGermlings(Iterable<ItemStack> seedling) {
		this.germlings.addAll(seedling);
		return this;
	}

	@Override
	public IFarmTypeBuilder addProduct(ItemStack product) {
		this.products.add(product);
		return this;
	}

	@Override
	public IFarmTypeBuilder addProducts(Collection<ItemStack> products) {
		this.products.addAll(products);
		return this;
	}

	@Override
	public IFarmTypeBuilder addFarmable(IFarmable farmable) {
		this.farmables.add(farmable);
		return this;
	}

	@Override
	public IFarmTypeBuilder addWindfallFarmable(Item germling, IFarmableFactory factory, Consumer<IWindfallFarmableBuilder> action) {
		if (this.windfallFarmables.containsKey(germling)) {
			throw new IllegalStateException("A windfall farmable for farm type " + this.id + " was already associated with the germling " + ModUtil.getRegistryName(germling) + ": " + this.windfallFarmables.get(germling) + ". Did you mean to use IFarmTypeBuilder#modifyWindfallFarmable ?");
		} else {
			WindfallFarmableBuilder builder = new WindfallFarmableBuilder(factory);
			action.accept(builder);
			this.windfallFarmables.put(germling, builder);
		}
		return this;
	}

	@Override
	public IFarmTypeBuilder modifyWindfallFarmable(Item germling, Consumer<IWindfallFarmableBuilder> action) {
		this.windfallFarmableModifications.merge(germling, action, Consumer::andThen);
		return this;
	}

	public IFarmType build() {
		Preconditions.checkNotNull(this.fertilizerConsumption, "Missing required call to IFarmTypeBuilder#setFertilizerConsumption on farm type " + this.id);
		Preconditions.checkNotNull(this.waterConsumption, "Missing required call to IFarmTypeBuilder#setWaterConsumption on farm type " + this.id);

		for (Map.Entry<Item, Consumer<IWindfallFarmableBuilder>> entry : this.windfallFarmableModifications.entrySet()) {
			entry.getValue().accept(this.windfallFarmables.get(entry.getKey()));
		}
		for (Map.Entry<Item, WindfallFarmableBuilder> entry : this.windfallFarmables.entrySet()) {
			this.farmables.add(entry.getValue().build(entry.getKey()));
		}

		return new FarmType(this.id, this.icon, this.factory, this.farmables.build(), this.fertilizerConsumption, this.waterConsumption, this.soils.build());
	}
}
