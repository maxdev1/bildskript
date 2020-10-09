package org.bildskript.parser.input;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 */
public abstract class CircuitSource {

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract PixelType get(int x, int y);

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract void set(int x, int y, PixelType p);

	/**
	 *
	 */
	public abstract int getWidth();

	/**
	 *
	 */
	public abstract int getHeight();

	/**
	 * @param source
	 */
	public void insert(Point pos, CircuitSource source) {
		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				PixelType col = source.get(x, y);

				// dont paste space
				if (col != PixelType.SPACE) {
					this.set(pos.x + x, pos.y + y, col);
				}
			}
		}
	}

	/**
	 * @param subRect
	 * @return
	 */
	public abstract CircuitSource ripArea(Rectangle subRect);

	/**
	 * @param selectedRect
	 * @return
	 */
	public abstract CircuitSource copy(Rectangle selectedRect);

}
