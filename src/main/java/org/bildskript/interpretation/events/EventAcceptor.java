package org.bildskript.interpretation.events;


/**
 *
 */
public interface EventAcceptor {

	/**
	 * @param e
	 * @return
	 */
	boolean acceptEvent(Event e);

}
