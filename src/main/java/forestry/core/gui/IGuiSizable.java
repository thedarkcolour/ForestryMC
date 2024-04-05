package forestry.core.gui;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IGuiSizable {

	int getGuiLeft();

	int getGuiTop();

	int getSizeX();

	int getSizeY();

	Minecraft getGameInstance();

	default Player getPlayer() {
		return Preconditions.checkNotNull(getGameInstance().player);
	}

	void renderTooltip(PoseStack transform, List<Component> text, Optional<TooltipComponent> idk, int mouseX, int mouseY);
}
