package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class FreqSignalThread extends Thread {

	private FreqSignal freqSignal;
	private boolean stopped;

	/**
	 * @param freqSignal
	 */
	public FreqSignalThread(FreqSignal freqSignal) {
		this.freqSignal = freqSignal;
		this.stopped = false;
	}

	/**
	 *
	 */
	public void cancel() {
		this.stopped = true;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!this.stopped) {
			try {
				Thread.sleep(this.freqSignal.getBaseFrequency()
						/ this.freqSignal.getFrequencyDivider());

				if (this.stopped) {
					break;
				}
				this.freqSignal.write(0, 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
