package org.bildskript.interpretation.parts.components;

import java.awt.Graphics;

import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;

/**
 *
 */
public abstract class Variable extends Component {

	private Integer value;
	private boolean verticalLayout;
	private int bits;

	/**
	 * @param bits
	 */
	public Variable(int bits) {
		this.bits = bits;
		this.value = 0;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#prepare()
	 */
	@Override
	public void prepare(CircuitSource p) {

		this.verticalLayout = this.layout.getBounds().height > this.layout.getBounds().width;
		this.loadInitialBits(p);
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#reset()
	 */
	@Override
	public void reset() {
		this.value = null;
	}

	/**
	 * @param p
	 */
	private void loadInitialBits(CircuitSource p) {
		this.value = 0;

		if (this.verticalLayout) {
			for (int i = 0; i < this.bits; i++) {
				if (p.get(this.layout.getBounds().x + 2, this.layout.getBounds().y + 2 + i) == PixelType.LANE) {
					this.value |= (1 << (this.bits - 1 - i));
				}
			}
		} else {
			for (int i = 0; i < this.bits; i++) {
				if (p.get(this.layout.getBounds().x + 2 + i, this.layout.getBounds().y + 2) == PixelType.LANE) {
					this.value |= (1 << (this.bits - 1 - i));
				}
			}
		}

	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int val) {
		if (pin == 0) {
			this.write(0, this.value);
		} else if (pin == 1) {
			this.value = val;
			if (this.value >= (1 << this.bits)) {
				this.value = 0;
			}
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#makeTooltip()
	 */
	@Override
	public String makeTooltip() {
		return this.bits + "bit var | value: " + this.value;
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#paint(java.awt.Graphics, int)
	 */
	@Override
	public void paint(Graphics g, int fieldSize) {

		int off = fieldSize * 2;

		if (this.verticalLayout) {
			for (int i = 0; i < this.bits; i++) {
				int bitX = off + (this.layout.getBounds().x * fieldSize);
				int bitY = off + (this.layout.getBounds().y * fieldSize) + (fieldSize * i);

				if ((this.value & (1 << (this.bits - 1 - i))) > 0) {
					g.setColor(PixelType.LANE.getReferenceColor());
				} else {
					g.setColor(PixelType.SPACE.getReferenceColor());
				}
				g.fillRect(bitX, bitY, fieldSize, fieldSize);
			}
		} else {
			for (int i = 0; i < this.bits; i++) {
				int bitX = off + (this.layout.getBounds().x * fieldSize) + (fieldSize * i);
				int bitY = off + (this.layout.getBounds().y * fieldSize);

				if ((this.value & (1 << (this.bits - 1 - i))) > 0) {
					g.setColor(PixelType.LANE.getReferenceColor());
				} else {
					g.setColor(PixelType.SPACE.getReferenceColor());
				}
				g.fillRect(bitX, bitY, fieldSize, fieldSize);
			}
		}
	}

}
