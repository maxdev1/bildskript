package org.bildskript.interpretation.parts.components;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 */
public class RgbDisplay16x16 extends Component {

	private Color[][] colors;

	private Integer x = null;
	private Integer y = null;
	private Integer r = null;
	private Integer g = null;
	private Integer b = null;

	/**
	 *
	 */
	public RgbDisplay16x16() {
		this.colors = new Color[16][];
		for (int i = 0; i < 16; i++) {
			this.colors[i] = new Color[16];
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#getName()
	 */
	@Override
	public String getName() {
		return "rgbdisplay16x16";
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#paint(java.awt.Graphics, int)
	 */
	@Override
	public void paint(Graphics g, int fieldSize) {
		super.paint(g, fieldSize);

		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				Color col = this.colors[y][x];
				if (col == null) {
					col = Color.black;
				}
				g.setColor(col);
				g.fillRect((this.layout.getBounds().x * fieldSize) + ((2 + x) * fieldSize),
						(this.layout.getBounds().y * fieldSize) + ((2 + y) * fieldSize), fieldSize,
						fieldSize);
			}
		}
	}

	/**
	 * @see org.bildskript.interpretation.parts.components.Component#accept(int, int)
	 */
	@Override
	public void accept(int pin, int value) {

		if (pin == 0) {
			this.x = value;
			if ((this.x < 0) || (this.x >= 16)) {
				this.x = 0;
			}

		} else if (pin == 1) {
			this.y = value;
			if ((this.y < 0) || (this.y >= 16)) {
				this.y = 0;
			}

		} else if (pin == 2) {
			this.r = value;
		} else if (pin == 3) {
			this.g = value;
		} else if (pin == 4) {
			this.b = value;
		}

		if ((this.x != null) && (this.y != null) && (this.r != null) && (this.g != null)
				&& (this.b != null)) {
			this.colors[this.y][this.x] = new Color(this.r, this.g, this.b);

			this.x = null;
			this.y = null;
			this.r = null;
			this.g = null;
			this.b = null;
		}
	}
}
