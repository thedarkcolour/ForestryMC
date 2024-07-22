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
package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import forestry.api.ForestryCapabilities;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.apiculture.DisplayHelper;
import forestry.apiculture.genetics.IGeneticTooltipProvider;
import forestry.core.config.Config;
import forestry.core.items.ItemForestry;

import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;

public abstract class ItemGE extends ItemForestry {
	protected ItemGE(Item.Properties properties) {
		super(properties.setNoRepair());
	}

	protected abstract ISpecies<?> getSpecies(ItemStack itemStack);

	protected abstract ILifeStage getType();

	@Override
	public Component getName(ItemStack itemStack) {
		if (GeneticHelper.getOrganism(itemStack).isEmpty()) {
			return super.getName(itemStack);
		}
		IAlleleForestrySpecies species = getSpecies(itemStack);

		return species.getItemName(getType());
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (!stack.hasTag()) { // villager trade wildcard bees
			return false;
		}
		IAlleleForestrySpecies species = getSpecies(stack);
		return species.hasEffect();
	}

	public static void appendGeneticsTooltip(ItemStack stack, ILifeStage organismType, List<Component> tooltip) {
		if (!stack.hasTag()) {
			return;
		}

		stack.getCapability(ForestryCapabilities.INDIVIDUAL).ifPresent(individual -> {
			if (individual.isAnalyzed()) {
				if (Screen.hasShiftDown()) {
					ToolTip helper = new ToolTip();
					for (IGeneticTooltipProvider<IIndividual> provider : DisplayHelper.INSTANCE.getTooltips(individual.getType().id(), organismType)) {
						provider.addTooltip(helper, individual.getGenome(), individual);
					}
					if (helper.isEmpty()) {
						individual.addTooltip(tooltip);
					}
					tooltip.addAll(helper.getLines());
				} else {
					tooltip.add(Component.translatable("for.gui.tooltip.tmi", "< %s >").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
				}
			}
		});

		IIndividual individual = GeneticHelper.getIndividual(stack);
		if (individual != null && individual.isAnalyzed()) {

		} else {
			tooltip.add(Component.translatable("for.gui.unknown", "< %s >").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		appendGeneticsTooltip(stack, getType(), tooltip);
	}

	@Override
	public String getCreatorModId(ItemStack itemStack) {
		IAlleleForestrySpecies species = getSpecies(itemStack);
		return species.getId().getNamespace();
	}

	public static <I extends IIndividual & IHasSecrets> void addCreativeItems(Item item, NonNullList<ItemStack> subItems, boolean hideSecrets, ISpeciesType<I> speciesRoot) {
		for (I individual : speciesRoot.getIndividualTemplates()) {
			// Don't show secrets unless ordered to.
			if (hideSecrets && individual.isSecret() && !Config.isDebug) {
				continue;
			}

			ItemStack stack = new ItemStack(item);
			individual.copyTo(stack);
			subItems.add(stack);
		}
	}
}
