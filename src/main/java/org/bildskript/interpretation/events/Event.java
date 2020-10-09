package org.bildskript.interpretation.events;

import org.bildskript.interpretation.parts.Chip;
import org.bildskript.interpretation.parts.Lane;

/**
 *
 */
public class Event {

	private Lane lane;
	private int value;
	private Chip chip;

	/**
	 * @return the chip.
	 */
	public Chip getChip() {
		return this.chip;
	}

	/**
	 * @param chip the chip to set.
	 */
	public void setChip(Chip chip) {
		this.chip = chip;
	}

	/**
	 * @return the lane.
	 */
	public Lane getLane() {
		return this.lane;
	}

	/**
	 * @param lane the lane to set.
	 */
	public void setLane(Lane lane) {
		this.lane = lane;
	}

	/**
	 * @return the value.
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
