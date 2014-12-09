package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class BitAnd extends TwoInOneOut {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "bitand";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.TwoInOneOut#makeResult()
	 */
	@Override
	public int makeResult() {
		return a & b;
	}
}
