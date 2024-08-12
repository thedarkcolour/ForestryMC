package forestry.sorting;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import forestry.api.genetics.filter.IFilterManager;
import forestry.api.genetics.filter.IFilterRuleType;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class FilterManager implements IFilterManager {
	private static final Comparator<IFilterRuleType> FILTER_COMPARATOR = (f, s) -> f.getId().compareToIgnoreCase(s.getId());

	private final ImmutableMap<String, IFilterRuleType> filterRules;
	private final Object2IntOpenHashMap<String> filterIdByName;
	private final Int2ObjectOpenHashMap<IFilterRuleType> filterById;

	public FilterManager(List<IFilterRuleType> registeredRuleTypes) {
		// sort alphabetically
		registeredRuleTypes.sort(FILTER_COMPARATOR);

		int size = registeredRuleTypes.size();
		ImmutableMap.Builder<String, IFilterRuleType> filterRules = ImmutableMap.builderWithExpectedSize(size);
		Object2IntOpenHashMap<String> filterIdByName = new Object2IntOpenHashMap<>(size);
		Int2ObjectOpenHashMap<IFilterRuleType> filterById = new Int2ObjectOpenHashMap<>(size);

		for (int i = 0; i < size; i++) {
			IFilterRuleType ruleType = registeredRuleTypes.get(i);
			filterRules.put(ruleType.getId(), ruleType);
			filterIdByName.put(ruleType.getId(), i);
			filterById.put(i, ruleType);
		}

		this.filterRules = filterRules.build();
		this.filterIdByName = filterIdByName;
		this.filterById = filterById;
	}

	@Override
	public ImmutableCollection<IFilterRuleType> getRules() {
		return this.filterRules.values();
	}

	@Override
	public IFilterRuleType getDefaultRule() {
		return DefaultFilterRuleType.CLOSED;
	}

	@Nullable
	@Override
	public IFilterRuleType getRule(String id) {
		return this.filterRules.get(id);
	}

	@Override
	public int getId(IFilterRuleType rule) {
		return this.filterIdByName.get(rule.getId());
	}

	@Nullable
	@Override
	public IFilterRuleType getRule(int id) {
		return this.filterById.get(id);
	}
}
