package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class InitSignal extends Component {

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "initsignal";
	}

	/**
	 * @ * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.ins.size() != 0) {
			this.addValidationError("invalid number of in pins");
		}
		if (this.outs.size() != 1) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#start()
	 */
	@Override
	public void start() {
		new Thread() {

			/**
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run() {
				InitSignal.this.accept(0, 1);
			}

		}.start();
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {
		this.write(0, value);
	}

}
