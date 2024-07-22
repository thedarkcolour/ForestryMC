package forestry.arboriculture.genetics.alleles;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import forestry.api.genetics.IAlleleRegistry;

import genetics.utils.AlleleUtils;

import forestry.api.arboriculture.genetics.IFruit;
import forestry.api.genetics.alleles.TreeChromosomes;
import forestry.arboriculture.DummyFruit;
import forestry.arboriculture.PodFruit;
import forestry.arboriculture.PodFruit.EnumPodType;
import forestry.arboriculture.RipeningFruit;
import forestry.core.features.CoreItems;
import forestry.core.items.ItemFruit.EnumFruit;

import static forestry.api.arboriculture.EnumFruitFamily.JUNGLE;
import static forestry.api.arboriculture.EnumFruitFamily.NONE;
import static forestry.api.arboriculture.EnumFruitFamily.NUX;
import static forestry.api.arboriculture.EnumFruitFamily.POMES;
import static forestry.api.arboriculture.EnumFruitFamily.PRUNES;

public class AlleleFruits {
	public static final IFruit fruitNone;
	public static final IFruit fruitApple;
	public static final IFruit fruitCocoa;
	public static final IFruit fruitChestnut;
	public static final IFruit fruitWalnut;
	public static final IFruit fruitCherry;
	public static final IFruit fruitDates;
	public static final IFruit fruitPapaya;
	public static final IFruit fruitLemon;
	public static final IFruit fruitPlum;
	private static final List<IFruit> fruitAlleles;
	@Nullable
	private static List<IFruit> fruitAllelesWithModels;

	static {
		ItemStack cocoaBean = new ItemStack(Items.COCOA_BEANS);

		fruitAlleles = Arrays.asList(
			fruitNone = new Fruit("none", new DummyFruit("for.fruits.none", NONE)),
			fruitApple = new Fruit("apple", new RipeningFruit("for.fruits.apple", POMES, () -> new ItemStack(Items.APPLE), 1.0f)
				.setColours(new Color(0xff2e2e), new Color(0xE3F49C))
				.setOverlay("pomes")),
			fruitCocoa = new Fruit("cocoa", new PodFruit("for.fruits.cocoa", JUNGLE, EnumPodType.COCOA, () -> cocoaBean)),
			// .setColours(0xecdca5, 0xc4d24a), true)
			fruitChestnut = new Fruit("chestnut", new RipeningFruit("for.fruits.chestnut", NUX, () -> CoreItems.FRUITS.stack(EnumFruit.CHESTNUT, 1), 1.0f)
				.setRipeningPeriod(6)
				.setColours(new Color(0x7f333d), new Color(0xc4d24a))
				.setOverlay("nuts"), true),
			fruitWalnut = new Fruit("walnut", new RipeningFruit("for.fruits.walnut", NUX, () -> CoreItems.FRUITS.stack(EnumFruit.WALNUT, 1), 1.0f)
				.setRipeningPeriod(8)
				.setColours(new Color(0xfba248), new Color(0xc4d24a))
				.setOverlay("nuts"), true),
			fruitCherry = new Fruit("cherry", new RipeningFruit("for.fruits.cherry", PRUNES, () -> CoreItems.FRUITS.stack(EnumFruit.CHERRY, 1), 1.0f)
				.setColours(new Color(0xff2e2e), new Color(0xc4d24a))
				.setOverlay("berries"), true),
			fruitDates = new Fruit("dates", new PodFruit("for.fruits.dates", JUNGLE, EnumPodType.DATES, () -> {
				return CoreItems.FRUITS.stack(EnumFruit.DATES, 4);
			})),
			fruitPapaya = new Fruit("papaya", new PodFruit("for.fruits.papaya", JUNGLE, EnumPodType.PAPAYA, () -> CoreItems.FRUITS.stack(EnumFruit.PAPAYA, 1))),
			fruitLemon = new Fruit("lemon", new RipeningFruit("for.fruits.lemon", PRUNES, () -> CoreItems.FRUITS.stack(EnumFruit.LEMON, 1), 1.0f)
				.setColours(new Color(0xeeee00), new Color(0x99ff00))
				.setOverlay("citrus"), true),
			fruitPlum = new Fruit("plum", new RipeningFruit("for.fruits.plum", PRUNES, () -> CoreItems.FRUITS.stack(EnumFruit.PLUM, 1), 1.0f)
				.setColours(new Color(0x663446), new Color(0xeeff1a))
				.setOverlay("plums"), true)
		);
	}

	public static void registerAlleles(IAlleleRegistry registry) {
		for (IFruit fruitAllele : fruitAlleles) {
			registry.registerAllele(fruitAllele, TreeChromosomes.FRUITS);
		}
	}

	public static List<IFruit> getFruitAlleles() {
		return fruitAlleles;
	}

	public static List<IFruit> getFruitAllelesWithModels() {
		if (fruitAllelesWithModels == null) {
			fruitAllelesWithModels = new ArrayList<>();
			AlleleUtils.filteredStream(TreeChromosomes.FRUITS)
				.filter((allele) -> allele.getModelName() != null)
				.forEach((allele) -> fruitAllelesWithModels.add(allele));
		}
		return fruitAllelesWithModels;
	}
}
