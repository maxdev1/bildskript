package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Output extends Component {

	public static int OUTPUT_NUM_COUNTER = 0;
	private int num = OUTPUT_NUM_COUNTER++;

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "output";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return "output #" + this.num;
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 1) {
			this.addValidationError("invalid number of in pins");
		}
		if (this.outs.size() != 0) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {
		if (pin == 0) {
			this.chip.writeOut(this.num, value);
		} else {
			System.err.println("unknown pin " + pin);
		}
	}

}
