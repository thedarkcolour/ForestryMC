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
package forestry.core.proxy;

import forestry.core.blocks.IMachinePropertiesTesr;
import forestry.core.blocks.MachinePropertiesTesr;
import forestry.core.tiles.TileAnalyzer;
import forestry.core.tiles.TileBase;
import forestry.core.tiles.TileEscritoire;
import forestry.core.tiles.TileMill;
import forestry.core.tiles.TileNaturalistChest;
import forestry.energy.tiles.EngineBlockEntity;

public class ProxyRender {

	public void initRendering() {
	}

	public void setRenderDefaultMachine(IMachinePropertiesTesr<? extends TileBase> machineProperties, String baseTexture) {
	}

	public void setRenderMill(IMachinePropertiesTesr<? extends TileMill> machineProperties, String baseTexture) {
	}

	public void setRenderDefaultEngine(MachinePropertiesTesr<? extends EngineBlockEntity> properties, String baseTexture) {
	}

	public void setRenderEscritoire(IMachinePropertiesTesr<? extends TileEscritoire> machineProperties) {
	}

	public void setRendererAnalyzer(IMachinePropertiesTesr<? extends TileAnalyzer> machineProperties) {
	}

	public void setRenderChest(IMachinePropertiesTesr<? extends TileNaturalistChest> machineProperties, String textureName) {
	}
}
