package org.bildskript.ui.editor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bildskript.interpretation.parts.components.Component;
import org.bildskript.parser.ComponentFactory;
import org.bildskript.parser.LaneWalker;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.PixelType;
import org.bildskript.ui.CircuitSourcePainter;
import org.bildskript.ui.editor.pens.Pen;
import org.bildskript.ui.editor.pens.SimplePixelPen;

/**
 *
 */
public class CircuitEditorArea extends JPanel implements
		KeyListener,
		FocusListener,
		MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private static final Color COLOR_GRID_LINES = new Color(120, 120, 120, 50);
	private static final Color DETECTED_COLOR = new Color(150, 255, 220);
	private static final Color CURSOR_COLOR = new Color(55, 155, 255);
	private static final Color MARKING_COLOR = new Color(55, 155, 255);

	private static final int MIN_ZOOM_FOR_GRID = 5;

	private CircuitSource source;
	private int zoom = 12;
	private Point markerModelPosition;
	private Point cursorModelPosition;

	private Pen componentPen;
	private Pen lanePen;
	private Pen laneHopPen;
	private Pen spacePen;
	private Pen pressedPen;

	private BufferedImage buffer;
	private JScrollPane scroller;

	private List<Component> highlightingComponents;
	private Rectangle syntaxUncheckedAreas;

	private CircuitSource copiedArea;

	private Point pressPoint;

	/**
	 *
	 */
	public CircuitEditorArea(CircuitSource source) {
		this.source = source;
		this.cursorModelPosition = new Point();
		this.markerModelPosition = null;

		this.highlightingComponents = new ArrayList<Component>();
		this.syntaxUncheckedAreas = new Rectangle(0, 0, source.getWidth(), source.getHeight());

		this.initialize();
		this.configure();
		this.refresh();
	}

	/**
	 * @param scroller the scroller to set
	 */
	public void setScroller(JScrollPane scroller) {
		this.scroller = scroller;
	}

	/**
	 *
	 */
	private void initialize() {

		this.componentPen = new SimplePixelPen(PixelType.COMPONENT);
		this.lanePen = new SimplePixelPen(PixelType.LANE);
		this.laneHopPen = new SimplePixelPen(PixelType.LANE_HOP);
		this.spacePen = new SimplePixelPen(PixelType.SPACE);

	}

	/**
	 *
	 */
	public void configure() {
		this.setFocusable(true);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);
	}

	/**
	 *
	 */
	public void hardRefresh() {

		this.setPreferredSize(new Dimension(this.source.getWidth() * this.zoom, this.source
				.getHeight() * this.zoom));

		// Detect components
		this.deleteComponentsWithin(this.syntaxUncheckedAreas);
		for (int y = this.syntaxUncheckedAreas.y; y < (this.syntaxUncheckedAreas.y + this.syntaxUncheckedAreas.height); y++) {
			for (int x = this.syntaxUncheckedAreas.x; x < (this.syntaxUncheckedAreas.x + this.syntaxUncheckedAreas.width); x++) {

				Component c = ComponentFactory.find(this.source, x, y);
				if (c != null) {
					this.highlightingComponents.add(c);
				}
			}
		}
		this.syntaxUncheckedAreas.setBounds(0, 0, 0, 0);

		this.revalidate();
	}

	/**
	 *
	 */
	private void refresh() {

		HardRefreshTrigger.triggerLater(this);

		// Paint the circuit
		this.buffer = CircuitSourcePainter.paint(this.source, this.zoom);
		this.repaint();
	}

	/**
	 *
	 */
	public void markSyntaxUnchecked(Rectangle rect) {
		this.syntaxUncheckedAreas.add(rect);
	}

	/**
	 * @param newCompBounds
	 */
	private void deleteComponentsWithin(Rectangle newCompBounds) {
		List<Component> deleteds = new ArrayList<Component>();
		Rectangle pinlessBounds = new Rectangle(newCompBounds.x + 1, newCompBounds.y + 1,
				newCompBounds.width - 2, newCompBounds.height - 2);

		for (int oy = pinlessBounds.y; oy < (pinlessBounds.y + pinlessBounds.height); oy++) {
			for (int ox = pinlessBounds.x; ox < (pinlessBounds.x + pinlessBounds.width); ox++) {
				for (Component component : this.highlightingComponents) {
					if (component.layout.getBounds().getLocation().equals(new Point(ox, oy))) {
						deleteds.add(component);
					}
				}
			}
		}

		this.highlightingComponents.removeAll(deleteds);
	}

	/**
	 * @param i
	 */
	public void zoom(int i) {
		this.zoom += i;
		if (this.zoom < CircuitSourcePainter.MINIMUM_ZOOM) {
			this.zoom = CircuitSourcePainter.MINIMUM_ZOOM;
		} else if (this.zoom > CircuitSourcePainter.MAXIMUM_ZOOM) {
			this.zoom = CircuitSourcePainter.MAXIMUM_ZOOM;
		}
		this.refresh();
	}

	/**
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics go) {

		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		// Background
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Image
		if ((this.source != null) && (this.buffer != null)) {
			g.drawImage(this.buffer, 0, 0, null);
		}

		// Component highlighting
		g.setColor(DETECTED_COLOR);
		for (Component component : this.highlightingComponents) {
			Rectangle bounds = component.layout.getBounds();

			for (int py = 0; py < bounds.height; py++) {
				for (int px = 0; px < bounds.width; px++) {
					if (this.source.get(bounds.x + px, bounds.y + py) == PixelType.SPACE) {
						g.fillRect((bounds.x + px) * this.zoom, (bounds.y + py) * this.zoom,
								this.zoom, this.zoom);
					}
				}
			}
		}

		Point screenCursor = this.modelToView(this.cursorModelPosition);

		// Ghost for copied
		if (this.copiedArea != null) {
			BufferedImage sub = CircuitSourcePainter.paint(this.copiedArea, this.zoom);

			Graphics2D subG = (Graphics2D) g.create();
			subG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			subG.drawImage(sub, screenCursor.x, screenCursor.y, null);
			subG.dispose();
		}

		// Grid
		if (this.zoom > MIN_ZOOM_FOR_GRID) {
			int gridSkip = this.zoom;

			for (int y = gridSkip; y < this.getHeight(); y += gridSkip) {
				g.setColor(COLOR_GRID_LINES);
				g.drawLine(0, y - 1, this.getWidth(), y - 1);
			}

			for (int x = gridSkip; x < this.getWidth(); x += gridSkip) {
				g.setColor(COLOR_GRID_LINES);
				g.drawLine(x - 1, 0, x - 1, this.getHeight());
			}
		}

		// Marking
		if (this.markerModelPosition != null) {
			g.setColor(MARKING_COLOR);
			Rectangle marked = this.getSelectedArea();
			g.drawRect(marked.x * this.zoom, marked.y * this.zoom, marked.width * this.zoom,
					marked.height * this.zoom);
		}

		// Cursor
		if (this.hasFocus()) {
			g.setColor(CURSOR_COLOR);
			g.drawOval(screenCursor.x - 1, screenCursor.y - 1, this.zoom + 1, this.zoom + 1);
		}

	}

	/**
	 * @return
	 */
	private Rectangle getSelectedArea() {

		if (this.markerModelPosition == null) {
			return new Rectangle(this.cursorModelPosition.x, this.cursorModelPosition.y, 1, 1);
		}

		int left = (this.markerModelPosition.x < this.cursorModelPosition.x)
				? this.markerModelPosition.x
				: this.cursorModelPosition.x;
		int right = (this.markerModelPosition.x > this.cursorModelPosition.x)
				? this.markerModelPosition.x
				: this.cursorModelPosition.x;
		int top = (this.markerModelPosition.y < this.cursorModelPosition.y)
				? this.markerModelPosition.y
				: this.cursorModelPosition.y;
		int bottom = (this.markerModelPosition.y > this.cursorModelPosition.y)
				? this.markerModelPosition.y
				: this.cursorModelPosition.y;
		return new Rectangle(left, top, (right - left) + 1, (bottom - top) + 1);
	}

	/**
	 * @param point
	 * @return
	 */
	public Point viewToModel(Point point) {
		return new Point(point.x / this.zoom, point.y / this.zoom);
	}

	/**
	 * @param point
	 * @return
	 */
	public Point modelToView(Point point) {
		return new Point(point.x * this.zoom, point.y * this.zoom);
	}

	/**
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		char chr = e.getKeyChar();

		if (chr == '1') {
			this.pressedPen = this.componentPen;
			this.applyPen(this.componentPen);

		} else if (chr == '2') {
			this.pressedPen = this.lanePen;
			this.applyPen(this.lanePen);

		} else if (chr == '3') {
			this.pressedPen = this.laneHopPen;
			this.applyPen(this.laneHopPen);

		} else if (chr == '4') {
			this.pressedPen = this.spacePen;
			this.applyPen(this.spacePen);

		} else if (chr == 'd') { // Delete
			this.delete();

		} else if (chr == 's') { // Select
			this.select();

		} else if (chr == 'x') { // Cut
			this.cut();

		} else if (chr == 'c') { // Copy
			this.copy();

		} else if (chr == 'v') { // Paste
			this.paste();

		} else if (key == KeyEvent.VK_LEFT) {
			this.moveCursor(-1, 0);

		} else if (key == KeyEvent.VK_RIGHT) {
			this.moveCursor(1, 0);

		} else if (key == KeyEvent.VK_UP) {
			this.moveCursor(0, -1);

		} else if (key == KeyEvent.VK_DOWN) {
			this.moveCursor(0, 1);

		}

	}

	/**
	 *
	 */
	private void select() {
		if (this.copiedArea != null) {
			this.copiedArea = null;
		} else {
			if (this.markerModelPosition == null) {
				this.markerModelPosition = new Point(this.cursorModelPosition.x,
						this.cursorModelPosition.y);
			} else {
				this.markerModelPosition = null;
			}
		}
		this.repaint();
	}

	/**
	 *
	 */
	private void cut() {
		if (this.markerModelPosition != null) {
			Rectangle selectedRect = this.getSelectedArea();
			CircuitSource clone = this.source.copy(selectedRect);

			for (int y = 0; y < selectedRect.height; y++) {
				for (int x = 0; x < selectedRect.width; x++) {
					this.source.set(selectedRect.x + x, selectedRect.y + y, PixelType.SPACE);
				}
			}

			this.markSyntaxUnchecked(selectedRect);

			this.copiedArea = clone;
			this.markerModelPosition = null;
			this.refresh();
		}
	}

	/**
	 *
	 */
	private void copy() {
		if (this.markerModelPosition != null) {
			Rectangle selectedRect = this.getSelectedArea();
			CircuitSource clone = this.source.copy(selectedRect);
			this.copiedArea = clone;
			this.markerModelPosition = null;
			this.repaint();
		}
	}

	/**
	 *
	 */
	private void paste() {
		if (this.copiedArea != null) {
			this.source.insert(this.cursorModelPosition, this.copiedArea);
			this.markSyntaxUnchecked(new Rectangle(this.cursorModelPosition.x,
					this.cursorModelPosition.y, this.copiedArea.getWidth(), this.copiedArea
							.getHeight()));
			this.refresh();
		}
	}

	/**
	 *
	 */
	private void delete() {

		// Check if area is marked
		if (this.markerModelPosition != null) {
			Rectangle selectedRect = this.getSelectedArea();

			for (int y = 0; y < selectedRect.height; y++) {
				for (int x = 0; x < selectedRect.width; x++) {
					this.source.set(selectedRect.x + x, selectedRect.y + y, PixelType.SPACE);
				}
			}

			this.markSyntaxUnchecked(selectedRect);

			this.refresh();
			this.markerModelPosition = null;

		} else {

			// Check if within component
			Component withinComponent = null;
			for (Component component : this.highlightingComponents) {
				if (component.layout.getBounds().contains(this.cursorModelPosition)) {
					withinComponent = component;
					break;
				}
			}

			// Delete component
			if (withinComponent != null) {

				Rectangle componentBounds = withinComponent.layout.getBounds();

				// Place spaces
				for (int y = 0; y < componentBounds.height; y++) {
					for (int x = 0; x < componentBounds.width; x++) {
						this.spacePen.paint(this.source, componentBounds.x + x, componentBounds.y
								+ y);
					}
				}

				// Mark unchecked
				this.markSyntaxUnchecked(componentBounds);
				this.refresh();

			} else {
				PixelType currentPixel = this.source.get(this.cursorModelPosition.x,
						this.cursorModelPosition.y);

				// Delete the lane
				if ((currentPixel == PixelType.LANE) || (currentPixel == PixelType.LANE_HOP)) {
					List<Point> lanePoints = LaneWalker.findLane(this.source,
							this.cursorModelPosition.x, this.cursorModelPosition.y);

					if (lanePoints.size() > 1) {
						for (Point point : lanePoints) {
							this.spacePen.paint(this.source, point.x, point.y);
						}
						this.refresh();
					}
				}
			}
		}
	}

	/**
	 * @param p
	 */
	private void applyPen(Pen p) {

		Rectangle selectedRect = this.getSelectedArea();

		for (int y = selectedRect.y; y < (selectedRect.y + selectedRect.height); y++) {
			for (int x = selectedRect.x; x < (selectedRect.x + selectedRect.width); x++) {
				p.paint(this.source, x, y);
			}
		}
		this.markSyntaxUnchecked(new Rectangle(selectedRect.x - 10, selectedRect.y - 10,
				selectedRect.x + selectedRect.width + 10, selectedRect.y + selectedRect.height + 10));
		this.refresh();
	}

	/**
	 *
	 */
	private void moveCursor(int x, int y) {
		this.setCursorPosition(new Point(this.cursorModelPosition.x + x, this.cursorModelPosition.y
				+ y));
	}

	/**
	 * @param p
	 */
	private void setCursorPosition(Point p) {

		this.cursorModelPosition = p;

		if (this.cursorModelPosition.x < 0) {
			this.cursorModelPosition.x = 0;
		}
		if (this.cursorModelPosition.y < 0) {
			this.cursorModelPosition.y = 0;
		}

		if (this.pressedPen != null) {
			this.applyPen(this.pressedPen);
		}

		if (this.scroller != null) {
			Point cursorOnScreen = this.modelToView(this.cursorModelPosition);
			this.scroller.getViewport().scrollRectToVisible(
					new Rectangle(cursorOnScreen.x, cursorOnScreen.y, 10, 10));
		}

		this.repaint();
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {

		char key = e.getKeyChar();

		if ((key == '1') || (key == '2') || (key == '3') || (key == '4')) {
			this.pressedPen = null;
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		this.repaint();
	}

	/**
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		this.repaint();
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		this.grabFocus();
		this.markerModelPosition = null;
		this.pressPoint = e.getPoint();
		this.setCursorPosition(this.viewToModel(e.getPoint()));
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		this.pressPoint = null;
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		this.markerModelPosition = this.viewToModel(this.pressPoint);
		this.setCursorPosition(this.viewToModel(e.getPoint()));
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {}

}
