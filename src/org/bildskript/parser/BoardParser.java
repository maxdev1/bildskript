package org.bildskript.parser;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.parts.Board;
import org.bildskript.interpretation.parts.Chip;
import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;

/**
 *
 */
public class BoardParser {

	private static final int SUBCHIP_MINIMUM_WIDTH = 15;
	private static final int SUBCHIP_MINIMUM_HEIGHT = 8;

	private CircuitSource source;
	private List<Rectangle> subBoardBoxes;
	private Rectangle absoluteBounds;

	/**
	 *
	 */
	public BoardParser(CircuitSource circuitSource, Rectangle bounds) {
		this.source = circuitSource;
		this.absoluteBounds = bounds;
		this.subBoardBoxes = new ArrayList<Rectangle>();
	}

	/**
	 * @return
	 */
	public Board parse() {

		Board board = new Board();
		board.setSubBoards(this.ripSubBoards());

		Chip rootChip = this.parseChip();
		rootChip.setBounds(this.absoluteBounds);
		rootChip.setBoard(board);
		board.setChip(rootChip);
		return board;
	}

	/**
	 *
	 */
	public List<Board> ripSubBoards() {
		this.findFunctionBoxes();

		List<Board> subBoards = new ArrayList<Board>();

		for (Rectangle subRect : this.subBoardBoxes) {
			subRect = new Rectangle(subRect.x + 1, subRect.y + 1,
					subRect.width - 2, subRect.height - 2);
			CircuitSource subParsable = this.source.ripArea(subRect);
			subBoards.add(new BoardParser(subParsable, subRect).parse());
		}

		return subBoards;
	}

	/**
	 * @return
	 */
	private Chip parseChip() {
		Chip chip = new Chip();
		chip.setSource(this.source);

		// Find components
		for (int y = 0; y < this.source.getHeight(); y++) {
			scanLoop: for (int x = 0; x < this.source.getWidth(); x++) {

				// Check if any of the components contains this point (except
				// the border)
				for (Component component : chip.getComponents()) {
					Rectangle bounds = component.layout.getBounds();
					Rectangle boundsWithoutBorder = new Rectangle(bounds.x + 1,
							bounds.y + 1, bounds.width - 2, bounds.height - 2);
					if (boundsWithoutBorder.contains(x, y)) {
						continue scanLoop;
					}
				}

				// Try to create a component here
				Component n = ComponentFactory.find(this.source, x, y);
				if (n != null) {
					n.chip = chip;
					chip.add(n);
				}
			}
		}

		// Wire lanes
		for (Component comp : chip.getComponents()) {
			for (int o = 0; o < comp.layout.getOutLocations().size(); o++) {
				Point pinPos = comp.layout.getOutLocations().get(o);

				LaneWalker.createLane(chip.getComponents(), this.source,
						pinPos.x, pinPos.y);
			}
		}

		return chip;
	}

	/**
	 *
	 */
	private void findFunctionBoxes() {

		for (int y = 0; y < this.source.getHeight(); y++) {
			scanLoop: for (int x = 0; x < this.source.getWidth(); x++) {

				// Already in a function box? Skip
				for (Rectangle rectangle : this.subBoardBoxes) {
					if (rectangle.contains(x, y)) {
						continue scanLoop;
					}
				}

				// Does it have at least 4 pixels on top left?
				if (!((this.source.get(x, y) == PixelType.COMPONENT)
						&& (this.source.get(x + 1, y) == PixelType.COMPONENT)
						&& (this.source.get(x, y + 1) == PixelType.COMPONENT) && (this.source
								.get(x + 1, y + 1) == PixelType.COMPONENT))) {
					continue scanLoop;
				}

				// Does it have a line on the top?
				int topRightX = x + 2;
				while (this.source.get(topRightX, y) == PixelType.COMPONENT) {

					if (this.source.get(topRightX + 1, y) == PixelType.SPACE) {
						if (this.source.get(topRightX, y + 1) != PixelType.COMPONENT) {
							continue scanLoop;
						}
						break;
					} else if (this.source.get(topRightX, y - 1) == PixelType.COMPONENT) {
						continue scanLoop;

					}

					++topRightX;
				}

				// Does it have a correct right line?
				int bottomRightY = y + 1;
				while (this.source.get(topRightX, bottomRightY) == PixelType.COMPONENT) {

					if (this.source.get(topRightX, bottomRightY + 1) == PixelType.SPACE) {
						if (this.source.get(topRightX - 1, bottomRightY) != PixelType.COMPONENT) {
							continue scanLoop;
						}
						break;
					} else if (this.source.get(topRightX + 1, bottomRightY) != PixelType.SPACE) {
						continue scanLoop;
					}

					++bottomRightY;
				}

				// Does it have a correct bottom line?
				int bottomLeftX = topRightX - 1;
				while (this.source.get(bottomLeftX, bottomRightY) == PixelType.COMPONENT) {

					if (this.source.get(bottomLeftX - 1, bottomRightY) == PixelType.SPACE) {
						if (this.source.get(bottomLeftX, bottomRightY - 1) != PixelType.COMPONENT) {
							continue scanLoop;
						}
						break;
					} else if (this.source.get(bottomLeftX, bottomRightY + 1) != PixelType.SPACE) {
						continue scanLoop;
					}

					--bottomLeftX;
				}

				// Does it have a correct left line?
				int topLeftY = bottomRightY - 1;
				while (this.source.get(x, topLeftY) == PixelType.COMPONENT) {

					if (this.source.get(x, topLeftY - 1) == PixelType.SPACE) {
						break;
					} else if (this.source.get(x - 1, topLeftY) != PixelType.SPACE) {
						continue scanLoop;
					}

					--topLeftY;
				}

				// Box must have a minimum size
				int width = (topRightX - x) + 1;
				int height = (bottomRightY - y) + 1;

				if ((width < SUBCHIP_MINIMUM_WIDTH)
						|| (height < SUBCHIP_MINIMUM_HEIGHT)) {
					continue scanLoop;
				}

				this.subBoardBoxes.add(new Rectangle(x, y, width, height));
			}
		}

	}

}
