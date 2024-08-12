package forestry.core.genetics.capability;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.util.INBTSerializable;

import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ILifeStage;
import forestry.api.genetics.ISpeciesType;
import forestry.core.utils.SpeciesUtil;

public class SerializableIndividualHandlerItem extends IndividualHandlerItem implements INBTSerializable<Tag> {
	public SerializableIndividualHandlerItem(ISpeciesType<?, ?> type, ItemStack container, IIndividual individual, ILifeStage stage) {
		super(type, container, individual, stage);
	}

	@Override
	public Tag serializeNBT() {
		return SpeciesUtil.serializeIndividual(this.individual);
	}

	@Override
	public void deserializeNBT(Tag nbt) {
		this.individual = SpeciesUtil.deserializeIndividual(this.speciesType, nbt);
	}
}
