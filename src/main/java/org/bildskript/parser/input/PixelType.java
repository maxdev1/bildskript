package org.bildskript.parser.input;

import java.awt.Color;

/**
 *
 */
public enum PixelType {

	COMPONENT(255, 0, 80), LANE(0, 0, 0), LANE_HOP(128, 128, 128), SPACE(255, 255, 255);

	private Color referenceColor;

	/**
	 *
	 */
	private PixelType(int r, int g, int b) {
		this.referenceColor = new Color(r, g, b);
	}

	/**
	 *
	 */
	public static PixelType getNearest(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		if ((r > g) && (r > b)) {
			return COMPONENT;
		}

		if ((r < 70) && (g < 70) && (b < 70)) {
			return LANE;
		}

		if ((r < 130) && (g < 130) && (b < 130)) {
			return LANE_HOP;
		}

		return SPACE;
	}

	/**
	 * @return
	 */
	public Color getReferenceColor() {
		return this.referenceColor;
	}
}
