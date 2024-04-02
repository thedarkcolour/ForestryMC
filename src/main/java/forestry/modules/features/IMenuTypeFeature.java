package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraftforge.network.IContainerFactory;

import forestry.api.core.IContainerTypeProvider;
import net.minecraftforge.registries.RegisterEvent;

public interface IMenuTypeFeature<C extends AbstractContainerMenu> extends IContainerTypeProvider<C>, IModFeature {

}
