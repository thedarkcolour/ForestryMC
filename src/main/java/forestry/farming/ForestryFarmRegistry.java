package forestry.farming;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import forestry.api.farming.IFarmProperties;
import forestry.api.farming.IFarmPropertiesBuilder;
import forestry.api.farming.IFarmRegistry;
import forestry.api.farming.IFarmable;
import forestry.api.farming.IFarmableInfo;
import forestry.farming.logic.FarmProperties;
import forestry.farming.logic.farmables.FarmableInfo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class ForestryFarmRegistry implements IFarmRegistry {
	private final Multimap<String, IFarmable> farmables = HashMultimap.create();
	private final Map<String, IFarmableInfo> farmableInfo = new LinkedHashMap<>();
	private final Object2IntOpenHashMap<Ingredient> fertilizers = new Object2IntOpenHashMap<>();
	private final Map<String, IFarmProperties> farmInstances = new HashMap<>();
	private final Map<String, IFarmPropertiesBuilder> propertiesBuilders = new HashMap<>();

	@Override
	public void registerFarmables(String identifier, IFarmable... farmablesArray) {
		IFarmableInfo info = getFarmableInfo(identifier);
		for (IFarmable farmable : farmablesArray) {
			farmable.addInformation(info);
		}
		farmables.putAll(identifier, Arrays.asList(farmablesArray));
	}

	@Override
	public Collection<IFarmable> getFarmables(String identifier) {
		return farmables.get(identifier);
	}

	@Override
	public IFarmableInfo getFarmableInfo(String identifier) {
		return farmableInfo.computeIfAbsent(identifier, FarmableInfo::new);
	}

	@Override
	public void registerFertilizer(Ingredient fertilizer, int value) {
		if (fertilizer.isEmpty()) {
			return;
		}
		fertilizers.put(fertilizer, value);
	}

	@Override
	public int getFertilizeValue(ItemStack stack) {
		for (Object2IntMap.Entry<Ingredient> fertilizer : this.fertilizers.object2IntEntrySet()) {
			if (fertilizer.getKey().test(stack)) {
				return fertilizer.getIntValue();
			}
		}
		return 0;
	}

	@Override
	public IFarmPropertiesBuilder getPropertiesBuilder(String identifier) {
		return propertiesBuilders.computeIfAbsent(identifier, FarmProperties.Builder::new);
	}

	@Override
	public IFarmProperties registerProperties(String identifier, IFarmProperties properties) {
		farmInstances.put(identifier, properties);
		return properties;
	}

	@Override
	@Nullable
	public IFarmProperties getProperties(String identifier) {
		return farmInstances.get(identifier);
	}
}
