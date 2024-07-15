package forestry.core.genetics.alleles;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.function.Function;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import forestry.api.genetics.alleles.IAllele;
import forestry.api.genetics.alleles.IAlleleRegistry;
import forestry.api.genetics.alleles.IBooleanAllele;
import forestry.api.genetics.alleles.IFloatAllele;
import forestry.api.genetics.alleles.IIntegerAllele;
import forestry.api.genetics.alleles.IValueAllele;
import forestry.api.genetics.alleles.ValueAllele;

import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class AlleleRegistry implements IAlleleRegistry {
	// primitive values to primitive allele
	private final Float2ObjectOpenHashMap<IFloatAllele> dominantFloatAlleles = new Float2ObjectOpenHashMap<>();
	private final Float2ObjectOpenHashMap<IFloatAllele> floatAlleles = new Float2ObjectOpenHashMap<>();
	private final Int2ObjectOpenHashMap<IIntegerAllele> dominantIntAlleles = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectOpenHashMap<IIntegerAllele> intAlleles = new Int2ObjectOpenHashMap<>();
	private final IBooleanAllele[] booleanAlleles = new BooleanAllele[4];
	// value to allele
	private final HashMap<?, IValueAllele<?>> valueAlleles = new HashMap<>();
	// id to allele
	private final LinkedHashMap<ResourceLocation, IAllele> allelesByName = new LinkedHashMap<>();

	@Override
	public IBooleanAllele booleanAllele(boolean value, boolean dominant) {
		// 0 = FF, 1 = FT, 2 = TF, 3 = TT
		int index = (value ? (dominant ? 3 : 2) : (dominant ? 1 : 0));
		IBooleanAllele allele = booleanAlleles[index];
		if (allele == null) {
			allele = new BooleanAllele(value, dominant);
			this.booleanAlleles[index] = allele;
			this.allelesByName.put(allele.id(), allele);
		}
		return allele;
	}

	@Override
	public IIntegerAllele intAllele(int value, boolean dominant) {
		return (dominant ? this.dominantIntAlleles : this.intAlleles).computeIfAbsent(value, v -> {
			IntegerAllele allele = new IntegerAllele(v, dominant);
			this.allelesByName.put(allele.id(), allele);
			return allele;
		});
	}

	@Override
	public IFloatAllele floatAllele(float value, boolean dominant) {
		return (dominant ? this.dominantFloatAlleles : this.floatAlleles).computeIfAbsent(value, v -> new FloatAllele(v, dominant));
	}

	@Override
	public <E extends Enum<E>> IValueAllele<E> enumAllele(String modId, E value, boolean dominant) {
		return valueAllele(value, dominant, enumValue -> {
			String enumName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, enumValue.getDeclaringClass().getSimpleName());
			String valueName = enumValue.name().toLowerCase(Locale.ROOT);
			if (dominant) {
				valueName += 'd';
			}
			return new ResourceLocation(modId, enumName + '_' + valueName);
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> IValueAllele<V> valueAllele(V value, boolean dominant, Function<V, ResourceLocation> naming) {
		Preconditions.checkNotNull(value, "Allele value must not be null.");

		IValueAllele<?> identityExisting = this.valueAlleles.get(value);

		if (identityExisting != null) {
			return (IValueAllele<V>) identityExisting;
		} else {

		}
	}

	@Override
	public Codec<IAllele> byNameCodec() {
		// should we offer a compressed codec as well? it would only be used in JsonOps.COMPRESSED
		return ResourceLocation.CODEC.flatXmap(id -> {
			IAllele allele = getAllele(id);
			if (allele != null) {
				return DataResult.success(allele);
			} else {
				return DataResult.error("Unknown allele: " + id);
			}
		}, allele -> DataResult.success(allele.id()));
	}

	@Override
	@Nullable
	public IAllele getAllele(ResourceLocation id) {
		return this.allelesByName.get(id);
	}
}
