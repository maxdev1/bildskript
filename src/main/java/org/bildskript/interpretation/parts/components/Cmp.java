package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class Cmp extends Component {

	private Integer a;
	private Integer b;

	/**
	 *
	 */
	public Cmp() {
		this.a = null;
		this.b = null;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "cmp";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 2) {
			this.addValidationError("invalid number of in pins");
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
			this.write(0, (this.a > this.b) ? 1 : 0);
			this.write(1, (this.a == this.b) ? 1 : 0);
			this.write(2, (this.a < this.b) ? 1 : 0);
			this.a = null;
			this.b = null;
		}
	}

}
