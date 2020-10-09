package org.bildskript.interpretation.parts;

import java.awt.Color;

/**
 *
 */
public class IdentifiedChip extends Chip {

	private Color[][] identifier;

	/**
	 * 
	 */
	public IdentifiedChip(Color[][] identifier) {
		this.identifier = identifier;
	}

}
