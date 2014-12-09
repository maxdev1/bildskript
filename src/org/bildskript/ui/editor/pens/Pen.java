package org.bildskript.ui.editor.pens;

import org.bildskript.parser.input.CircuitSource;

/**
 *
 */
public abstract class Pen {

	/**
	 * @param source
	 */
	public abstract void paint(CircuitSource source, int x, int y);

}
