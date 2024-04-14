package forestry.energy.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import com.mojang.blaze3d.vertex.PoseStack;

import forestry.core.config.Constants;
import forestry.energy.menu.PeatEngineMenu;
import forestry.energy.tiles.PeatEngineBlockEntity;

public class PeatEngineScreen extends EngineScreen<PeatEngineMenu, PeatEngineBlockEntity> {
	public PeatEngineScreen(PeatEngineMenu menu, Inventory inv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/peatengine.png", menu, inv, title, menu.getTile());
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseX, int mouseY) {
		super.renderBg(transform, partialTicks, mouseX, mouseY);

		if (engine.isBurning()) {
			int progress = engine.getBurnTimeRemainingScaled(12);
			blit(transform, leftPos + 45, topPos + 27 + 12 - progress, 176, 12 - progress, 14, progress + 2);
		}
	}
}
