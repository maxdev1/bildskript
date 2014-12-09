package org.bildskript.parser;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bildskript.interpretation.parts.Lane;
import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;

/**
 *
 */
public class LaneWalker {

	/**
	 *
	 */
	public static void createLane(List<Component> availableComponents, CircuitSource circuitSource,
			int startX, int startY) {

		List<Point> path = findLane(circuitSource, startX, startY);
		if (path.size() == 0) {
			return;
		}

		// Find in and out components
		Point start = new Point(startX, startY);
		Point end = path.get(path.size() - 1);

		Component source = null;
		int sourcePin = -1;
		sourceSearch: for (Component component : availableComponents) {
			for (int i = 0; i < component.layout.getOutLocations().size(); i++) {
				Point outPinPos = component.layout.getOutLocations().get(i);
				if (outPinPos.equals(start)) {
					source = component;
					sourcePin = i;
					break sourceSearch;
				}
			}
		}

		Component target = null;
		int targetPin = -1;
		targetSearch: for (Component component : availableComponents) {

			for (int pin : component.layout.getInLocations().keySet()) {

				Point inPinPos = component.layout.getInLocations().get(pin);

				if (inPinPos.equals(end)) {
					target = component;
					targetPin = pin;
					break targetSearch;
				}
			}
		}

		// System.out.println("wiring lane from " + (source != null ?
		// source.getName() : "nothing")
		// + " at #" + sourcePin + " to " + (target != null ? target.getName() :
		// "nothing")
		// + " at #" + targetPin);
		Lane.wire(sourcePin, source, targetPin, target, path);
	}

	/**
	 * @param circuitSource
	 * @param startX
	 * @param startY
	 * @return
	 */
	public static List<Point> findLane(CircuitSource circuitSource, int startX, int startY) {

		int x = startX;
		int y = startY;

		Point previous = null;
		List<Point> path = new ArrayList<Point>();

		while (true) {
			Point p = new Point(x, y);
			if (path.contains(p)) {
				break;
			}
			path.add(p);

			// Check if lane goes on normally
			if (circuitSource.get(x, y + 1) == PixelType.LANE) {
				if ((previous == null) || (previous.distance(x, y + 1) != 0)) {
					previous = p;
					++y;
					continue;
				}
			}

			if (circuitSource.get(x, y - 1) == PixelType.LANE) {
				if ((previous == null) || (previous.distance(x, y - 1) != 0)) {
					previous = p;
					--y;
					continue;
				}
			}

			if (circuitSource.get(x + 1, y) == PixelType.LANE) {
				if ((previous == null) || (previous.distance(x + 1, y) != 0)) {
					previous = p;
					++x;
					continue;
				}
			}

			if (circuitSource.get(x - 1, y) == PixelType.LANE) {
				if ((previous == null) || (previous.distance(x - 1, y) != 0)) {
					previous = p;
					--x;
					continue;
				}
			}

			// Check if lane goes on with hop
			if (circuitSource.get(x, y + 1) == PixelType.LANE_HOP) {
				if ((previous == null) || (previous.distance(x, y + 1) != 0)) {
					previous = p;
					y += 5;
					continue;
				}
			}

			if (circuitSource.get(x, y - 1) == PixelType.LANE_HOP) {
				if ((previous == null) || (previous.distance(x, y - 1) != 0)) {
					previous = p;
					y -= 5;
					continue;
				}
			}

			if (circuitSource.get(x + 1, y) == PixelType.LANE_HOP) {
				if ((previous == null) || (previous.distance(x + 1, y) != 0)) {
					previous = p;
					x += 5;
					continue;
				}
			}

			if (circuitSource.get(x - 1, y) == PixelType.LANE_HOP) {
				if ((previous == null) || (previous.distance(x - 1, y) != 0)) {
					previous = p;
					x -= 5;
					continue;
				}
			}

			// None found?
			break;
		}

		return path;
	}
}
