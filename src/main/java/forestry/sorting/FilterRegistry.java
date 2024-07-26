package forestry.sorting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;

import forestry.api.genetics.filter.IFilterRegistry;
import forestry.api.genetics.filter.IFilterRuleType;

public enum FilterRegistry implements IFilterRegistry {
	INSTANCE;

	private static final Comparator<IFilterRuleType> FILTER_COMPARATOR = (f, s) -> f.getId().compareToIgnoreCase(s.getId());

	private final LinkedHashMap<String, IFilterRuleType> filterByName = new LinkedHashMap<>();
	private final LinkedHashMap<String, Integer> filterIDByName = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, IFilterRuleType> filterByID = new LinkedHashMap<>();

	@Override
	public void registerFilter(IFilterRuleType rule) {
		if (!this.filterByID.isEmpty()) {
			return;
		}
		this.filterByName.put(rule.getId(), rule);
	}

	public void init() {
		ArrayList<IFilterRuleType> rules = new ArrayList<>(this.filterByName.values());
		rules.sort(FILTER_COMPARATOR);

		int size = rules.size();
		for (int i = 0; i < size; i++) {
			IFilterRuleType rule = rules.get(i);
			this.filterIDByName.put(rule.getId(), i);
			this.filterByID.put(i, rule);
		}
	}

	@Override
	public Collection<IFilterRuleType> getRules() {
		return this.filterByName.values();
	}

	@Override
	public IFilterRuleType getDefaultRule() {
		return DefaultFilterRuleType.CLOSED;
	}

	@Nullable
	@Override
	public IFilterRuleType getRule(String uid) {
		return this.filterByName.get(uid);
	}

	@Override
	public int getId(IFilterRuleType rule) {
		return this.filterIDByName.get(rule.getId());
	}

	@Nullable
	@Override
	public IFilterRuleType getRule(int id) {
		return this.filterByID.get(id);
	}
}
