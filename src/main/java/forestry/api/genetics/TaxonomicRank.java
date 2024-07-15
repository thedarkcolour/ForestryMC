package forestry.api.genetics;

import java.util.List;

import net.minecraft.util.Mth;

/**
 * Represents taxonomic ranks, sometimes called levels of classification, for a {@link ITaxon}.
 */
public enum TaxonomicRank {
	DOMAIN(0x777fff, true),
	KINGDOM(0x77c3ff),
	PHYLUM(0x77ffb6, true),
	CLASS(0x7bff77),
	ORDER(0xbeff77),
	FAMILY(0xfffd77),
	GENUS(0xffba77);

	public static final List<TaxonomicRank> VALUES = List.of(values());

	private final int colour;
	private final boolean isDroppable;

	TaxonomicRank(int colour) {
		this(colour, false);
	}

	TaxonomicRank(int colour, boolean isDroppable) {
		this.colour = colour;
		this.isDroppable = isDroppable;
	}

	/**
	 * @return Colour to use for displaying this classification.
	 */
	public int getColour() {
		return colour;
	}

	/**
	 * @return Whether display of this classification level can be omitted in case of space constraints.
	 */
	public boolean isDroppable() {
		return isDroppable;
	}

	/**
	 * @return The rank below this one. Never goes past {@link #GENUS}.
	 */
	public TaxonomicRank next() {
		return VALUES.get(Mth.clamp(0, VALUES.size(), ordinal() + 1));
	}
}
