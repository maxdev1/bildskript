package org.bildskript.interpretation.parts;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;

/**
 *
 */
public class Chip {

	public List<Component> components;
	private CircuitSource source;
	private ChipOutHandler outHandler;
	private Board board;
	private Rectangle bounds;

	/**
	 *
	 */
	public Chip() {
		this.components = new ArrayList<Component>();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.board.getChip() == this) {
			return "Root chip";
		}
		return "Subchip";
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return this.bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * @return the source.
	 */
	public CircuitSource getSource() {
		return this.source;
	}

	/**
	 * @param source the source to set.
	 */
	public void setSource(CircuitSource source) {
		this.source = source;
	}

	/**
	 * @return the components
	 */
	public List<Component> getComponents() {
		return this.components;
	}

	/**
	 * @param c
	 */
	public void add(Component c) {
		this.components.add(c);
	}

	/**
	 *
	 */
	public void initialize() {

		// Send init signals
		for (Component c : this.components) {
			c.start();
		}

		// Wait for all components to finish
		while (true) {
			boolean allFinished = true;

			for (Component c : this.components) {
				if (!c.isFinished()) {
					allFinished = false;
				}
			}

			if (allFinished) {
				break;
			} else {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {}
			}
		}

	}

	/**
	 * @param outHandler the outHandler to set.
	 */
	public void setOutHandler(ChipOutHandler outHandler) {
		this.outHandler = outHandler;
	}

	/**
	 * @param pin
	 * @param value
	 */
	public void writeOut(int pin, int value) {
		if (this.outHandler != null) {
			this.outHandler.write(pin, value);
		} else {
			System.out.println("handlerless chip output: #" + pin + ": " + value);
		}
	}

	/**
	 *
	 */
	public void reset() {
		for (Component component : this.components) {
			component.reset();
		}
	}

}
