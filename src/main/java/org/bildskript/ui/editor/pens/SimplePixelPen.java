package org.bildskript.ui.editor.pens;

import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;

/**
 *
 */
public class SimplePixelPen extends Pen {

	private PixelType type;

	/**
	 * @param type
	 */
	public SimplePixelPen(PixelType type) {
		this.type = type;
	}

	/**
	 * @see org.bildskript.ui.editor.pens.Pen#paint(int, int)
	 */
	@Override
	public void paint(CircuitSource out, int x, int y) {
		out.set(x, y, this.type);
	}

}
