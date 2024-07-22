/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.inventory;

import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import forestry.api.core.IErrorSource;
import forestry.api.core.IError;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.ISpeciesType;
import forestry.apiculture.features.ApicultureItems;
import forestry.api.core.ForestryError;
import forestry.core.utils.GeneticsUtil;

public class ItemInventoryAlyzer extends ItemInventory implements IErrorSource {
	public static final int SLOT_ENERGY = 0;
	public static final int SLOT_SPECIMEN = 1;
	public static final int SLOT_ANALYZE_1 = 2;
	public static final int SLOT_ANALYZE_2 = 3;
	public static final int SLOT_ANALYZE_3 = 4;
	public static final int SLOT_ANALYZE_4 = 5;
	public static final int SLOT_ANALYZE_5 = 6;

	public ItemInventoryAlyzer(Player player, ItemStack itemstack) {
		super(player, 7, itemstack);
	}

	public static boolean isAlyzingFuel(ItemStack itemstack) {
		if (itemstack.isEmpty()) {
			return false;
		}

		return ApicultureItems.HONEY_DROPS.itemEqual(itemstack) || ApicultureItems.HONEYDEW.itemEqual(itemstack);
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		if (slotIndex == SLOT_ENERGY) {
			return isAlyzingFuel(itemStack);
		}

		// only allow one slot to be used at a time
		if (hasSpecimen() && getItem(slotIndex).isEmpty()) {
			return false;
		}

		itemStack = GeneticsUtil.convertToGeneticEquivalent(itemStack);
		ISpeciesType<IIndividual> speciesRoot = RootUtils.getRoot(itemStack);
		if (speciesRoot == null) {
			return false;
		}

		if (slotIndex == SLOT_SPECIMEN) {
			return true;
		}

		IIndividual individual = speciesRoot.create(itemStack);
		return individual != null && individual.isAnalyzed();
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		super.setItem(index, itemStack);
		if (index == SLOT_SPECIMEN) {
			analyzeSpecimen(itemStack);
		} else if (index == SLOT_ENERGY) {
			analyzeSpecimen(getItem(SLOT_SPECIMEN));
		}
	}

	private void analyzeSpecimen(ItemStack specimen) {
		if (specimen.isEmpty()) {
			return;
		}

		ItemStack convertedSpecimen = GeneticsUtil.convertToGeneticEquivalent(specimen);
		if (!ItemStack.matches(specimen, convertedSpecimen)) {
			setItem(SLOT_SPECIMEN, convertedSpecimen);
			specimen = convertedSpecimen;
		}

		ISpeciesType<IIndividual> speciesRoot = RootUtils.getRoot(specimen);
		// No individual, abort
		if (speciesRoot == null) {
			return;
		}

		IIndividual individual = speciesRoot.create(specimen);

		// Analyze if necessary
		if (individual != null) {
			if (!individual.isAnalyzed()) {
				// Requires energy
				if (!isAlyzingFuel(getItem(SLOT_ENERGY))) {
					return;
				}

				if (individual.analyze()) {
					IBreedingTracker breedingTracker = speciesRoot.getBreedingTracker(player.level, player.getGameProfile());
					breedingTracker.registerSpecies(individual.getGenome().getPrimarySpecies());
					breedingTracker.registerSpecies(individual.getGenome().getSecondarySpecies());

					individual.copyTo(specimen);

					// Decrease energy
					removeItem(SLOT_ENERGY, 1);
				}
			}
		}

		setItem(SLOT_ANALYZE_1, specimen);
		setItem(SLOT_SPECIMEN, ItemStack.EMPTY);
	}

	@Override
	public Set<IError> getErrors() {
		if (!hasSpecimen()) {
			return Set.of(ForestryError.NO_SPECIMEN);
		} else {
			ISpeciesType<IIndividual> definition = RootUtils.getRoot(getSpecimen());
			if (definition != null && !isAlyzingFuel(getItem(SLOT_ENERGY))) {
				return Set.of(ForestryError.NO_HONEY);
			}
		}

		return Set.of();
	}

	public ItemStack getSpecimen() {
		for (int i = SLOT_SPECIMEN; i <= SLOT_ANALYZE_5; i++) {
			ItemStack itemStack = getItem(i);
			if (!itemStack.isEmpty()) {
				return itemStack;
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean hasSpecimen() {
		return !getSpecimen().isEmpty();
	}

	@Override
	protected void onWriteNBT(CompoundTag nbt) {
		ItemStack energy = getItem(ItemInventoryAlyzer.SLOT_ENERGY);
		int amount = 0;
		if (!energy.isEmpty()) {
			amount = energy.getCount();
		}
		nbt.putInt("Charges", amount);
	}
}
