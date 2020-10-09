package org.bildskript.parser.input;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 */
public class ImageCircuitSource extends CircuitSource {

	private BufferedImage img;

	/**
	 *
	 */
	public void load(BufferedImage img) {
		this.img = img;

		this.tryShrink();
	}

	/**
	 * @return
	 */
	public static CircuitSource createBlank() {
		ImageCircuitSource source = new ImageCircuitSource();
		BufferedImage emptyImg = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g = emptyImg.createGraphics();
		g.setColor(PixelType.SPACE.getReferenceColor());
		g.fillRect(0, 0, 1, 1);
		g.dispose();

		source.load(emptyImg);
		return source;
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#get(int, int)
	 */
	@Override
	public PixelType get(int x, int y) {
		if (this.img == null) {
			return PixelType.SPACE;
		}

		if ((this.img.getHeight() > y) && (this.img.getWidth() > x) && (x >= 0)
				&& (y >= 0)) {
			return PixelType.getNearest(new Color(this.img.getRGB(x, y)));
		}

		return PixelType.SPACE;
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#set(int, int,
	 *      org.bildskript.parser.input.PixelType)
	 */
	@Override
	public void set(int x, int y, PixelType p) {
		if (this.img == null) {
			return;
		}

		this.ensureCapacity(x, y);
		this.img.setRGB(x, y, p.getReferenceColor().getRGB());

		this.tryShrink();
	}

	/**
	 * @param x
	 * @param y
	 */
	private void ensureCapacity(int x, int y) {
		int ow = this.img.getWidth();
		int oh = this.img.getHeight();
		int nw = ow;
		int nh = oh;

		if (x >= ow) {
			nw = x + 3;
		}
		if (y >= oh) {
			nh = y + 3;
		}

		if ((nw > ow) || (nh > oh)) {
			this.resizeTo(nw, nh);
		}
	}

	/**
	 *
	 */
	private void tryShrink() {
		int miny;
		yloop: for (miny = this.getHeight() - 1; miny >= 0; miny--) {
			for (int x = this.getWidth() - 1; x >= 0; x--) {
				if (this.get(x, miny) != PixelType.SPACE) {
					break yloop;
				}
			}
		}

		int minx;
		xloop: for (minx = this.getWidth() - 1; minx >= 0; minx--) {
			for (int y = this.getHeight() - 1; y >= 0; y--) {
				if (this.get(minx, y) != PixelType.SPACE) {
					break xloop;
				}
			}
		}

		this.resizeTo(minx + 3, miny + 3);
	}

	/**
	 * @param h
	 * @param w
	 */
	private void resizeTo(int w, int h) {
		BufferedImage resizedImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D resizedGraphics = resizedImage.createGraphics();
		resizedGraphics.setColor(PixelType.SPACE.getReferenceColor());
		resizedGraphics.fillRect(0, 0, w, h);
		resizedGraphics.drawImage(this.img, 0, 0, null);
		resizedGraphics.dispose();

		this.img = resizedImage;
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#getWidth()
	 */
	@Override
	public int getWidth() {
		if (this.img == null) {
			return 0;
		}

		return this.img.getWidth();
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#getHeight()
	 */
	@Override
	public int getHeight() {
		if (this.img == null) {
			return 0;
		}

		return this.img.getHeight();
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#ripArea(java.awt.Rectangle)
	 */
	@Override
	public CircuitSource ripArea(Rectangle subRect) {

		CircuitSource subSource = this.copy(subRect);

		// Delete
		Graphics2D imgGraphics = this.img.createGraphics();
		imgGraphics.setColor(PixelType.SPACE.getReferenceColor());
		imgGraphics.fillRect(subRect.x, subRect.y, subRect.width,
				subRect.height);
		imgGraphics.dispose();

		return subSource;
	}

	/**
	 * @see org.bildskript.parser.input.CircuitSource#copy(java.awt.Rectangle)
	 */
	@Override
	public CircuitSource copy(Rectangle subRect) {

		ImageCircuitSource imageCircuitSource = new ImageCircuitSource();

		if (this.img != null) {
			this.ensureCapacity(subRect.x + subRect.width, subRect.y
					+ subRect.height);

			BufferedImage subImg = new BufferedImage(subRect.width,
					subRect.height, BufferedImage.TYPE_INT_RGB);

			Graphics2D subImgGraphics = subImg.createGraphics();
			subImgGraphics.drawImage(this.img.getSubimage(subRect.x, subRect.y,
					subRect.width, subRect.height), 0, 0, null);
			subImgGraphics.dispose();

			imageCircuitSource.load(subImg);
		}

		return imageCircuitSource;
	}

}
