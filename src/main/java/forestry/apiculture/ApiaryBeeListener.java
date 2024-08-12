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
package forestry.apiculture;

import forestry.api.apiculture.IBeeListener;
import forestry.apiculture.inventory.IApiaryInventory;

public class ApiaryBeeListener implements IBeeListener {
	private final IApiary apiary;

	public ApiaryBeeListener(IApiary apiary) {
		this.apiary = apiary;
	}

	@Override
	public void wearOutEquipment(int amount) {
		IApiaryInventory apiaryInventory = this.apiary.getApiaryInventory();
		apiaryInventory.wearOutFrames(this.apiary, amount);
	}
}
