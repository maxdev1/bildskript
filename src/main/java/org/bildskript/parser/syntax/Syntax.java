package org.bildskript.parser.syntax;

import org.bildskript.interpretation.parts.components.Component;

/**
 *
 */
public class Syntax {

	private Class<? extends Component> clazz;
	private SyntaxItem[][][] synts;

	/**
	 * @param clazz
	 * @param synts
	 */
	public Syntax(Class<? extends Component> clazz, SyntaxItem[][][] synts) {
		this.clazz = clazz;
		this.synts = synts;
	}

	/**
	 * @return the clazz
	 */
	public Class<? extends Component> getClazz() {
		return this.clazz;
	}

	/**
	 * @return the synts
	 */
	public SyntaxItem[][][] getSynts() {
		return this.synts;
	}

}
