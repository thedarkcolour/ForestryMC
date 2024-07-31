package forestry.core.genetics.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.util.INBTSerializable;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;
import forestry.core.utils.SpeciesUtil;

public class SerializableIndividualHandlerItem extends IndividualHandlerItem implements INBTSerializable<CompoundTag> {
	public static final String NBT_INDIVIDUAL = "IND";

	public SerializableIndividualHandlerItem(ISpeciesType<?, ?> type, ItemStack container, IIndividual individual, ILifeStage stage) {
		super(type, container, individual, stage);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		Tag individualNbt = SpeciesUtil.serializeIndividual(this.individual);
		if (individualNbt != null) {
			nbt.put(NBT_INDIVIDUAL, individualNbt);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		Tag individualNbt = nbt.get(NBT_INDIVIDUAL);

		if (individualNbt != null) {
			this.individual = SpeciesUtil.deserializeIndividual(this.speciesType, individualNbt);
		}
	}
}
