package org.bildskript.interpretation.parts;

import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.events.Event;
import org.bildskript.interpretation.events.EventAcceptor;
import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;

/**
 *
 */
public class Board {

	private Chip chip;
	private List<Board> subBoards;
	private EventAcceptor acceptor;

	/**
	 * @param chip
	 *            the chip to set
	 */
	public void setChip(Chip root) {
		this.chip = root;
	}

	/**
	 * @return the chip
	 */
	public Chip getChip() {
		return this.chip;
	}

	/**
	 * @return the subBoards
	 */
	public List<Board> getSubBoards() {
		return this.subBoards;
	}

	/**
	 * @param subBoards
	 *            the subBoards to set
	 */
	public void setSubBoards(List<Board> subBoards) {
		this.subBoards = subBoards;
	}

	/**
	 *
	 */
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		for (Component c : this.chip.getComponents()) {
			c.validate();
			errors.addAll(c.getValidationErrors());
		}

		return errors;
	}

	/**
	 *
	 */
	public void initialize() {
		this.chip.initialize();
	}

	/**
	 *
	 */
	public void reset() {
		this.chip.reset();
	}

	/**
	 * @param chip
	 * @param lane
	 * @param value
	 * @return
	 */
	public boolean acceptEvent(Chip chip, Lane lane, int value) {
		Event e = new Event();
		e.setLane(lane);
		e.setValue(value);
		e.setChip(chip);

		if (this.acceptor != null) {
			return this.acceptor.acceptEvent(e);
		}
		return true;
	}

	/**
	 * @param acceptor
	 */
	public void setEventAcceptor(EventAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	/**
	 * @return
	 */
	public CircuitSource flattenToSource() {
		CircuitSource out = this.chip.getSource();

		for (Board board : this.subBoards) {
			Chip chip = board.getChip();
			out.insert(chip.getBounds().getLocation(), chip.getSource());
		}

		return out;
	}

}
