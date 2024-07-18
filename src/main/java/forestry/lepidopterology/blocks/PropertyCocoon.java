/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.lepidopterology.blocks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import forestry.api.genetics.alleles.ButterflyChromosomes;
import forestry.api.lepidopterology.genetics.IButterflyCocoon;
import forestry.core.blocks.properties.PropertyAllele;

import genetics.utils.AlleleUtils;

public class PropertyCocoon extends PropertyAllele<IButterflyCocoon> {
	private static final Map<String, IButterflyCocoon> namesMap = new HashMap<>();

	public PropertyCocoon(String name) {
		super(name, IButterflyCocoon.class);
	}

	@Override
	public Class<IButterflyCocoon> getValueClass() {
		return IButterflyCocoon.class;
	}

	@Override
	public Collection<IButterflyCocoon> getPossibleValues() {
		return AlleleUtils.filteredAlleles(ButterflyChromosomes.COCOON);
	}

	@Override
	public String getName(IButterflyCocoon value) {
		return value.getCocoonName();
	}

	@Override
	public Optional<IButterflyCocoon> getValue(String value) {
		if (namesMap.isEmpty()) {
			// Using the stream here so we can save one 'collect' call in 'getRegisteredAlleles()'
			AlleleUtils.filteredStream(ButterflyChromosomes.COCOON).forEach(cocoon -> {
				String propertyName = getName(cocoon);
				namesMap.put(propertyName, cocoon);
			});
		}
		return Optional.ofNullable(namesMap.get(value));
	}
}
