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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import forestry.api.core.INbtWritable;
import forestry.api.genetics.IForestrySpeciesType;
import forestry.api.genetics.alleles.IAlleleForestrySpecies;
import forestry.core.network.IStreamable;
import forestry.core.utils.ColourUtil;
import forestry.core.utils.NetworkUtil;

import forestry.api.genetics.alleles.IAllele;
import genetics.api.individual.IIndividual;
import forestry.api.genetics.ISpeciesType;
import genetics.utils.AlleleUtils;

public class EscritoireGameToken implements INbtWritable, IStreamable {

	private enum State {
		UNREVEALED,// face down
		PROBED,    // shown by escritoire probe action
		SELECTED,  // selected by the user as the first half of a match
		MATCHED,   // successfully matched
		FAILED;    // failed to match
		public static final State[] VALUES = values();
	}

	private static final String[] OVERLAY_NONE = new String[0];
	private static final String[] OVERLAY_FAILED = new String[]{"errors/errored"};
	private static final String[] OVERLAY_SELECTED = new String[]{"errors/unknown"};

	@Nullable
	private IIndividual tokenIndividual;
	private ItemStack tokenStack = ItemStack.EMPTY;

	private State state = State.UNREVEALED;

	public EscritoireGameToken(FriendlyByteBuf data) {
		readData(data);
	}

	public EscritoireGameToken(String speciesUid) {
		setTokenSpecies(speciesUid);
	}

	public EscritoireGameToken(CompoundTag CompoundNBT) {
		if (CompoundNBT.contains("state")) {
			int stateOrdinal = CompoundNBT.getInt("state");
			state = State.values()[stateOrdinal];
		}

		if (CompoundNBT.contains("tokenSpecies")) {
			String speciesUid = CompoundNBT.getString("tokenSpecies");
			setTokenSpecies(speciesUid);
		}
	}

	private void setTokenSpecies(String speciesUid) {
		IAllele allele = AlleleUtils.getAllele(speciesUid);

		if (allele instanceof IAlleleForestrySpecies species) {
			ISpeciesType<IIndividual> root = (ISpeciesType<IIndividual>) species.getSpecies();
			IAllele[] template = root.getTemplates().getTemplate(species.getId().toString());
			this.tokenIndividual = root.templateAsIndividual(template);
			this.tokenStack = root.getTypes().createStack(this.tokenIndividual, ((IForestrySpeciesType<IIndividual>) root).getIconType());
		}
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

		int iconColor = tokenIndividual.getGenome().getPrimarySpecies(IAlleleForestrySpecies.class).getSpriteColour(0);

		if (state == State.MATCHED) {
			return ColourUtil.multiplyRGBComponents(iconColor, 0.7f);
		} else {
			return iconColor;
		}
	}


	public Component getTooltip() {
		return !tokenStack.isEmpty() ? tokenStack.getHoverName() : Component.translatable("for.gui.unknown");
	}

	public String[] getOverlayIcons() {
		return switch (state) {
			case FAILED -> OVERLAY_FAILED;
			case SELECTED -> OVERLAY_SELECTED;
			default -> OVERLAY_NONE;
		};
	}

	public boolean matches(EscritoireGameToken other) {
		return ItemStack.matches(tokenStack, other.getTokenStack());
	}

	@Override
	public CompoundTag write(CompoundTag CompoundNBT) {
		CompoundNBT.putInt("state", state.ordinal());

		if (tokenIndividual != null) {
			CompoundNBT.putString("tokenSpecies", tokenIndividual.getGenome().getPrimarySpecies().id().toString());
		}
		return CompoundNBT;
	}

	/* IStreamable */
	@Override
	public void writeData(FriendlyByteBuf data) {
		NetworkUtil.writeEnum(data, state);
		if (tokenIndividual != null) {
			data.writeBoolean(true);
			data.writeUtf(tokenIndividual.getGenome().getPrimarySpecies().id().toString());
		} else {
			data.writeBoolean(false);
		}
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		state = NetworkUtil.readEnum(data, State.VALUES);
		if (data.readBoolean()) {
			String speciesUid = data.readUtf();
			setTokenSpecies(speciesUid);
		}
	}
}
