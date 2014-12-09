package org.bildskript.interpretation.parts.components;

/**
 *
 */
public abstract class TwoInOneOut extends Component {

	protected Integer a;
	protected Integer b;

	/**
	 *
	 */
	public TwoInOneOut() {
		this.reset();
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#reset()
	 */
	@Override
	public void reset() {
		this.a = null;
		this.b = null;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return this.getName() + " | a: " + this.a + ", b: " + this.b;
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
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
	 * @return
	 */
	public abstract int makeResult();

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
			this.write(0, this.makeResult());
			this.a = null;
			this.b = null;
		}
	}

}
