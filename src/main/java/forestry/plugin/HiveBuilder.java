package forestry.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.apiculture.hives.IHiveDrop;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.plugin.IHiveBuilder;
import forestry.apiculture.genetics.HiveDrop;
import forestry.core.utils.SpeciesUtil;

public class HiveBuilder implements IHiveBuilder {
	private final ArrayList<Supplier<HiveDrop>> drops = new ArrayList<>();

	@Override
	public IHiveBuilder addDrop(double chance, ResourceLocation speciesId, Supplier<List<ItemStack>> extraItems, float ignobleChance, Map<IChromosome<?>, IAllele> alleles) {
		this.drops.add(() -> new HiveDrop(chance, SpeciesUtil.getBeeSpecies(speciesId), extraItems.get(), ignobleChance, alleles));
		return this;
	}

	@Override
	public IHiveBuilder addCustomDrop(Supplier<IHiveDrop> drop) {
		return null;
	}
}
