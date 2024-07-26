package forestry.core.genetics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.util.INBTSerializable;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;

public class SerializableIndividualHandler extends IndividualHandler implements INBTSerializable<CompoundTag> {
	public SerializableIndividualHandler(ItemStack container, IIndividual individual, ILifeStage stage) {
		super(container, individual, stage);
	}

	@Override
	public CompoundTag serializeNBT() {
		return CodecUtil;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {

	}
}
