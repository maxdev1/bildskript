package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Dummy extends Component {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "dummy";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return "dummy";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {
	}

}
