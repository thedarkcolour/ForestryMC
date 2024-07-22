package forestry.core.genetics.analyzer;

import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.ISpecies;
import forestry.core.gui.GuiConstants;
import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.DatabaseElement;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.gui.elements.layouts.LayoutHelper;

import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutation;

public class MutationsTab<I extends IIndividual> extends DatabaseTab<I> {
	public MutationsTab(Supplier<ItemStack> stackSupplier) {
		super("mutations", stackSupplier);
	}

	@Override
	public void createElements(DatabaseElement container, IIndividual individual, ItemStack itemStack) {
		IGenome genome = individual.getGenome();
		ISpeciesType<?> speciesRoot = individual.getType();
		ISpecies<?> species = genome.getActiveValue(speciesRoot.getKaryotype().getSpeciesChromosome());
		IMutationManager mutationContainer = speciesRoot.getMutations();

		Player player = Minecraft.getInstance().player;
		IBreedingTracker<?> breedingTracker = speciesRoot.getBreedingTracker(player.level, player.getGameProfile());

		LayoutHelper groupHelper = container.layoutHelper((x, y) -> GuiElementFactory.horizontal(16, 0, new Insets(0, 1, 0, 0)), 100, 16);
		Collection<? extends IMutation<?>> mutations = getValidMutations(mutationContainer.getMutationsFrom(species));
		if (!mutations.isEmpty()) {
			container.label(Component.translatable("for.gui.database.mutations.further"), Alignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
			mutations.forEach(mutation -> groupHelper.add(GuiElementFactory.INSTANCE.createMutation(0, 0, 50, 16, mutation, species, breedingTracker)));
			groupHelper.finish(true);
		}
		mutations = getValidMutations(mutationContainer.getResultantMutations(species));
		if (mutations.isEmpty()) {
			return;
		}
		container.label(Component.translatable("for.gui.database.mutations.resultant"), Alignment.TOP_CENTER, GuiConstants.UNDERLINED_STYLE);
		mutations.forEach(mutation -> groupHelper.add(GuiElementFactory.INSTANCE.createMutationResultant(0, 0, 50, 16, mutation, breedingTracker)));
		groupHelper.finish(true);
	}

	private Collection<? extends IMutation<?>> getValidMutations(List<? extends IMutation<?>> mutations) {
		mutations.removeIf(IMutation::isSecret);
		return mutations;
	}
}
