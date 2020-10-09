package org.bildskript.interpretation.parts;

import java.awt.Point;
import java.util.List;

import org.bildskript.interpretation.parts.components.Component;

/**
 *
 */
public class Lane {

	private int sourcePin;
	private int targetPin;
	private Component source;
	private Component target;
	private List<Point> path;

	/**
	 *
	 */
	public Lane() {
		this.sourcePin = -1;
		this.targetPin = -1;
	}

	/**
	 * @param path
	 */
	public static void wire(int sourcePin, Component source, int targetPin, Component target,
			List<Point> path) {
		Lane lane = new Lane();
		lane.source = source;
		lane.sourcePin = sourcePin;
		lane.target = target;
		lane.targetPin = targetPin;
		lane.path = path;

		if (source != null) {
			source.outs.put(sourcePin, lane);
		}
		if (target != null) {
			target.ins.put(targetPin, lane);
		}
	}

	/**
	 * @return the path.
	 */
	public List<Point> getPath() {
		return this.path;
	}

	/**
	 *
	 */
	public void write(int value) {
		this.target.accept(this.targetPin, value);
	}

	/**
	 * @return the sourcePin
	 */
	public int getSourcePin() {
		return this.sourcePin;
	}

	/**
	 * @return the source
	 */
	public Component getSource() {
		return this.source;
	}

	/**
	 * @return the targetPin
	 */
	public int getTargetPin() {
		return this.targetPin;
	}

	/**
	 * @return the target
	 */
	public Component getTarget() {
		return this.target;
	}

}
