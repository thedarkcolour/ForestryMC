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
package forestry.apiculture.items;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.core.ItemGroups;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.core.config.Config;
import forestry.core.genetics.SerializableIndividualHandler;
import forestry.core.genetics.ItemGE;
import forestry.core.items.definitions.IColoredItem;
import forestry.core.utils.SpeciesUtil;

public class ItemBeeGE extends ItemGE implements IColoredItem {
	private final BeeLifeStage type;

	public ItemBeeGE(BeeLifeStage type) {
		super(type != BeeLifeStage.DRONE ? new Item.Properties().tab(ItemGroups.tabApiculture).durability(1) : new Item.Properties().tab(ItemGroups.tabApiculture));
		this.type = type;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new SerializableIndividualHandler(stack, SpeciesUtil.BEE_TYPE.get().createIndividual(nbt, this.type));
	}

	@Override
	protected IBeeSpecies getSpecies(ItemStack stack) {
		return GeneticHelper.getOrganism(stack).getAllele(BeeChromosomes.SPECIES, true);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (!stack.hasTag()) {
			return;
		}

		if (this.type != BeeLifeStage.DRONE) {
			IIndividualHandler.ifPresent(stack, individual -> {
				if (((IBee) individual).isPristine()) {
					list.add(Component.translatable("for.bees.stock.pristine").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
				} else {
					list.add(Component.translatable("for.bees.stock.ignoble").withStyle(ChatFormatting.YELLOW));
				}
			});
		}

		super.appendHoverText(stack, level, list, flag);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> subItems) {
		if (allowedIn(tab)) {
			addCreativeItems(subItems, true);
		}
	}

	public void addCreativeItems(List<ItemStack> subItems, boolean hideSecrets) {
		//so need to adjust init sequence
		for (IBeeSpecies species : SpeciesUtil.getAllBeeSpecies()) {
			// Don't show secret bees unless ordered to.
			if (hideSecrets && species.isSecret() && !Config.isDebug) {
				continue;
			}
			subItems.add(species.createStack(this.type));
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int tintIndex) {
		if (!itemstack.hasTag()) {
			if (tintIndex == 1) {
				return 0xffdc16;
			} else if (tintIndex == 2) {
				// 2 = stripes
				return 0;
			} else {
				// 0 = outline
				return 0xffffff;
			}
		} else {
			IBeeSpecies species = getSpecies(itemstack);

			return switch (tintIndex) {
				case 2 -> species.getStripes();
				case 1 -> species.getBody();
				default -> species.getOutline();
			};
		}
	}

	public final BeeLifeStage getStage() {
		return type;
	}
}
