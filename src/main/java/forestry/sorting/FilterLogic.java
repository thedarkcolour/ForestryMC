package forestry.sorting;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import forestry.api.IForestryApi;
import forestry.api.core.ILocatable;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IIndividualHandler;
import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleManager;
import forestry.api.genetics.filter.FilterData;
import forestry.api.genetics.filter.IFilterLogic;
import forestry.api.genetics.filter.IFilterRuleType;
import forestry.core.utils.NetworkUtil;
import forestry.sorting.network.packets.PacketFilterChangeGenome;
import forestry.sorting.network.packets.PacketFilterChangeRule;
import forestry.sorting.network.packets.PacketGuiFilterUpdate;

public class FilterLogic implements IFilterLogic {
	private final ILocatable locatable;
	private final INetworkHandler networkHandler;
	private IFilterRuleType[] filterRules = new IFilterRuleType[6];
	private AlleleFilter[][] genomeFilter = new AlleleFilter[6][3];

	public FilterLogic(ILocatable locatable, INetworkHandler networkHandler) {
		this.locatable = locatable;
		this.networkHandler = networkHandler;

		for (int i = 0; i < this.filterRules.length; i++) {
			this.filterRules[i] = FilterRegistry.INSTANCE.getDefaultRule();
		}
	}

	@Override
	public INetworkHandler getNetworkHandler() {
		return this.networkHandler;
	}

	@Override
	public CompoundTag write(CompoundTag data) {
		for (int i = 0; i < this.filterRules.length; i++) {
			data.putString("TypeFilter" + i, this.filterRules[i].getId());
		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				AlleleFilter filter = this.genomeFilter[i][j];
				if (filter == null) {
					continue;
				}
				if (filter.activeAllele != null) {
					data.putString("GenomeFilterS" + i + "-" + j + "-" + 0, filter.activeAllele.alleleId().toString());
				}
				if (filter.inactiveAllele != null) {
					data.putString("GenomeFilterS" + i + "-" + j + "-" + 1, filter.inactiveAllele.alleleId().toString());
				}
			}
		}
		return data;
	}

	@Override
	public void read(CompoundTag data) {
		for (int i = 0; i < this.filterRules.length; i++) {
			this.filterRules[i] = FilterRegistry.INSTANCE.getRuleOrDefault(data.getString("TypeFilter" + i));
		}

		IAlleleManager alleles = IForestryApi.INSTANCE.getAlleleManager();

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				AlleleFilter filter = new AlleleFilter();
				if (data.contains("GenomeFilterS" + i + "-" + j + "-" + 0)) {
					filter.activeAllele = alleles.getAllele(new ResourceLocation(data.getString("GenomeFilterS" + i + "-" + j + "-" + 0)));
				}
				if (data.contains("GenomeFilterS" + i + "-" + j + "-" + 1)) {
					filter.inactiveAllele = alleles.getAllele(new ResourceLocation(data.getString("GenomeFilterS" + i + "-" + j + "-" + 1)));
				}
				this.genomeFilter[i][j] = filter;
			}
		}
	}

	@Override
	public void writeGuiData(FriendlyByteBuf buffer) {
		writeFilterRules(buffer, filterRules);
		writeGenomeFilters(buffer, genomeFilter);
	}

	@Override
	public void readGuiData(FriendlyByteBuf buffer) {
		filterRules = readFilterRules(buffer);
		genomeFilter = readGenomeFilters(buffer);
	}

	public static void writeFilterRules(FriendlyByteBuf buffer, IFilterRuleType[] filterRules) {
		for (IFilterRuleType filterRule : filterRules) {
			buffer.writeShort(FilterRegistry.INSTANCE.getId(filterRule));
		}
	}

	public static void writeGenomeFilters(FriendlyByteBuf buffer, AlleleFilter[][] genomeFilter) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				AlleleFilter filter = genomeFilter[i][j];
				if (filter == null) {
					buffer.writeBoolean(false);
					buffer.writeBoolean(false);
					continue;
				}
				if (filter.activeAllele != null) {
					buffer.writeBoolean(true);
					buffer.writeUtf(filter.activeAllele.alleleId().toString());
				} else {
					buffer.writeBoolean(false);
				}
				if (filter.inactiveAllele != null) {
					buffer.writeBoolean(true);
					buffer.writeUtf(filter.inactiveAllele.alleleId().toString());
				} else {
					buffer.writeBoolean(false);
				}
			}
		}
	}

	public static IFilterRuleType[] readFilterRules(FriendlyByteBuf buffer) {
		IFilterRuleType[] filterRules = new IFilterRuleType[6];
		for (int i = 0; i < 6; i++) {
			filterRules[i] = FilterRegistry.INSTANCE.getRule(buffer.readShort());
		}

		return filterRules;
	}

	public static AlleleFilter[][] readGenomeFilters(FriendlyByteBuf buffer) {
		AlleleFilter[][] genomeFilters = new AlleleFilter[6][32023];
		IAlleleManager alleles = IForestryApi.INSTANCE.getAlleleManager();

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				AlleleFilter filter = new AlleleFilter();
				if (buffer.readBoolean()) {
					filter.activeAllele = alleles.getAllele(new ResourceLocation(buffer.readUtf(1024)));
				}
				if (buffer.readBoolean()) {
					filter.inactiveAllele = alleles.getAllele(new ResourceLocation(buffer.readUtf(1024)));
				}
				genomeFilters[i][j] = filter;
			}
		}

		return genomeFilters;
	}

	@Override
	public boolean isValid(ItemStack stack, Direction facing) {
		return IIndividualHandler.filter(stack, (individual, stage) -> isValid(facing, stack, new FilterData(individual, stage)));
	}

	public boolean isValid(Direction facing, ItemStack stack, FilterData filterData) {
		IFilterRuleType rule = getRule(facing);
		if (rule == DefaultFilterRuleType.CLOSED) {
			return false;
		}
		if (rule == DefaultFilterRuleType.ITEM) {
			return true;
		}
		ResourceLocation requiredRoot = rule.getSpeciesTypeId();
		if (requiredRoot != null && (!filterData.type().id().equals(requiredRoot))) {
			return false;
		}
		if (rule == DefaultFilterRuleType.ANYTHING || rule.isValid(stack, filterData)) {
			IIndividual ind = filterData.individual();
			var genome = ind.getGenome().getAllelePair(ind.getGenome().getKaryotype().getSpeciesChromosome());
			var active = genome.active().value();
			var inactive = genome.active().value();
			return isValidAllelePair(facing, active.id().toString(), inactive.id().toString());
		}
		return false;
	}

	public boolean isValidAllelePair(Direction orientation, String activeUID, String inactiveUID) {
		AlleleFilter[] directionFilters = this.genomeFilter[orientation.ordinal()];

		if (directionFilters == null) {
			return true;
		}

		boolean foundFilter = false;
		for (int i = 0; i < 3; i++) {
			AlleleFilter filter = directionFilters[i];
			if (filter != null && !filter.isEmpty()) {
				foundFilter = true;
				if (!filter.isEmpty() && filter.isValid(activeUID, inactiveUID)) {
					return true;
				}
			}
		}
		return !foundFilter;
	}

	public IFilterRuleType getRule(Direction facing) {
		return filterRules[facing.ordinal()];
	}

	public boolean setRule(Direction facing, IFilterRuleType rule) {
		if (filterRules[facing.ordinal()] != rule) {
			filterRules[facing.ordinal()] = rule;
			return true;
		}
		return false;
	}

	@Nullable
	public AlleleFilter getGenomeFilter(Direction facing, int index) {
		return genomeFilter[facing.ordinal()][index];
	}

	@Nullable
	public IAllele getGenomeFilter(Direction facing, int index, boolean active) {
		AlleleFilter filter = getGenomeFilter(facing, index);
		if (filter == null) {
			return null;
		}
		return active ? filter.activeAllele : filter.inactiveAllele;
	}

	public boolean setGenomeFilter(Direction facing, int index, boolean active, @Nullable IAllele allele) {
		AlleleFilter filter = genomeFilter[facing.ordinal()][index];
		if (filter == null) {
			filter = genomeFilter[facing.ordinal()][index] = new AlleleFilter();
		}
		boolean set;
		if (active) {
			set = filter.activeAllele != allele;
			filter.activeAllele = allele;
		} else {
			set = filter.inactiveAllele != allele;
			filter.inactiveAllele = allele;
		}
		return set;
	}

	@Override
	public void sendToServer(Direction facing, int index, boolean active, @Nullable IAllele allele) {
		NetworkUtil.sendToServer(new PacketFilterChangeGenome(locatable.getCoordinates(), facing, (short) index, active, allele));
	}

	@Override
	public void sendToServer(Direction facing, IFilterRuleType rule) {
		NetworkUtil.sendToServer(new PacketFilterChangeRule(locatable.getCoordinates(), facing, rule));
	}

	public PacketGuiFilterUpdate createGuiUpdatePacket(BlockPos pos) {
		return new PacketGuiFilterUpdate(pos, this.filterRules, this.genomeFilter);
	}

	public void readGuiUpdatePacket(PacketGuiFilterUpdate msg) {
		this.filterRules = msg.filterRules();
		this.genomeFilter = msg.genomeFilter();
	}
}
