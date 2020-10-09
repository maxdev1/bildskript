package org.bildskript.parser;

import java.awt.Point;

import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;
import org.bildskript.parser.layout.LayoutDescriptor;
import org.bildskript.parser.syntax.Syntax;
import org.bildskript.parser.syntax.SyntaxItem;
import org.bildskript.parser.syntax.SyntaxList;

/**
 *
 */
public class ComponentFactory {

	/**
	 * @param p
	 * @param x
	 * @param y
	 * @return
	 */
	public static Component find(CircuitSource p, int x, int y) {

		for (Syntax synt : SyntaxList.getSyntaxes()) {

			LayoutDescriptor layout = ComponentFactory.findMatch(synt.getSynts(), p, x, y);

			if (layout != null) {

				try {
					Component comp = synt.getClazz().newInstance();
					comp.layout = layout;
					comp.prepare(p);
					return comp;
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * @return
	 */
	private static LayoutDescriptor findMatch(SyntaxItem[][][] syntaxesByRotation, CircuitSource source,
			int x, int y) {

		for (int rotation = 0; rotation < syntaxesByRotation.length; rotation++) {
			LayoutDescriptor desc = new LayoutDescriptor();
			if (ComponentFactory.tryMatching(syntaxesByRotation[rotation], source, x, y, desc)) {
				return desc;
			}
		}

		return null;
	}

	/**
	 * @return
	 */
	private static boolean tryMatching(SyntaxItem[][] syntax, CircuitSource source, int x, int y,
			LayoutDescriptor desc) {

		int syntaxHeight = syntax.length;
		int syntaxWidth = syntax[0].length;

		for (int py = 0; py < syntaxHeight; py++) {
			for (int px = 0; px < syntaxWidth; px++) {

				int absX = x + px;
				int absY = y + py;

				// Border checking
				// If we are at the border we need to check for pins
				if ((px == 0) || (py == 0) || (px == (syntaxWidth - 1))
						|| (py == (syntaxHeight - 1))) {

					if (syntax[py][px].type == SyntaxItem.IN) { // Wants IN?
						// Is lane connected?
						if (source.get(absX, absY) == PixelType.LANE) {
							desc.getInLocations().put(syntax[py][px].additional,
									new Point(absX, absY));
						}
						continue;

					} else if (syntax[py][px].type == SyntaxItem.OUT) { // Wants OUT?
						// Is lane connected?
						if (source.get(absX, absY) == PixelType.LANE) {
							desc.getOutLocations().put(syntax[py][px].additional,
									new Point(absX, absY));
						}
						continue;

					} else if (syntax[py][px].type == SyntaxItem.REQUIRED_IN) {
						// Lane required
						if (source.get(absX, absY) == PixelType.LANE) {
							desc.getInLocations().put(syntax[py][px].additional,
									new Point(absX, absY));
						} else {
							return false;
						}

					} else if (syntax[py][px].type == SyntaxItem.REQUIRED_OUT) {
						// Lane required
						if (source.get(absX, absY) == PixelType.LANE) {
							desc.getOutLocations().put(syntax[py][px].additional,
									new Point(absX, absY));
						} else {
							return false;
						}

					} else if (syntax[py][px].type == SyntaxItem.SPACE) { // Need space
						if (source.get(absX, absY) != PixelType.SPACE) {
							return false;
						}
					}

					continue;
				}

				// Content checking
				// If the syntax awaits a component pixel, check for it
				if (syntax[py][px].type == SyntaxItem.COMPONENT) {
					if (source.get(absX, absY) != PixelType.COMPONENT) {
						return false;
					}
				}
				// Otherwise, allow anything except a component pixel
				if (syntax[py][px].type == SyntaxItem.SPACE) {
					if (source.get(absX, absY) == PixelType.COMPONENT) {
						return false;
					}
				}
			}
		}

		desc.getBounds().x = x;
		desc.getBounds().y = y;
		desc.getBounds().width = syntaxWidth;
		desc.getBounds().height = syntaxHeight;

		return true;
	}
}
