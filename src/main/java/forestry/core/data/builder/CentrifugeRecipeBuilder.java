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
package forestry.core.data.builder;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import forestry.api.core.Product;
import forestry.core.utils.JsonUtil;
import forestry.factory.features.FactoryRecipeTypes;

public class CentrifugeRecipeBuilder {
	private int processingTime;
	private Ingredient input;
	private final ArrayList<Product> outputs = new ArrayList<>();

	public CentrifugeRecipeBuilder setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
		return this;
	}

	public CentrifugeRecipeBuilder setInput(Ingredient input) {
		this.input = input;
		return this;
	}

	public CentrifugeRecipeBuilder product(float chance, ItemStack stack) {
		outputs.add(new Product(stack.getItem(), stack.getCount(), stack.getTag(), chance));
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		Preconditions.checkState(!this.outputs.isEmpty(), "Empty centrifuge recipes are not allowed");
		consumer.accept(new Result(id, processingTime, input, outputs));
	}

	public static class Result implements FinishedRecipe {
		private final ResourceLocation id;
		private final int processingTime;
		private final Ingredient input;
		private final ArrayList<Product> outputs;

		public Result(ResourceLocation id, int processingTime, Ingredient input, ArrayList<Product> outputs) {
			this.id = id;
			this.processingTime = processingTime;
			this.input = input;
			this.outputs = outputs;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("time", processingTime);
			json.add("input", input.toJson());

			JsonArray products = new JsonArray();

			for (Product product : outputs) {
				products.add(JsonUtil.serialize(Product.CODEC, product));
			}

			json.add("products", products);
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return FactoryRecipeTypes.CENTRIFUGE.serializer();
		}

		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
