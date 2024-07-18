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
package forestry.climatology.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.ForestryConstants;
import forestry.api.climate.ClimateState;
import forestry.api.climate.IClimateTransformer;
import forestry.api.core.HumidityType;
import forestry.api.core.TemperatureType;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.core.gui.elements.GuiElement;

import genetics.api.individual.IIndividual;
import genetics.utils.RootUtils;

@OnlyIn(Dist.CLIENT)
public class SpeciesSelectionElement extends GuiElement {

	private final IClimateTransformer transformer;

	public SpeciesSelectionElement(int xPos, int yPos, IClimateTransformer transformer) {
		super(xPos, yPos);
		setSize(22, 22);
		this.transformer = transformer;
		/*addSelfEventHandler(GuiEvent.DownEvent.class, event -> {
			PlayerEntity player = Minecraft.getInstance().player;
			ItemStack itemstack = player.inventoryMenu.getCarried();
			if (itemstack.isEmpty()) {
				return;
			}
			Optional<IIndividual> optional = RootUtils.getIndividual(itemstack);
			if (!optional.isPresent()) {
				return;
			}
			IIndividual individual = optional.get();
			IAlleleForestrySpecies primary = individual.getGenome().getPrimary(IAlleleForestrySpecies.class);
			EnumTemperature temperature = primary.temperature();
			EnumHumidity humidity = primary.humidity();
			float temp;
			float humid;
			switch (temperature) {
				case HELLISH:
					temp = 2.0F;
					break;
				case HOT:
					temp = 1.25F;
					break;
				case WARM:
					temp = 0.9F;
					break;
				case COLD:
					temp = 0.15F;
					break;
				case ICY:
					temp = 0.0F;
					break;
				case NORMAL:
				default:
					temp = 0.79F;
					break;
			}
			switch (humidity) {
				case DAMP:
					humid = 0.9F;
					break;
				case ARID:
					humid = 0.2F;
					break;
				case NORMAL:
				default:
					humid = 0.4F;
					break;
			}
			transformer.setTarget(ClimateStateHelper.INSTANCE.create(temp, humid));
		});*/
	}

	@Override
	public boolean onMouseClicked(double mouseX, double mouseY, int mouseButton) {
		Player player = Minecraft.getInstance().player;
		ItemStack itemstack = player.inventoryMenu.getCarried();
		if (itemstack.isEmpty()) {
			return false;
		}
		IIndividual individual = RootUtils.getIndividual(itemstack);
		if (individual == null) {
			return false;
		}
		IAlleleForestrySpecies primary = individual.getGenome().getPrimarySpecies(IAlleleForestrySpecies.class);
		TemperatureType temperature = primary.getTemperature();
		HumidityType humidity = primary.getHumidity();
		transformer.setTarget(new ClimateState(temperature, humidity));
		return true;
	}

	@Override
	public void drawElement(PoseStack transform, int mouseX, int mouseY) {
		super.drawElement(transform, mouseX, mouseY);
		RenderSystem.setShaderTexture(0, ForestryConstants.forestry("textures/gui/habitat_former.png"));
		// RenderSystem.enableAlphaTest();
		blit(transform, 0, 0, 224, 46, 22, 22);
		// RenderSystem.disableAlphaTest();
	}

	@Override
	public boolean canMouseOver() {
		return true;
	}
}
