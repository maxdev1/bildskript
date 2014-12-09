package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Decider extends Component {

	private Integer value;

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "decider";
	}

	/**
	 * @ * @see
	 * org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {

		if (this.ins.size() != 2) {
			this.addValidationError("invalid number of in pins");
		}
		if (this.outs.size() != 1) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int,
	 *      int)
	 */
	@Override
	public void accept(int pin, int val) {

		if (pin == 0) {
			this.value = val;

		} else if (pin == 1) {
			if (val != 0) {
				this.write(0, this.value);
				this.value = null;
			}
		}
	}

}
