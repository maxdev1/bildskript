package org.bildskript.parser.layout;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LayoutDescriptor {

	private Rectangle bounds;
	private Map<Integer, Point> inLocations;
	private Map<Integer, Point> outLocations;

	/**
	 *
	 */
	public LayoutDescriptor() {
		this.bounds = new Rectangle();
		this.inLocations = new HashMap<>();
		this.outLocations = new HashMap<>();
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return this.bounds;
	}

	/**
	 * @return the inLocations.
	 */
	public Map<Integer, Point> getInLocations() {
		return this.inLocations;
	}

	/**
	 * @return the outLocations.
	 */
	public Map<Integer, Point> getOutLocations() {
		return this.outLocations;
	}

}
