package forestry.api.genetics;

import net.minecraft.resources.ResourceLocation;

public final class SpeciesDefinition {
	private final ResourceLocation id;

	public SpeciesDefinition(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation getId() {
		return this.id;
	}
}
