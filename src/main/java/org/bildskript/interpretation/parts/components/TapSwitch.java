package org.bildskript.interpretation.parts.components;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.bildskript.parser.input.PixelType;

/**
 *
 */
public class TapSwitch extends Component {

	private boolean tapped;

	/**
	 *
	 */
	public TapSwitch() {
		this.tapped = false;
	}

	/**
	 * @ * @see
	 * org.bildskript.interpretation.parts.components.Component#validate()
	 */
	@Override
	public void validate() {
		if (this.outs.size() != 1) {
			this.addValidationError("invalid number of out pins");
		}
	}

	/**
	 * @return the tapped.
	 */
	public boolean isTapped() {
		return this.tapped;
	}

	/**
	 * @param tapped
	 *            the tapped to set.
	 */
	public void setTapped(boolean tapped) {
		this.tapped = tapped;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "tapswitch";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return "tapper";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#paint(java.awt.Graphics,
	 *      int)
	 */
	@Override
	public void paint(Graphics g, int fieldSize) {

		if (this.tapped) {
			Rectangle b = this.layout.getBounds();
			g.setColor(PixelType.COMPONENT.getReferenceColor());

			// By rotation
			if (b.width > b.height) {
				g.fillRect((b.x * fieldSize) + (3 * fieldSize),
						(b.y * fieldSize) + (2 * fieldSize), fieldSize,
						fieldSize);
			} else {
				g.fillRect((b.x * fieldSize) + (2 * fieldSize),
						(b.y * fieldSize) + (3 * fieldSize), fieldSize,
						fieldSize);
			}
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int,
	 *      int)
	 */
	@Override
	public void accept(int pin, int value) {

		// Virtual pin by mouse click
		if (pin == 0xFF) {
			this.tapped = true;
			new TapSwitchSignalThread(this).start();
		}

	}

}
