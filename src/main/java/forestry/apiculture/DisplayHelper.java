package forestry.apiculture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.alyzer.IAlleleDisplayHelper;
import forestry.apiculture.genetics.IGeneticTooltipProvider;

import forestry.api.genetics.ILifeStage;

public enum DisplayHelper implements IAlleleDisplayHelper {
	INSTANCE;

	private final Map<ResourceLocation, PriorityQueue<OrderedPair<IGeneticTooltipProvider<?>>>> tooltips = new HashMap<>();
	private final Map<ResourceLocation, PriorityQueue<OrderedPair<IGeneticTooltipProvider<?>>>> alyzers = new HashMap<>();

	@Override
	public void addTooltip(IGeneticTooltipProvider<?> provider, ResourceLocation id, int orderingInfo) {
		this.tooltips.computeIfAbsent(id, (root) -> new PriorityQueue<>()).add(new OrderedPair<>(provider, orderingInfo, null));
	}

	@Override
	public void addTooltip(IGeneticTooltipProvider<?> provider, ResourceLocation id, int orderingInfo, Predicate<ILifeStage> typeFilter) {
		this.tooltips.computeIfAbsent(id, (root) -> new PriorityQueue<>()).add(new OrderedPair<>(provider, orderingInfo, typeFilter));
	}

	@Override
	public void addAlyzer(IGeneticTooltipProvider<?> provider, ResourceLocation id, int orderingInfo) {
		this.alyzers.computeIfAbsent(id, (root) -> new PriorityQueue<>()).add(new OrderedPair<>(provider, orderingInfo, null));
	}

	public <I extends IIndividual> List<IGeneticTooltipProvider<I>> getTooltips(ResourceLocation rootUID, ILifeStage type) {
		if (!tooltips.containsKey(rootUID)) {
			return List.of();
		}
		ArrayList<IGeneticTooltipProvider<?>> tooltips = new ArrayList<>();
		for (OrderedPair<IGeneticTooltipProvider<?>> pair : this.tooltips.get(rootUID)) {
			if (pair.hasValue(type)) {
				tooltips.add(pair.value);
			}
		}
		return tooltips;
	}

	private static class OrderedPair<T> implements Comparable<OrderedPair<T>> {
		private final T value;
		private final int info;
		@Nullable
		private final Predicate<ILifeStage> filter;

		private OrderedPair(T value, int info, @Nullable Predicate<ILifeStage> filter) {
			this.value = value;
			this.info = info;
			this.filter = filter;
		}

		public boolean hasValue(ILifeStage type) {
			return filter == null || filter.test(type);
		}

		@Override
		public int compareTo(OrderedPair<T> o) {
			return info - o.info;
		}
	}
}
