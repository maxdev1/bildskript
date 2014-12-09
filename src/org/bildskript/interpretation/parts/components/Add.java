package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Add extends TwoInOneOut {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "add";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.TwoInOneOut#makeResult()
	 */
	@Override
	public int makeResult() {
		return this.a + this.b;
	}

}
