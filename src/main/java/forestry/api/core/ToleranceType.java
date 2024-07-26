/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.core;

/**
 * Used to determine the range of acceptable alternatives to an ideal {@link forestry.api.core.TemperatureType} or {@link forestry.api.core.HumidityType}.
 * For example, a bee whose ideal humidity is NORMAL and has a tolerance of BOTH_1 can tolerate DAMP, NORMAL, and ARID humidity,
 * or a bee whose ideal humidity is ARID and has a tolerance of UP_1 can tolerate ARID and NORMAL humidity.
 */
public enum ToleranceType {
	NONE(0, 0),
	BOTH_1(1, 1),
	BOTH_2(2, 2),
	BOTH_3(3, 3),
	BOTH_4(4, 4),
	BOTH_5(5, 5),
	UP_1(1, 0),
	UP_2(2, 0),
	UP_3(3, 0),
	UP_4(4, 0),
	UP_5(5, 0),
	DOWN_1(0, 1),
	DOWN_2(0, 2),
	DOWN_3(0, 3),
	DOWN_4(0, 4),
	DOWN_5(0, 5);

	public final int up;
	public final int down;

	ToleranceType(int up, int down) {
		this.up = up;
		this.down = down;
	}
}
