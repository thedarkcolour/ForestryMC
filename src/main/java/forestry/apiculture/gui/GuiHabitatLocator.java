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
package forestry.apiculture.gui;

import com.google.common.collect.LinkedListMultimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.biome.Biome;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import forestry.api.ForestryTags;
import forestry.apiculture.inventory.ItemInventoryHabitatLocator;
import forestry.core.config.Constants;
import forestry.core.gui.GuiForestry;
import forestry.core.render.ColourProperties;

public class GuiHabitatLocator extends GuiForestry<ContainerHabitatLocator> {
	private static final LinkedListMultimap<String, TagKey<Biome>> habitats = LinkedListMultimap.create();

	static {
		habitats.putAll("Ocean", Arrays.asList(ForestryTags.Biomes.OCEAN_CATEGORY, ForestryTags.Biomes.BEACH_CATEGORY));
		habitats.put("Plains", ForestryTags.Biomes.PLAINS_CATEGORY);
		habitats.put("Desert", ForestryTags.Biomes.DESERT_CATEGORY);
		habitats.putAll("Forest", Arrays.asList(ForestryTags.Biomes.FOREST_CATEGORY, ForestryTags.Biomes.RIVER_CATEGORY));
		habitats.put("Jungle", ForestryTags.Biomes.JUNGLE_CATEGORY);
		habitats.put("Taiga", ForestryTags.Biomes.TAIGA_CATEGORY);
		habitats.put("Hills", ForestryTags.Biomes.EXTREME_HILLS_CATEGORY);
		habitats.put("Swamp", ForestryTags.Biomes.SWAMP_CATEGORY);
		habitats.put("Snow", ForestryTags.Biomes.ICY_CATEGORY);
		habitats.put("Mushroom", ForestryTags.Biomes.MUSHROOM_CATEGORY);
		habitats.put("Nether", ForestryTags.Biomes.NETHER_CATEGORY);
		habitats.put("End", ForestryTags.Biomes.THE_END_CATEGORY);
	}

	private final ItemInventoryHabitatLocator itemInventory;
	private final List<HabitatSlot> habitatSlots = new ArrayList<>(habitats.size());

	private int startX;
	private int startY;

	public GuiHabitatLocator(ContainerHabitatLocator container, Inventory playerInv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/biomefinder.png", container, playerInv, title);

		this.itemInventory = container.getItemInventory();
		this.imageWidth = 176;
		this.imageHeight = 184;

		int slot = 0;
		for (String habitatName : habitats.keySet()) {
			int x;
			int y;
			if (slot > 5) {
				x = 18 + (slot - 6) * 20;
				y = 50;
			} else {
				x = 18 + slot * 20;
				y = 32;
			}
			List<TagKey<Biome>> biomes = habitats.get(habitatName);
			// todo hack fix, need to add two more slots to the screen for ocean and river
			HabitatSlot habitatSlot = new HabitatSlot(widgetManager, x, y, habitatName, biomes.get(0));
			habitatSlots.add(habitatSlot);
			widgetManager.add(habitatSlot);
			slot++;
		}
	}


	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseY, int mouseX) {
		//super.renderBg(transform, partialTicks, mouseY, mouseX);

		// todo
		Component str = Component.translatable("item.forestry.habitat_locator")/*.toUpperCase(Locale.ENGLISH)*/;
		getFontRenderer().draw(transform, str, startX + 8 + textLayout.getCenteredOffset(str, 138), startY + 16, ColourProperties.INSTANCE.get("gui.screen"));

		// Set active according to valid biomes.
		HashSet<TagKey<Biome>> activeBiomeTypes = new HashSet<>();

		for (Holder<Biome> biome : itemInventory.getBiomesToSearch()) {
			activeBiomeTypes.addAll(biome.getTagKeys().toList());
		}

		for (HabitatSlot habitatSlot : habitatSlots) {
			habitatSlot.setActive(activeBiomeTypes);
		}

		for (HabitatSlot slot : habitatSlots) {
			slot.draw(transform, startX, startY);
		}

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset afterwards.
	}

	@Override
	public void init() {
		super.init();

		startX = (this.width - this.imageWidth) / 2;
		startY = (this.height - this.imageHeight) / 2;
	}

	@Override
	protected void addLedgers() {
		addErrorLedger(itemInventory);
		addHintLedger("habitat.locator");
	}
}
