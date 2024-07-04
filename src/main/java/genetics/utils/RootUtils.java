package genetics.utils;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IIndividual;
import genetics.api.root.IIndividualRoot;
import genetics.api.root.IRootDefinition;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.IRootComponent;

public class RootUtils {
	public static boolean hasRoot(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().getSpeciesRoot(stack).isPresent();
	}

	public static <R extends IIndividualRoot> IRootDefinition<R> getRoot(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().getSpeciesRoot(stack);
	}

	public static boolean isIndividual(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().isIndividual(stack);
	}

	@Nullable
	public static IIndividual getIndividual(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().getIndividual(stack);
	}

	@Nullable
	public static IIndividual getIndividualOr(ItemStack stack, IIndividual fallback) {
		IIndividual individual = getIndividual(stack);
		return individual != null ? individual : fallback;
	}

	@SuppressWarnings("unchecked")
	public static <C extends IRootComponent> C getComponent(IIndividual individual, ComponentKey<C> key) {
		return (C) individual.getRoot().getComponent(key);
	}
}
