package genetics.utils;

import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import genetics.api.GeneticsAPI;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ISpeciesType;
import genetics.api.root.IRootDefinition;
import genetics.api.root.components.ComponentKey;
import genetics.api.root.components.IRootComponent;

public class RootUtils {
	public static boolean hasRoot(ItemStack stack) {
		return getRoot(stack) != null;
	}

	@Nullable
	public static <R extends ISpeciesType<?>> R getRoot(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().getSpeciesRoot(stack);
	}

	public static boolean isIndividual(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().isIndividual(stack);
	}

	@Nullable
	public static IIndividual getIndividual(ItemStack stack) {
		return GeneticsAPI.apiInstance.getRootHelper().getIndividual(stack);
	}

	@SuppressWarnings("unchecked")
	public static <C extends IRootComponent> C getComponent(IIndividual individual, ComponentKey<C> key) {
		return (C) individual.getRoot().getComponent(key);
	}
}
