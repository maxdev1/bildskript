package org.bildskript.interpretation.parts.components;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bildskript.interpretation.parts.Chip;
import org.bildskript.interpretation.parts.Lane;
import org.bildskript.interpretation.parts.ValidationError;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.layout.LayoutDescriptor;

/**
 *
 */
public abstract class Component {

	public Map<Integer, Lane> ins;
	public Map<Integer, Lane> outs;
	public LayoutDescriptor layout;
	public Chip chip;

	private List<ValidationError> validationErrors;

	/**
	 *
	 */
	public Component() {
		this.ins = new HashMap<Integer, Lane>();
		this.outs = new HashMap<Integer, Lane>();
		this.validationErrors = new ArrayList<>();
	}

	/**
	 * @return
	 */
	public abstract String getName();

	/**
	 *
	 */
	public void validate() {}

	/**
	 *
	 */
	public void addValidationError(String text) {
		this.validationErrors.add(new ValidationError(this, text));
	}

	/**
	 * @return the validationErrors.
	 */
	public List<ValidationError> getValidationErrors() {
		return this.validationErrors;
	}

	/**
	 * @param pin
	 * @param value
	 */
	public abstract void accept(int pin, int value);

	/**
	 *
	 */
	public void prepare(CircuitSource p) {}

	/**
	 * Starts any parallel work that the component may do.
	 */
	public void start() {}

	/**
	 * @return true if all parallel work the component does is finished
	 */
	public boolean isFinished() {
		return true;
	}

	/**
	 * @return
	 */
	public String makeTooltip() {
		return this.getName();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "name: ";
		res += this.getName() + " resolvedIns:(";
		for (int i = 0; i < this.ins.size(); i++) {
			res += " " + this.ins.get(i).toString();
		}
		res += " ) resolvedOuts:(";
		for (int i = 0; i < this.outs.size(); i++) {
			res += " " + this.outs.get(i).toString();
		}
		res += " ) layoutIns:(";
		for (int i : this.layout.getInLocations().keySet()) {
			res += " " + i + ":" + this.layout.getInLocations().get(i).x + "/"
					+ this.layout.getInLocations().get(i).y;
		}
		res += " ) layoutOuts:(";
		for (int o : this.layout.getOutLocations().keySet()) {
			res += " " + o + ":" + this.layout.getOutLocations().get(o).x + "/"
					+ this.layout.getOutLocations().get(o).y;
		}
		res += " )";

		return res;
	}

	/**
	 *
	 */
	public void write(int pin, int value) {

		if ((pin < this.outs.size()) && (pin >= 0)) {

			Lane lane = this.outs.get(pin);
			Component target = lane.getTarget();

			if (target != null) {
				if (this.chip.getBoard().acceptEvent(this.chip, lane, value)) {
					target.accept(lane.getTargetPin(), value);
				}
			}

		} else {
			System.err.println("chip error, " + this.getName() + " misses output pin " + pin);
		}
	}

	/**
	 * @param g
	 * @param fieldSize
	 */
	public void paint(Graphics g, int fieldSize) {}

	/**
	 *
	 */
	public void reset() {}

}
