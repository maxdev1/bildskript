package org.bildskript.interpretation.parts.components;

/**
 *
 */
public class TapSwitchSignalThread extends Thread {

	private TapSwitch swit;

	/**
	 *
	 */
	public TapSwitchSignalThread(TapSwitch tap) {
		this.swit = tap;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		this.swit.write(0, 1);
		this.swit.setTapped(false);
	}

}
