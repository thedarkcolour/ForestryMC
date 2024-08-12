package forestry.core.utils;

public final class TickHelper {
	private int tickCount;

	// offset is used to avoid multiple tick helpers all ticking at the same times
	public TickHelper(int offset) {
		this.tickCount = offset % 2048;
	}

	public void onTick() {
		tickCount++;
	}

	public boolean updateOnInterval(int tickInterval) {
		return tickCount % tickInterval == 0;
	}
}
