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

import org.apache.commons.lang3.mutable.MutableBoolean;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import forestry.api.ForestryCapabilities;
import forestry.api.core.tooltips.ToolTip;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.apiculture.DisplayHelper;
import forestry.apiculture.genetics.IGeneticTooltipProvider;
import forestry.core.config.Config;
import forestry.core.items.ItemForestry;
import forestry.core.utils.GeneticsUtil;

public abstract class ItemGE extends ItemForestry {
	protected ItemGE(Item.Properties properties) {
		super(properties.setNoRepair());
	}

	protected abstract ISpecies<?> getSpecies(ItemStack stack);

	protected abstract ILifeStage getStage();

	@Override
	public Component getName(ItemStack stack) {
		return stack.getCapability(ForestryCapabilities.INDIVIDUAL)
				.map(handler -> GeneticsUtil.getItemName(handler.getStage(), handler.getIndividual().getSpecies()))
				.orElseGet(() -> super.getName(stack));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (!stack.hasTag()) { // villager trade wildcard bees
			return false;
		}
		ISpecies<?> species = getSpecies(stack);
		return species.hasGlint();
	}

	public static void appendGeneticsTooltip(ItemStack stack, ILifeStage organismType, List<Component> tooltip) {
		if (!stack.hasTag()) {
			return;
		}

		MutableBoolean analyzed = new MutableBoolean();
		IIndividualHandler.ifPresent(stack, individual -> {
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

				analyzed.setTrue();
			}
		});
		if (analyzed.isFalse()) {
			tooltip.add(Component.translatable("for.gui.unknown", "< %s >").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		appendGeneticsTooltip(stack, getStage(), tooltip);
	}

	@Override
	public String getCreatorModId(ItemStack stack) {
		ISpecies<?> species = getSpecies(stack);
		return species.id().getNamespace();
	}

	public static <S extends ISpecies<I>, I extends IIndividual> void addCreativeItems(ILifeStage stage, List<ItemStack> subItems, boolean hideSecrets, ISpeciesType<S, I> type) {
		for (S species : type.getAllSpecies()) {
			// Don't show secrets unless ordered to.
			if (hideSecrets && species.isSecret() && !Config.isDebug) {
				continue;
			}

			subItems.add(species.createStack(species.createIndividual(), stage));
		}
	}
}
