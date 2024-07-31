/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.genetics;

import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IAlyzerPlugin {
	void drawAnalyticsPage1(PoseStack transform, Screen gui, ItemStack stack);

	void drawAnalyticsPage2(PoseStack transform, Screen gui, ItemStack stack);

	void drawAnalyticsPage3(PoseStack transform, Screen gui, ItemStack stack);

	/**
	 * The hints that will be shown in the alyzer gui.
	 */
	List<String> getHints();

	/**
	 * @return Icon stacks used by this plugin for rendering.
	 */
	Map<ISpecies<?>, ItemStack> getIconStacks();
}
