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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import forestry.api.core.INbtWritable;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IChromosome;
import forestry.api.genetics.alleles.IValueChromosome;
import forestry.core.network.IStreamable;
import forestry.core.utils.NetworkUtil;

import genetics.api.individual.IIndividual;
import forestry.api.genetics.alleles.IKaryotype;
import genetics.utils.RootUtils;

public class EscritoireGameBoard implements INbtWritable, IStreamable {
	private static final Random rand = new Random();
	private static final int TOKEN_COUNT_MAX = 22;
	private static final int TOKEN_COUNT_MIN = 6;

	private final List<EscritoireGameToken> gameTokens = new ArrayList<>(TOKEN_COUNT_MAX);
	private int tokenCount;

	public EscritoireGameBoard() {

	}

	public EscritoireGameBoard(CompoundTag nbt) {
		tokenCount = nbt.getInt("TokenCount");

		if (tokenCount > 0) {
			EscritoireGameToken[] tokens = new EscritoireGameToken[tokenCount];
			ListTag nbttaglist = nbt.getList("GameTokens", Tag.TAG_LIST);

			for (int j = 0; j < nbttaglist.size(); ++j) {
				CompoundTag CompoundNBT2 = nbttaglist.getCompound(j);
				int index = CompoundNBT2.getByte("Slot");
				tokens[index] = new EscritoireGameToken(CompoundNBT2);
			}

			Collections.addAll(gameTokens, tokens);
		}
	}

	public boolean initialize(ItemStack specimen) {
		IIndividual individual = RootUtils.getIndividual(specimen);
		if (individual == null) {
			return false;
		}

		IGenome genome = individual.getGenome();
		IKaryotype karyotype = genome.getKaryotype();
		ISpeciesType<?> species = genome.getPrimarySpecies();

		tokenCount = getTokenCount(genome);

		for (int i = 0; i < tokenCount / 2; i++) {
			IAllele[] randomTemplate = species.getTemplates().getRandomTemplate(rand);
			IChromosome iChromosomeType = karyotype.getSpeciesChromosome();
			String speciesUid = randomTemplate[iChromosomeType.ordinal()].id().toString();
			gameTokens.add(new EscritoireGameToken(speciesUid));
			gameTokens.add(new EscritoireGameToken(speciesUid));
		}
		Collections.shuffle(gameTokens);
		return true;
	}

	@Nullable
	public EscritoireGameToken getToken(int index) {
		// todo figure out why tokenCount is out of sync with gameTokens
		if (index >= tokenCount || index >= gameTokens.size()) {
			return null;
		}
		return gameTokens.get(index);
	}

	public int getTokenCount() {
		return tokenCount;
	}

	public void hideProbedTokens() {
		for (EscritoireGameToken token : gameTokens) {
			if (token.isProbed()) {
				token.setProbed(false);
			}
		}
	}

	private List<EscritoireGameToken> getUnrevealedTokens() {
		List<EscritoireGameToken> unrevealed = new ArrayList<>();
		for (EscritoireGameToken token : gameTokens) {
			if (!token.isVisible()) {
				unrevealed.add(token);
			}
		}

		return unrevealed;
	}

	@Nullable
	private EscritoireGameToken getSelected() {
		for (EscritoireGameToken token : gameTokens) {
			if (token.isSelected()) {
				return token;
			}
		}

		return null;
	}

	private boolean isBoardCleared() {
		for (EscritoireGameToken token : gameTokens) {
			if (!token.isMatched()) {
				return false;
			}
		}

		return true;
	}

	public void probe() {
		List<EscritoireGameToken> tokens = getUnrevealedTokens();
		int index = rand.nextInt(tokens.size());

		EscritoireGameToken token = tokens.get(index);
		token.setProbed(true);
	}

	public EscritoireGame.Status choose(EscritoireGameToken token) {
		EscritoireGame.Status status = EscritoireGame.Status.PLAYING;
		if (token.isMatched() || token.isSelected()) {
			return status;
		}

		EscritoireGameToken selected = getSelected();
		if (selected == null) {
			token.setSelected();
			hideProbedTokens();
		} else if (token.matches(selected)) {
			selected.setMatched();
			token.setMatched();
			if (isBoardCleared()) {
				status = EscritoireGame.Status.SUCCESS;
			}
			hideProbedTokens();
		} else {
			token.setFailed();
			selected.setFailed();
			status = EscritoireGame.Status.FAILURE;
		}

		return status;
	}

	public void reset() {
		gameTokens.clear();
		tokenCount = 0;
	}

	private static int getTokenCount(IGenome genome) {
		IValueChromosome<? extends ISpeciesType<?>> speciesChromosome = genome.getKaryotype().getSpeciesChromosome();
		ISpeciesType<?> species1 = genome.getPrimarySpecies(speciesChromosome);
		ISpeciesType<?> species2 = genome.getSecondarySpecies(speciesChromosome);

		int tokenCount = species1.getComplexity() + species2.getComplexity();

		if (tokenCount % 2 != 0) {
			tokenCount = Math.round((float) tokenCount / 2) * 2;
		}

		if (tokenCount > TOKEN_COUNT_MAX) {
			tokenCount = TOKEN_COUNT_MAX;
		} else if (tokenCount < TOKEN_COUNT_MIN) {
			tokenCount = TOKEN_COUNT_MIN;
		}

		return tokenCount;
	}

	@Override
	public CompoundTag write(CompoundTag compoundNBT) {
		if (tokenCount > 0) {
			compoundNBT.putInt("TokenCount", tokenCount);
			ListTag nbttaglist = new ListTag();

			for (int i = 0; i < tokenCount; i++) {
				EscritoireGameToken token = gameTokens.get(i);
				if (token == null) {
					continue;
				}

				CompoundTag compoundNBT2 = new CompoundTag();
				compoundNBT2.putByte("Slot", (byte) i);
				token.write(compoundNBT2);
				nbttaglist.add(compoundNBT2);
			}

			compoundNBT.put("GameTokens", nbttaglist);
		} else {
			compoundNBT.putInt("TokenCount", 0);
		}
		return compoundNBT;
	}

	/* IStreamable */
	@Override
	public void writeData(FriendlyByteBuf data) {
		data.writeVarInt(tokenCount);
		NetworkUtil.writeStreamables(data, gameTokens);
	}

	@Override
	public void readData(FriendlyByteBuf data) {
		tokenCount = data.readVarInt();
		NetworkUtil.readStreamables(data, gameTokens, EscritoireGameToken::new);
	}
}
