package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Dup extends Component {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "dup";
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 1) {
			this.addValidationError("missing in pin");
		}
		if (this.outs.size() != 2) {
			this.addValidationError("does not have two out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {
		if (pin == 0) {
			this.write(0, value);
			this.write(1, value);
		} else {
			System.err.println("unknown pin " + pin);
		}
	}

}
