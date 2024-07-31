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
package forestry.core.tiles;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.IForestryApi;
import forestry.api.client.ForestrySprites;
import forestry.api.core.INbtWritable;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpecies;
import forestry.api.genetics.ISpeciesType;
import forestry.core.network.IStreamable;
import forestry.core.utils.ColourUtil;
import forestry.core.utils.NetworkUtil;

public class EscritoireGameToken implements INbtWritable, IStreamable {
	private enum State {
		UNREVEALED,// face down
		PROBED,    // shown by escritoire probe action
		SELECTED,  // selected by the user as the first half of a match
		MATCHED,   // successfully matched
		FAILED;    // failed to match
		public static final State[] VALUES = values();
	}

	@Nullable
	private ISpeciesType<? extends ISpecies<?>, ?> tokenType;
	@Nullable
	private IIndividual tokenIndividual;
	private ItemStack tokenStack = ItemStack.EMPTY;

	private State state = State.UNREVEALED;

	public EscritoireGameToken(FriendlyByteBuf data) {
		readData(data);
	}

	public EscritoireGameToken(ISpecies<?> species) {
		setTokenSpecies(species);
	}

	public EscritoireGameToken(CompoundTag nbt) {
		read(nbt);
	}

	private void setTokenSpecies(ResourceLocation typeId, ResourceLocation speciesId) {
		if (this.tokenType != null && typeId == this.tokenType.id()) {
			setTokenSpecies(this.tokenType.getSpecies(speciesId));
		} else {
			ISpeciesType<?, ?> type = IForestryApi.INSTANCE.getGeneticManager().getSpeciesType(typeId);
			setTokenSpecies(type.getSpecies(speciesId));
		}
	}

	private void setTokenSpecies(ISpecies<?> species) {
		this.tokenIndividual = species.createIndividual();
		this.tokenType = species.getType();
		this.tokenStack = species.createStack(species.getType().getDefaultStage());
	}

	public ItemStack getTokenStack() {
		return tokenStack;
	}

	public boolean isVisible() {
		return state != State.UNREVEALED;
	}

	public boolean isProbed() {
		return state == State.PROBED;
	}

	public boolean isMatched() {
		return state == State.MATCHED;
	}

	public boolean isSelected() {
		return state == State.SELECTED;
	}

	public void setFailed() {
		state = State.FAILED;
	}

	public void setProbed(boolean probed) {
		if (probed) {
			state = State.PROBED;
		} else {
			state = State.UNREVEALED;
		}
	}

	public void setSelected() {
		state = State.SELECTED;
	}

	public void setMatched() {
		state = State.MATCHED;
	}

	public int getTokenColour() {
		if (tokenIndividual == null || !isVisible()) {
			return 0xffffff;
		}

		int iconColor = tokenIndividual.getGenome().getActiveSpecies().getEscritoireColor();

		if (state == State.MATCHED) {
			return ColourUtil.multiplyRGBComponents(iconColor, 0.7f);
		} else {
			return iconColor;
		}
	}


	public Component getTooltip() {
		return !tokenStack.isEmpty() ? tokenStack.getHoverName() : Component.translatable("for.gui.unknown");
	}

	@Nullable
	public ResourceLocation getOverlayToken() {
		return switch (state) {
			case FAILED -> ForestrySprites.ERROR_ERRORED;
			case SELECTED -> ForestrySprites.ERROR_UNKNOWN;
			default -> null;
		};
	}

	public boolean matches(EscritoireGameToken other) {
		return ItemStack.matches(tokenStack, other.getTokenStack());
	}

	@Override
	public CompoundTag write(CompoundTag nbt) {
		nbt.putInt("state", this.state.ordinal());

		if (this.tokenIndividual != null) {
			nbt.putString("tokenSpecies", this.tokenIndividual.getSpecies().id().toString());
		}
		return nbt;
	}

	private void read(CompoundTag nbt) {
		if (nbt.contains("state")) {
			int stateOrdinal = nbt.getInt("state");
			this.state = State.VALUES[stateOrdinal];
		}

		String tokenSpecies = nbt.getString("tokenSpecies");
		String tokenType = nbt.getString("tokenSpeciesType");

		if (!tokenSpecies.isEmpty() && !tokenType.isEmpty()) {
			setTokenSpecies(new ResourceLocation(tokenType), new ResourceLocation(tokenSpecies));
		}
	}

	@Override
	public void writeData(FriendlyByteBuf data) {
		NetworkUtil.writeEnum(data, state);
		if (tokenIndividual != null && this.tokenType != null) {
			data.writeBoolean(true);
			data.writeResourceLocation(tokenIndividual.getSpecies().id());
			data.writeResourceLocation(tokenType.id());
		} else {
			data.writeBoolean(false);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		state = NetworkUtil.readEnum(data, State.VALUES);
		if (data.readBoolean()) {
			ResourceLocation speciesId = data.readResourceLocation();
			ResourceLocation typeId = data.readResourceLocation();
			setTokenSpecies(typeId, speciesId);
		}
	}
}
