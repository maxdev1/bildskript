package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class LogAnd extends TwoInOneOut {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "logand";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.TwoInOneOut#makeResult()
	 */
	@Override
	public int makeResult() {
		return ((a > 0) && (b > 0)) ? 1 : 0;
	}
}
