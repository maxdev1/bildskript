package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Div extends Component {

	private Integer a;
	private Integer b;

	/**
	 *
	 */
	public Div() {
		this.a = null;
		this.b = null;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "div";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return "divider | a: " + this.a + ", b: " + this.b;
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 2) {
			this.addValidationError("invalid number of in pins");
		}
		if (this.outs.size() != 2) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {

		if (pin == 0) {
			this.a = value;
		} else if (pin == 1) {
			this.b = value;
		} else {
			System.err.println("unknown pin " + pin);
		}

		if ((this.a != null) && (this.b != null)) {
			this.write(0, this.a / this.b);
			this.write(1, this.a % this.b);
			this.a = null;
			this.b = null;
		}
	}

}
