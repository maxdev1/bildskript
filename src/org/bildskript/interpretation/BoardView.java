package org.bildskript.interpretation;

import org.bildskript.interpretation.events.Event;

/**
 *
 */
public interface BoardView {

	/**
	 * @param state
	 */
	void publishState(BoardViewEvent state);

	/**
	 * @param e
	 */
	void showEvent(Event e);

	/**
	 * @return
	 */
	int getStepPause();

}
