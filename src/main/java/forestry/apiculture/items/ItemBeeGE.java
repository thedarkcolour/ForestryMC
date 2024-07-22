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

import forestry.api.IForestryApi;
import forestry.api.apiculture.BeeManager;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import forestry.api.apiculture.genetics.IBeeSpecies;
import forestry.api.apiculture.genetics.BeeLifeStage;
import forestry.api.apiculture.genetics.IAlleleBeeSpecies;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.genetics.IBeeSpeciesType;
import forestry.api.core.ItemGroups;
import forestry.api.genetics.ForestrySpeciesTypes;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.alleles.BeeChromosomes;
import forestry.core.config.Config;
import forestry.core.genetics.ItemGE;
import forestry.core.items.definitions.IColoredItem;

import genetics.api.GeneticHelper;

public class ItemBeeGE extends ItemGE implements IColoredItem {
	private final BeeLifeStage type;

	public ItemBeeGE(BeeLifeStage type) {
		super(type != BeeLifeStage.DRONE ? new Item.Properties().tab(ItemGroups.tabApiculture).durability(1) : new Item.Properties().tab(ItemGroups.tabApiculture));
		this.type = type;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return GeneticHelper.createOrganism(stack, type, BeeManager.beeRoot.gegettDefinition());
	}

	@Override
	protected ISpecies<?> getSpecies(ItemStack itemStack) {
		return GeneticHelper.getOrganism(itemStack).getAllele(BeeChromosomes.SPECIES, true);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (!stack.hasTag()) {
			return;
		}

		if (type != BeeLifeStage.DRONE) {
			IBee individual = GeneticHelper.getIndividual(stack);
			if (individual == null) {
				return;
			}

			if (individual.isPristine()) {
				list.add(Component.translatable("for.bees.stock.pristine").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
			} else {
				list.add(Component.translatable("for.bees.stock.ignoble").withStyle(ChatFormatting.YELLOW));
			}
		}

		super.appendHoverText(stack, level, list, flag);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> subItems) {
		if (this.allowedIn(tab)) {
			addCreativeItems(subItems, true);
		}
	}

	public void addCreativeItems(NonNullList<ItemStack> subItems, boolean hideSecrets) {
		//so need to adjust init sequence
		IBeeSpeciesType type = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(ForestrySpeciesTypes.BEE);
		for (IBeeSpecies bee : type.getSpecies()) {
			// Don't show secret bees unless ordered to.
			if (hideSecrets && bee.isSecret() && !Config.isDebug) {
				continue;
			}
			ItemStack stack = new ItemStack(this);
			bee.copyTo(stack);
			subItems.add(stack);
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int tintIndex) {
		if (!itemstack.hasTag()) {
			if (tintIndex == 1) {
				return 0xffdc16;
			} else {
				return 0xffffff;
			}
		}

		IAlleleBeeSpecies species = getSpecies(itemstack);
		return species.getSpriteColour(tintIndex);
	}

	public final BeeLifeStage getType() {
		return type;
	}
}
