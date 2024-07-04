package genetics.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import genetics.api.IGeneTemplate;
import genetics.api.alleles.IAllele;
import genetics.api.individual.IChromosomeType;
import genetics.api.root.IIndividualRoot;

import genetics.ApiInstance;
import genetics.Genetics;
import genetics.utils.AlleleUtils;

public class GeneTemplate implements IGeneTemplate, ICapabilitySerializable<CompoundTag> {
	public static final IGeneTemplate EMPTY = new Empty();

	private static final String NBT_ALLELE = "Allele";
	private static final String NBT_TYPE = "Type";
	private static final String NBT_DEFINITION = "Definition";

	private final LazyOptional<IGeneTemplate> holder = LazyOptional.of(() -> this);

	@Nullable
	private IAllele allele;
	@Nullable
	private IChromosomeType type;
	@Nullable
	private IIndividualRoot root;

	@Nullable
	@Override
	public IAllele getAllele() {
		return allele;
	}

	@Nullable
	@Override
	public IChromosomeType getType() {
		return type;
	}

	@Nullable
	@Override
	public IIndividualRoot getRoot() {
		return root;
	}

	@Override
	public void setAllele(@Nullable IChromosomeType type, @Nullable IAllele allele) {
		this.allele = allele;
		this.type = type;
		if (type != null) {
			root = type.getRoot();
		} else {
			root = null;
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compound = new CompoundTag();
		if (allele != null) {
			compound.putString(NBT_ALLELE, allele.getRegistryName().toString());
		}
		if (type != null && root != null) {
			compound.putByte(NBT_TYPE, (byte) type.getIndex());
			compound.putString(NBT_DEFINITION, root.getUID());
		}
		return compound;
	}

	@Override
	public void deserializeNBT(CompoundTag compound) {
		if (compound.contains(NBT_TYPE) && compound.contains(NBT_DEFINITION)) {
			ApiInstance.INSTANCE.getRoot(compound.getString(NBT_DEFINITION)).ifPresent(def -> {
				this.root = def;
				type = def.getKaryotype().getChromosomeTypes()[compound.getByte(NBT_TYPE)];
			});
		}
		if (compound.contains(NBT_ALLELE)) {
			allele = AlleleUtils.getAllele(compound.getString(NBT_ALLELE));
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction facing) {
		return Genetics.GENE_TEMPLATE.orEmpty(cap, holder);
	}

	private static class Empty implements IGeneTemplate {
		@Nullable
		@Override
		public IAllele getAllele() {
			return null;
		}

		@Nullable
		@Override
		public IChromosomeType getType() {
			return null;
		}

		@Nullable
		@Override
		public IIndividualRoot getRoot() {
			return null;
		}

		@Override
		public void setAllele(@Nullable IChromosomeType type, @Nullable IAllele allele) {
		}
	}
}
