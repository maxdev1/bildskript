package org.bildskript.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.bildskript.parser.input.CircuitSource;

/**
 *
 */
public class CircuitSourcePainter {

	public static final int MINIMUM_ZOOM = 1;
	public static final int DEFAULT_ZOOM = 4;
	public static final int MAXIMUM_ZOOM = 20;

	/**
	 *
	 */
	public static BufferedImage paint(CircuitSource source, int zoom) {

		BufferedImage image = new BufferedImage(source.getWidth() * zoom,
				source.getHeight() * zoom, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = image.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, source.getWidth() * zoom, source.getHeight() * zoom);

		for (int y = 0; y < source.getHeight(); y++) {
			for (int x = 0; x < source.getWidth(); x++) {
				g.setColor(source.get(x, y).getReferenceColor());
				g.fillRect(x * zoom, y * zoom, zoom, zoom);
			}
		}

		g.dispose();

		return image;
	}
}
