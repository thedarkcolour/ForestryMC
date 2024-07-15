package forestry.api.plugin;

import java.util.function.Consumer;

public interface ISpeciesTypeBuilder {
	/**
	 * Defines the default karyotype for members of this species type. Although the default genome can be customized
	 * on a per-species basis, all members of the same species type have the same set of chromosomes.
	 */
	ISpeciesTypeBuilder setKaryotype(Consumer<IKaryotypeBuilder> action);
}
