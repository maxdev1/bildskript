package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class If extends Component {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "if";
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
			if (value == 0) {
				this.write(1, 1);
			} else {
				this.write(0, 1);
			}
		} else {
			System.err.println("unknown pin " + pin);
		}
	}

}
