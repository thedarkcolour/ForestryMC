package forestry.factory.recipes.jei.carpenter;

import forestry.api.recipes.ICarpenterRecipe;
import forestry.core.recipes.jei.ForestryRecipeType;
import forestry.core.utils.JeiUtil;
import forestry.core.utils.NetworkUtil;
import forestry.factory.features.FactoryMenuTypes;
import forestry.factory.gui.ContainerCarpenter;
import forestry.factory.network.packets.PacketRecipeTransferRequest;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CarpenterRecipeTransferHandler implements IRecipeTransferHandler<ContainerCarpenter, ICarpenterRecipe> {
	@Override
	public Class<ContainerCarpenter> getContainerClass() {
		return ContainerCarpenter.class;
	}


	@Override
	public Optional<MenuType<ContainerCarpenter>> getMenuType() {
		return Optional.of(FactoryMenuTypes.CARPENTER.menuType());
	}

	@Override
	public RecipeType<ICarpenterRecipe> getRecipeType() {
		return ForestryRecipeType.CARPENTER;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ContainerCarpenter container, ICarpenterRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		if (doTransfer) {
			Container craftingInventory = container.getCarpenter().getCraftingInventory();
			NonNullList<ItemStack> items = JeiUtil.getFirstItemStacks(recipeSlots);
			for (int i = 1; i < items.size(); i++) {
				craftingInventory.setItem(i - 1, items.get(i));
			}
			NetworkUtil.sendToServer(new PacketRecipeTransferRequest(container.getCarpenter(), items));
		}

		return null;
	}
}
