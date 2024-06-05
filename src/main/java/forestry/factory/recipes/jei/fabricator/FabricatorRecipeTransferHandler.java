package forestry.factory.recipes.jei.fabricator;

import javax.annotation.Nullable;

import forestry.core.recipes.jei.ForestryRecipeType;
import forestry.core.utils.JeiUtil;
import forestry.factory.features.FactoryMenuTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.recipes.IFabricatorRecipe;
import forestry.core.utils.NetworkUtil;
import forestry.factory.gui.ContainerFabricator;
import forestry.factory.network.packets.PacketRecipeTransferRequest;

import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class FabricatorRecipeTransferHandler implements IRecipeTransferHandler<ContainerFabricator, IFabricatorRecipe> {

	@Override
	public Class<ContainerFabricator> getContainerClass() {
		return ContainerFabricator.class;
	}

	@Override
	public Optional<MenuType<ContainerFabricator>> getMenuType() {
		return Optional.of(FactoryMenuTypes.FABRICATOR.menuType());
	}

	@Override
	public RecipeType<IFabricatorRecipe> getRecipeType() {
		return ForestryRecipeType.FABRICATOR;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ContainerFabricator container, IFabricatorRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		if (doTransfer) {
			Container craftingInventory = container.getFabricator().getCraftingInventory();
			NonNullList<ItemStack> items = JeiUtil.getFirstItemStacks(recipeSlots);
			for (int i = 0; i < items.size(); i++) {
				craftingInventory.setItem(i, items.get(i));
			}

			NetworkUtil.sendToServer(new PacketRecipeTransferRequest(container.getFabricator().getBlockPos(), items));
		}

		return null;
	}

}
