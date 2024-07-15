package genetics.root;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.ComponentKeys;
import genetics.api.root.translator.IBlockTranslator;
import genetics.api.root.translator.IIndividualTranslator;
import genetics.api.root.translator.IItemTranslator;

public class IndividualTranslator<I extends IIndividual> implements IIndividualTranslator<I> {
	private final ISpeciesType<I> root;
	private final Map<Item, IItemTranslator<I>> itemTranslators = new HashMap<>();
	private final Map<Block, IBlockTranslator<I>> blockTranslators = new HashMap<>();

	public IndividualTranslator(ISpeciesType<I> root) {
		this.root = root;
	}

	@Override
	public ISpeciesType<I> getRoot() {
		return root;
	}

	@Override
	public IIndividualTranslator<I> registerTranslator(IBlockTranslator<I> translator, Block... translatorKeys) {
		for (Block key : translatorKeys) {
			blockTranslators.put(key, translator);
		}
		return this;
	}

	@Override
	public IIndividualTranslator<I> registerTranslator(IItemTranslator<I> translator, Item... translatorKeys) {
		for (Item key : translatorKeys) {
			itemTranslators.put(key, translator);
		}
		return this;
	}

	@Override
	public IItemTranslator<I> getTranslator(Item translatorKey) {
		return itemTranslators.get(translatorKey);
	}

	@Override
	public IBlockTranslator<I> getTranslator(Block translatorKey) {
		return blockTranslators.get(translatorKey);
	}

	@Nullable
	@Override
	public I translateMember(BlockState state) {
		IBlockTranslator<I> translator = getTranslator(state.getBlock());
		return translator == null ? null : translator.getIndividualFromObject(state);
	}

	@Nullable
	@Override
	public I translateMember(ItemStack stack) {
		IItemTranslator<I> translator = getTranslator(stack.getItem());
		return translator == null ? null : translator.getIndividualFromObject(stack);
	}

	@Override
	public ItemStack getGeneticEquivalent(BlockState state) {
		IBlockTranslator<I> translator = getTranslator(state.getBlock());
		return translator == null ? ItemStack.EMPTY : translator.getGeneticEquivalent(state);
	}

	@Override
	public ItemStack getGeneticEquivalent(ItemStack stack) {
		IItemTranslator<I> translator = getTranslator(stack.getItem());
		return translator == null ? ItemStack.EMPTY : translator.getGeneticEquivalent(stack);
	}

	@Override
	public ComponentKey<IIndividualTranslator> getKey() {
		return ComponentKeys.TRANSLATORS;
	}
}
