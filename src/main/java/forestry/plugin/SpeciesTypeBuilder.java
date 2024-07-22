package forestry.plugin;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;

import forestry.api.plugin.IKaryotypeBuilder;
import forestry.api.plugin.ISpeciesTypeBuilder;

public class SpeciesTypeBuilder implements ISpeciesTypeBuilder {
	private final ResourceLocation id;

	public SpeciesTypeBuilder(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ISpeciesTypeBuilder setKaryotype(Consumer<IKaryotypeBuilder> action) {
		return null;
	}
}
