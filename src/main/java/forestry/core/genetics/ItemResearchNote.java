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
package forestry.core.genetics;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IMutationManager;
import forestry.api.genetics.ISpeciesType;
import forestry.api.genetics.ISpecies;
import forestry.core.genetics.mutations.EnumMutateChance;
import forestry.core.items.ItemForestry;

import forestry.api.genetics.IMutation;

public class ItemResearchNote extends ItemForestry {
	private static final String PARENT_0_KEY = "P0";
	private static final String PARENT_1_KEY = "P1";
	private static final String RESULT_KEY = "RS";
	private static final String TYPE_KEY = "ROT";

	private static final String RESEARCHER_KEY = "RES";
	private static final String NBT_TYPE = "TYP";
	private static final String NBT_INNER = "INN";

	public enum EnumNoteType {
		NONE, MUTATION, SPECIES;

		public static final EnumNoteType[] VALUES = values();

		@Nullable
		private static IMutation<?> getEncodedMutation(ISpeciesType<?, ?> type, CompoundTag compound) {
			ISpecies<?> parent0 = AlleleUtil.getSpecies(type, compound, PARENT_0_KEY);
			ISpecies<?> parent1 = AlleleUtil.getSpecies(type, compound, PARENT_1_KEY);
			if (parent0 == null || parent1 == null) {
				return null;
			}

			ISpecies<?> result = null;
			if (compound.contains(RESULT_KEY)) {
				result = AlleleUtil.getSpecies(type, compound, RESULT_KEY);
			}

			IMutation<?> encoded = null;
			IMutationManager mutations = type.getMutations();
			for (IMutation<?> mutation : (List<IMutation<?>>) mutations.getMutationsFrom(parent0)) {
				if (mutation.isPartner(parent1)) {
					if (result == null || mutation.getResult().id().equals(result.id())) {
						encoded = mutation;
						break;
					}
				}
			}

			return encoded;
		}

		public ArrayList<Component> getTooltip(CompoundTag compound) {
			ArrayList<Component> tooltips = new ArrayList<>();

			if (this == NONE) {
				return tooltips;
			}

			if (this == MUTATION) {
				ISpeciesType<IIndividual> root = GeneticsAPI.apiInstance.getRoot(compound.getString("ROT"));
				if (root == null) {
					return tooltips;
				}

				IMutation<?> encoded = getEncodedMutation(root, compound);
				if (encoded == null) {
					return tooltips;
				}

				Component species1 = Component.literal("'").append(encoded.getFirstParent().getDisplayName()).append("'").withStyle(ChatFormatting.YELLOW);
				Component species2 = Component.literal("'").append(encoded.getSecondParent().getDisplayName()).append("'").withStyle(ChatFormatting.YELLOW);
				String mutationChanceKey = EnumMutateChance.rateChance(encoded.getBaseChance()).toString().toLowerCase(Locale.ENGLISH);
				Component mutationChance = Component.translatable("for.researchNote.chance." + mutationChanceKey).withStyle(ChatFormatting.BLUE);
				Component speciesResult = encoded.getResult().getDisplayName().copy().withStyle(ChatFormatting.LIGHT_PURPLE);

				tooltips.add(Component.translatable("for.researchNote.discovery.0"));
				tooltips.add(Component.translatable("for.researchNote.discovery.1", species1, species2).withStyle(ChatFormatting.GRAY));
				tooltips.add(Component.translatable("for.researchNote.discovery.2", mutationChance).withStyle(ChatFormatting.GRAY));
				tooltips.add(Component.translatable("for.researchNote.discovery.3", speciesResult).withStyle(ChatFormatting.GRAY));

				if (!encoded.getSpecialConditions().isEmpty()) {
					for (Component line : encoded.getSpecialConditions()) {
						tooltips.add(((MutableComponent) line).withStyle(ChatFormatting.GOLD));
					}
				}
			} else if (this == SPECIES) {
				IAlleleForestrySpecies alleleFirst = AlleleUtils.getAllele(compound.getString(PARENT_0_KEY));

				if (alleleFirst != null) {
					SpeciesType<?> root = GeneticsAPI.apiInstance.getRoot(compound.getString(TYPE_KEY));

					if (root != null) {
						tooltips.add(Component.translatable("researchNote.discovered.0"));
						tooltips.add(Component.translatable("for.researchNote.discovered.1", alleleFirst.getDisplayName(), alleleFirst.getBinomial()));
					}
				}
			}

			return tooltips;
		}

		public boolean registerResults(Level level, Player player, CompoundTag compound) {
			if (this == NONE) {
				return false;
			}

			if (this == MUTATION) {
				ISpeciesType<?> root = GeneticsAPI.apiInstance.getRoot(compound.getString("ROT"));
				if (root == null) {
					return false;
				}

				IMutation encoded = getEncodedMutation(root, compound);
				if (encoded == null) {
					return false;
				}

				IBreedingTracker tracker = ((ISpeciesType) encoded.getType()).getBreedingTracker(level, player.getGameProfile());
				if (tracker.isResearched(encoded)) {
					player.sendSystemMessage(Component.translatable("for.chat.cannotmemorizeagain"));
					return false;
				}

				ISpecies<?> speciesFirst = encoded.getFirstParent();
				ISpecies<?> speciesSecond = encoded.getSecondParent();
				ISpecies<?> speciesResult = encoded.getResult();

				tracker.registerSpecies(speciesFirst);
				tracker.registerSpecies(speciesSecond);
				tracker.registerSpecies(speciesResult);

				tracker.researchMutation(encoded);
				player.sendSystemMessage(Component.translatable("for.chat.memorizednote"));

				player.sendSystemMessage(Component.translatable("for.chat.memorizednote2",
						((MutableComponent) speciesFirst.getDisplayName()).withStyle(ChatFormatting.GRAY),
						((MutableComponent) speciesSecond.getDisplayName()).withStyle(ChatFormatting.GRAY),
						((MutableComponent) speciesResult.getDisplayName()).withStyle(ChatFormatting.GREEN)));

				return true;
			}

			return false;

		}

		public static ResearchNote createMutationNote(GameProfile researcher, IMutation mutation) {
			CompoundTag compound = new CompoundTag();
			compound.putString(TYPE_KEY, mutation.getType().id());
			compound.putString(PARENT_0_KEY, mutation.getFirstParent().getId().toString());
			compound.putString(PARENT_1_KEY, mutation.getSecondParent().getId().toString());
			compound.putString(RESULT_KEY, mutation.getResult().getId().toString());
			return new ResearchNote(researcher, MUTATION, compound);
		}

		public static ItemStack createMutationNoteStack(Item item, GameProfile researcher, IMutation mutation) {
			ResearchNote note = createMutationNote(researcher, mutation);
			CompoundTag compound = new CompoundTag();
			note.writeToNBT(compound);
			ItemStack created = new ItemStack(item);
			created.setTag(compound);
			return created;
		}

		public static ResearchNote createSpeciesNote(GameProfile researcher, IAlleleForestrySpecies species) {
			CompoundTag compound = new CompoundTag();
			compound.putString(TYPE_KEY, species.getSpecies().getId());
			compound.putString(PARENT_0_KEY, species.getId().toString());
			return new ResearchNote(researcher, SPECIES, compound);
		}

		public static ItemStack createSpeciesNoteStack(Item item, GameProfile researcher, IAlleleForestrySpecies species) {
			ResearchNote note = createSpeciesNote(researcher, species);
			CompoundTag compound = new CompoundTag();
			note.writeToNBT(compound);
			ItemStack created = new ItemStack(item);
			created.setTag(compound);
			return created;
		}

	}

	public static class ResearchNote {
		@Nullable
		private final GameProfile researcher;
		private final EnumNoteType type;
		private final CompoundTag inner;

		public ResearchNote(GameProfile researcher, EnumNoteType type, CompoundTag inner) {
			this.researcher = researcher;
			this.type = type;
			this.inner = inner;
		}

		public ResearchNote(@Nullable CompoundTag compound) {
			if (compound != null) {
				if (compound.contains(RESEARCHER_KEY)) {
					this.researcher = NbtUtils.readGameProfile(compound.getCompound(RESEARCHER_KEY));
				} else {
					this.researcher = null;
				}
				this.type = EnumNoteType.VALUES[compound.getByte(NBT_TYPE)];
				this.inner = compound.getCompound(NBT_INNER);
			} else {
				this.type = EnumNoteType.NONE;
				this.researcher = null;
				this.inner = new CompoundTag();
			}
		}

		public CompoundTag writeToNBT(CompoundTag compound) {
			if (this.researcher != null) {
				CompoundTag nbt = new CompoundTag();
				NbtUtils.writeGameProfile(nbt, researcher);
				compound.put(RESEARCHER_KEY, nbt);
			}
			compound.putByte(NBT_TYPE, (byte) type.ordinal());
			compound.put(NBT_INNER, inner);
			return compound;
		}

		public void addTooltip(List<Component> list) {
			ArrayList<Component> tooltips = type.getTooltip(inner);
			if (tooltips.isEmpty()) {
				list.add(Component.translatable("for.researchNote.error.0").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
				list.add(Component.translatable("for.researchNote.error.1"));
				return;
			}

			list.addAll(tooltips);
		}

		public boolean registerResults(Level world, Player player) {
			return type.registerResults(world, player, inner);
		}
	}

	public ItemResearchNote() {
		super((new Item.Properties()).tab(null));
	}

	@Override
	public Component getName(ItemStack itemstack) {
		ResearchNote note = new ResearchNote(itemstack.getTag());
		String researcherName;
		if (note.researcher == null) {
			researcherName = "Sengir";
		} else {
			researcherName = note.researcher.getName();
		}
		return Component.translatable(getDescriptionId(itemstack), researcherName);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		ResearchNote note = new ResearchNote(itemstack.getTag());
		note.addTooltip(list);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack heldItem = playerIn.getItemInHand(handIn);
		if (worldIn.isClientSide) {
			return InteractionResultHolder.pass(heldItem);
		}

		ResearchNote note = new ResearchNote(heldItem.getTag());
		if (note.registerResults(worldIn, playerIn)) {
			playerIn.getInventory().removeItem(playerIn.getInventory().selected, 1);
		}

		return InteractionResultHolder.success(heldItem);
	}
}
