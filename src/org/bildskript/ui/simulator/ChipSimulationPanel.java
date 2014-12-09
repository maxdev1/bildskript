package org.bildskript.ui.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.bildskript.Style;
import org.bildskript.interpretation.events.Event;
import org.bildskript.interpretation.parts.Board;
import org.bildskript.interpretation.parts.Chip;
import org.bildskript.interpretation.parts.ChipOutHandler;
import org.bildskript.interpretation.parts.Lane;
import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.ui.CircuitSourcePainter;

/**
 *
 */
public class ChipSimulationPanel extends JPanel implements MouseMotionListener,
		MouseListener, ChipOutHandler {

	private static final long serialVersionUID = 1L;

	public static final Color LANE_HIGHLIGHT_COLOR = new Color(80, 150, 255);
	private static final Color OUTPUT_COLOR = new Color(120, 120, 120);
	private static final Color ERROR_HIGHLIGHT_COLOR = new Color(255, 0, 255);

	private int zoom = CircuitSourcePainter.DEFAULT_ZOOM;

	private Chip chip;
	private boolean isSubChip;
	private CircuitSource source;
	private HashMap<Integer, Integer> output;

	private BufferedImage sourceBuffer;
	private Event blockedEvent;

	private boolean lanePaintEnabled;

	private Point tooltipPoint;
	private Component tooltippedComponent;
	private Lane tooltippedLane;

	private List<ChipSimulationPanel> subChipPanels;

	/**
	 * @param chip
	 * @param subBoards
	 */
	public ChipSimulationPanel(Chip chip, List<Board> subBoards,
			boolean isSubChip) {
		this.isSubChip = isSubChip;
		this.chip = chip;
		this.initialize();
		this.configure();

		this.addSubChips(subBoards);
	}

	/**
	 * @param subBoards
	 */
	private void addSubChips(List<Board> subBoards) {

		for (Board subBoard : subBoards) {
			Chip subChip = subBoard.getChip();
			ChipSimulationPanel subChipPanel = new ChipSimulationPanel(subChip,
					subBoard.getSubBoards(), true);
			subChipPanel.setBounds(subChip.getBounds());
			this.addSubChipPanel(subChipPanel);
		}
	}

	/**
	 *
	 */
	public void initialize() {
		this.output = new HashMap<Integer, Integer>();
		this.chip.setOutHandler(this);

		this.subChipPanels = new ArrayList<ChipSimulationPanel>();
		this.source = this.chip.getSource();

		this.lanePaintEnabled = true;
		this.blockedEvent = null;

		this.tooltippedComponent = null;
		this.tooltippedLane = null;
		this.tooltipPoint = null;
	}

	/**
	 *
	 */
	public void configure() {
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setLayout(null);
		this.setOpaque(false);
		this.setDoubleBuffered(true);
		this.refresh();
	}

	/**
	 *
	 */
	private void refresh() {
		Dimension size = new Dimension(this.source.getWidth() * this.zoom,
				this.source.getHeight() * this.zoom);
		this.setPreferredSize(size);
		this.setSize(size);

		// Repaint this board
		this.sourceBuffer = CircuitSourcePainter.paint(this.source, this.zoom);

		// Refresh all subchips
		for (ChipSimulationPanel subChipPanel : this.subChipPanels) {
			Rectangle bounds = subChipPanel.chip.getBounds();
			subChipPanel
					.setLocation(bounds.x * this.zoom, bounds.y * this.zoom);
			subChipPanel.setZoom(this.zoom);
		}

		this.revalidate();
		this.repaint();
	}

	/**
	 * @param subChipPanel
	 */
	public void addSubChipPanel(ChipSimulationPanel subChipPanel) {
		this.subChipPanels.add(subChipPanel);
		this.add(subChipPanel);
		this.refresh();
	}

	/**
	 * @see org.bildskript.interpretation.parts.ChipOutHandler#write(int, int)
	 */
	@Override
	public void write(int pin, int value) {
		this.output.put(pin, value);
		this.repaint();
	}

	/**
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics go) {

		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		// Draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Draw chip
		if ((this.source != null) && (this.chip != null)) {
			// Draw source
			g.drawImage(this.sourceBuffer, 0, 0, null);

			// Let components paint additional stuff
			for (Component c : this.chip.getComponents()) {
				c.paint(g, this.zoom);
			}

			// Highlight invalid components
			g.setColor(ERROR_HIGHLIGHT_COLOR);
			for (Component c : this.chip.getComponents()) {
				if (c.getValidationErrors().size() > 0) {
					Rectangle bounds = c.layout.getBounds();
					g.drawRect(bounds.x * this.zoom, bounds.y * this.zoom,
							bounds.width * this.zoom, bounds.height * this.zoom);
				}
			}

			// Highlight lane
			if (this.lanePaintEnabled) {
				if (this.blockedEvent != null) {
					g.setColor(LANE_HIGHLIGHT_COLOR);
					List<Point> lanePath = this.blockedEvent.getLane()
							.getPath();
					for (Point p : lanePath) {
						if (p != null) {
							g.fillRect(p.x * this.zoom, p.y * this.zoom,
									this.zoom, this.zoom);
						}
					}
				}
			}

			// Draw tooltips
			if (this.tooltipPoint != null) {

				int textX = this.tooltipPoint.x;
				int textY = this.tooltipPoint.y;

				if (this.tooltippedLane != null) { // Draw lane tooltip
					String sourceName = (this.tooltippedLane.getSource() != null ? this.tooltippedLane
							.getSource().getName() : "nothing");
					String targetName = (this.tooltippedLane.getTarget() != null ? this.tooltippedLane
							.getTarget().getName() : "nothing");
					String txt = "from " + sourceName + ":"
							+ this.tooltippedLane.getSourcePin() + " to "
							+ targetName + ":"
							+ this.tooltippedLane.getTargetPin();
					g.setFont(Style.normalFont);
					g.setColor(Color.black);
					g.drawString(txt, textX - 1, textY - 1);
					g.drawString(txt, textX - 1, textY + 1);
					g.drawString(txt, textX + 1, textY + 1);
					g.drawString(txt, textX + 1, textY - 1);
					g.setColor(Color.white);
					g.drawString(txt, textX, textY);

				} else if (this.tooltippedComponent != null) { // Draw component
					// tooltip
					String txt = this.tooltippedComponent.makeTooltip();
					g.setFont(Style.normalFont);
					g.setColor(Color.black);
					g.drawString(txt, textX - 1, textY - 1);
					g.drawString(txt, textX - 1, textY + 1);
					g.drawString(txt, textX + 1, textY + 1);
					g.drawString(txt, textX + 1, textY - 1);
					g.setColor(Color.white);
					g.drawString(txt, textX, textY);

				}
			}
		}

		if (!this.isSubChip) {
			// Draw output
			int y = this.getHeight();
			g.setFont(Style.normalFont);
			g.setColor(OUTPUT_COLOR);

			List<String> hud = new ArrayList<String>();
			hud.add("Output values:");
			for (int pin : this.output.keySet()) {
				hud.add("#" + pin + ": " + this.output.get(pin));
			}

			for (String str : hud) {
				Rectangle2D strBounds = g.getFontMetrics().getStringBounds(str,
						g);
				g.drawString(str, 10, y - 10);
				y -= strBounds.getHeight();
			}
		}

		// Paint subboards
		super.paint(g);
	}

	/**
	 * @param point
	 */
	private void showTooltipAt(Point point) {

		if (this.chip == null) {
			return;
		}

		this.tooltipPoint = point;

		// Within a component?
		Component foundComponent = null;
		for (Component c : this.chip.getComponents()) {
			Rectangle bounds = c.layout.getBounds();

			Rectangle zoomed = new Rectangle(bounds.x * this.zoom, bounds.y
					* this.zoom, bounds.width * this.zoom, bounds.height
					* this.zoom);
			if (zoomed.contains(point)) {
				foundComponent = c;
				break;
			}
		}
		this.tooltippedComponent = foundComponent;

		// Within a lane?
		Lane foundLane = null;
		Point pointOnModel = new Point(point.x / this.zoom, point.y / this.zoom);

		for (Component c : this.chip.getComponents()) {
			for (int pin : c.outs.keySet()) {
				Lane lane = c.outs.get(pin);

				if (lane.getPath().contains(pointOnModel)) {
					foundLane = lane;
					break;
				}
			}
		}
		this.tooltippedLane = foundLane;

		this.repaint();
	}

	/**
	 * @param point
	 */
	private void pressAt(Point point) {

		if (this.chip == null) {
			return;
		}

		// Within a component?
		Component foundComponent = null;
		for (Component c : this.chip.getComponents()) {
			Rectangle bounds = c.layout.getBounds();

			Rectangle zoomed = new Rectangle(bounds.x * this.zoom, bounds.y
					* this.zoom, bounds.width * this.zoom, bounds.height
					* this.zoom);
			if (zoomed.contains(point)) {
				foundComponent = c;
				break;
			}
		}

		if (foundComponent != null) {
			foundComponent.accept(0xFF, 1);
		}
	}

	/**
	 *
	 */
	public void reset() {
		for (ChipSimulationPanel subChipPanel : this.subChipPanels) {
			subChipPanel.reset();
		}

		this.blockedEvent = null;
		this.repaint();
	}

	/**
	 * @param e
	 */
	public void dispatchEvent(Event e) {

		if (e.getChip() == this.chip) {
			this.showEvent(e);
		} else {
			for (ChipSimulationPanel subChip : this.subChipPanels) {
				subChip.dispatchEvent(e);
			}
		}
	}

	/**
	 * @param e
	 */
	public void showEvent(Event e) {
		this.blockedEvent = e;
		this.repaint();
	}

	/**
	 * @param lanesPainted
	 *            the lanesPainted to set.
	 */
	public void setLanePaintEnabled(boolean lanesPainted) {
		this.lanePaintEnabled = lanesPainted;
	}

	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
		this.refresh();
	}

	/**
	 * @param i
	 */
	public void zoom(int i) {
		int newZoom = this.zoom + i;
		if (newZoom < CircuitSourcePainter.MINIMUM_ZOOM) {
			newZoom = CircuitSourcePainter.MINIMUM_ZOOM;
		} else if (newZoom > CircuitSourcePainter.MAXIMUM_ZOOM) {
			newZoom = CircuitSourcePainter.MAXIMUM_ZOOM;
		}
		this.setZoom(newZoom);
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		this.showTooltipAt(e.getPoint());
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		this.pressAt(e.getPoint());
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

}
