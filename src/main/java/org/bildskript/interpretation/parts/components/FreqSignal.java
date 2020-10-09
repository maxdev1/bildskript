package org.bildskript.interpretation.parts.components;


/**
 *
 */
public class FreqSignal extends Component {

	private int baseFrequency = 1000;
	private int frequencyDivider = 10;
	private FreqSignalThread signalThread;

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "freqsignal";
	}

	/**
	 * @return the baseFrequency.
	 */
	public int getBaseFrequency() {
		return this.baseFrequency;
	}

	/**
	 * @return the frequencyDivider.
	 */
	public int getFrequencyDivider() {
		return this.frequencyDivider;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.outs.size() != 1) {
			this.addValidationError("out pin missing");
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return "ticker | div: " + this.frequencyDivider;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#start()
	 */
	@Override
	public void start() {
		this.signalThread = new FreqSignalThread(this);
		this.signalThread.start();
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#reset()
	 */
	@Override
	public void reset() {
		if (this.signalThread != null) {
			this.signalThread.cancel();
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return false;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {
		if (pin == 0) {
			this.frequencyDivider = value;
		}
	}

}
