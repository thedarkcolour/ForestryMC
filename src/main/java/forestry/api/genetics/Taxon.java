package forestry.api.genetics;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Biological classifications from domain down to genus.
 * Used by the Forestry analyzers to display hierarchies.
 *
 * @param name     The lowercase name of the taxon.
 * @param rank     The level inside the full hierarchy this particular taxon is located at.
 * @param children The taxa directly below this taxon.
 * @param species  The member species of this group.
 * @param parent   The parent taxon, or {@code null} if this is a {@link TaxonomicRank#DOMAIN}.
 */
public record Taxon(String name, TaxonomicRank rank, List<Taxon> children, List<ISpecies<?>> species,
					@Nullable Taxon parent) {
}
