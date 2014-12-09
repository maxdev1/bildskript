package org.bildskript.parser.syntax;

import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.parts.components.Component;

/**
 *
 */
public class SyntaxList {

	private static List<Syntax> componentSyntaxes = new ArrayList<Syntax>();

	/**
	 * @return the componentSyntaxes
	 */
	public static List<Syntax> getSyntaxes() {
		return componentSyntaxes;
	}

	/**
	 *
	 */
	public static void addSyntax(Class<? extends Component> clazz, SyntaxItem[][] syntax) {

		SyntaxItem[][][] synts = new SyntaxItem[4][][];
		synts[0] = syntax;
		synts[1] = SyntaxList.rotate90(synts[0]);
		synts[2] = SyntaxList.rotate90(synts[1]);
		synts[3] = SyntaxList.rotate90(synts[2]);

		componentSyntaxes.add(new Syntax(clazz, synts));
	}

	/**
	 * @param syntax
	 * @return
	 */
	private static SyntaxItem[][] rotate90(SyntaxItem[][] syntax) {
		final int da = syntax.length;
		final int db = syntax[0].length;

		SyntaxItem[][] rot = new SyntaxItem[db][da];
		for (int r = 0; r < da; r++) {
			for (int c = 0; c < db; c++) {
				rot[c][da - 1 - r] = syntax[r][c];
			}
		}
		return rot;
	}

}
