package forestry.core.genetics.analyzer;

import java.util.Locale;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.gatgets.IDatabaseTab;
import forestry.core.features.CoreItems;
import forestry.core.gui.elements.DatabaseElement;

public enum AnalyzerTab implements IDatabaseTab<IIndividual> {
	ANALYZE {
		@Override
		public void createElements(DatabaseElement container, IIndividual individual, ILifeStage stage, ItemStack stack) {
		}

		@Override
		public ItemStack getIconStack() {
			return CoreItems.PORTABLE_ALYZER.stack();
		}
	};

	@Override
	public Component getTooltip(IIndividual individual) {
		return Component.translatable("for.gui.database.tab." + name().toLowerCase(Locale.ENGLISH) + ".name");
	}
}
