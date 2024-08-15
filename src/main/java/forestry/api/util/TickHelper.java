package forestry.api.util;

/**
 * Helper object used to execute update logic on every tick interval.
 */
public final class TickHelper {
	private int tickCount;

	/**
	 * Create a new tick helper.
	 *
	 * @param offset A random offset used to avoid other TickHelpers on the same interval all ticking at the same time.
	 */
	public TickHelper(int offset) {
		this.tickCount = offset % 2048;
	}

	/**
	 * Must be called every tick to keep track of the total number of ticks.
	 */
	public void onTick() {
		this.tickCount++;
	}

	/**
	 * Used to check if a certain number of ticks has elapsed.
	 *
	 * @param tickInterval The tick interval for which this method should return true. For example, a tickInterval
	 *                     of 60 would return true every three seconds.
	 * @return Whether enough ticks have elapsed since the last time this method returned true.
	 */
	public boolean updateOnInterval(int tickInterval) {
		return this.tickCount % tickInterval == 0;
	}
}
