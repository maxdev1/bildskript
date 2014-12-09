package org.bildskript.interpretation.parts;

import org.bildskript.interpretation.parts.components.Component;

/**
 *
 */
public class ValidationError {

	private Component component;
	private String text;

	/**
	 * @param component
	 * @param text
	 */
	public ValidationError(Component component, String text) {
		this.component = component;
		this.text = text;
	}

	/**
	 * @return the component.
	 */
	public Component getComponent() {
		return this.component;
	}

	/**
	 * @return the text.
	 */
	public String getText() {
		return this.text;
	}
}
