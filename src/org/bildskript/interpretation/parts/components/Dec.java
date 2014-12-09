package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Dec extends Component {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "dec";
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 1) {
			this.addValidationError("invalid number of in pins");
		}
		if (this.outs.size() != 1) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {

		if (pin == 0) {
			this.write(0, value - 1);
		}
	}
}
