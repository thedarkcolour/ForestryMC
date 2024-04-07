package forestry.lepidopterology.genetics.alleles;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import forestry.api.lepidopterology.genetics.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IAlleleButterflyCocoon;
import forestry.api.lepidopterology.genetics.IAlleleButterflyEffect;
import forestry.core.features.CoreItems;
import forestry.core.items.definitions.EnumCraftingMaterial;

import genetics.api.alleles.IAlleleRegistry;

public class ButterflyAlleles {
	public static final IAlleleButterflyCocoon DEFAULT_COCOON = new AlleleButterflyCocoon("default", false);
	public static final IAlleleButterflyCocoon SILK_COCOON = new AlleleButterflyCocoon("silk", false);
	public static final List<IAlleleButterflyCocoon> cocoonAlleles = List.of(DEFAULT_COCOON, SILK_COCOON);

	public static final IAlleleButterflyEffect butterflyNone = new AlleleButterflyEffectNone();

	public static void registerAlleles(IAlleleRegistry registry) {
		for (IAlleleButterflyCocoon cocoonAllele : cocoonAlleles) {
			registry.registerAllele(cocoonAllele, ButterflyChromosomes.COCOON);
		}
		registry.registerAllele(butterflyNone, ButterflyChromosomes.EFFECT);
	}

	public static void createLoot() {
		DEFAULT_COCOON.addLoot(new ItemStack(Items.STRING, 2), 1F);
		DEFAULT_COCOON.addLoot(new ItemStack(Items.STRING), 0.75F);
		DEFAULT_COCOON.addLoot(new ItemStack(Items.STRING, 3), 0.25F);

		SILK_COCOON.addLoot(CoreItems.CRAFTING_MATERIALS.stack(EnumCraftingMaterial.SILK_WISP, 3), 0.75F);
		SILK_COCOON.addLoot(CoreItems.CRAFTING_MATERIALS.stack(EnumCraftingMaterial.SILK_WISP, 2), 0.25F);
	}
}
