package forestry.core.config;

import java.text.NumberFormat;

import net.minecraft.client.Minecraft;

public enum EnergyDisplayMode {
	RF;

	EnergyDisplayMode() {
	}

	public String formatEnergyValue(int energy) {
		NumberFormat format = NumberFormat.getIntegerInstance(Minecraft.getInstance().getLocale());
		return format.format(energy) + " RF";
	}

	public String formatRate(int rate) {
		return formatEnergyValue(rate) + "/t";
	}
}
