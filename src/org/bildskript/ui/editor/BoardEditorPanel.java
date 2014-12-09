package org.bildskript.ui.editor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;

import org.bildskript.interpretation.parts.Board;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.ui.BoardStudio;
import org.bildskript.ui.icons.IconLoader;

/**
 *
 */
public class BoardEditorPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private BoardStudio boardStudio;
	private CircuitSource source;

	private JPanel toolbarWrapper;
	private JToolBar toolbar;
	private JButton simulateButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;

	private CircuitEditorArea circuitEditorArea;
	private JScrollPane paintAreaScroller;

	/**
	 * @param boardStudio
	 */
	public BoardEditorPanel(BoardStudio boardStudio, Board board) {
		this.boardStudio = boardStudio;
		this.source = board.flattenToSource();

		this.initialize();
		this.build();
		this.configure();
	}

	/**
	 *
	 */
	public void initialize() {
		this.toolbarWrapper = new JPanel();
		this.toolbar = new JToolBar();
		this.zoomInButton = new JButton();
		this.zoomOutButton = new JButton();
		this.simulateButton = new JButton("Simulate");

		this.circuitEditorArea = new CircuitEditorArea(this.source);
		this.paintAreaScroller = new JScrollPane(this.circuitEditorArea);
		this.paintAreaScroller.setFocusable(false);
		this.circuitEditorArea.setScroller(this.paintAreaScroller);

		this.paintAreaScroller.setBackground(new Color(220, 220, 220));
	}

	/**
	 *
	 */
	public void build() {
		this.toolbar.add(this.zoomOutButton);
		this.toolbar.add(this.zoomInButton);
		this.toolbar.addSeparator();
		this.toolbar.add(this.simulateButton);

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		{
			this.toolbarWrapper.setLayout(new GridBagLayout());
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(0, 5, 5, 5);
			this.toolbarWrapper.add(this.toolbar, gbc);
		}
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(this.toolbarWrapper, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(this.paintAreaScroller, gbc);
	}

	/**
	 *
	 */
	public void configure() {

		this.simulateButton.setIcon(IconLoader.getInstance().getImage("run.png"));
		this.toolbar.setFloatable(false);
		this.toolbarWrapper.setBorder(new MatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));

		this.simulateButton.addActionListener(this);

		this.zoomOutButton.setIcon(IconLoader.getInstance().getImage("zoomout.png"));
		this.zoomInButton.setIcon(IconLoader.getInstance().getImage("zoomin.png"));

		this.zoomInButton.addActionListener(this);
		this.zoomOutButton.addActionListener(this);

		this.paintAreaScroller.setBorder(null);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == this.simulateButton) {
			this.boardStudio.openSimulator(this.source, true);
		} else if (source == this.zoomInButton) {
			this.circuitEditorArea.zoom(1);
		} else if (source == this.zoomOutButton) {
			this.circuitEditorArea.zoom(-1);
		}
	}

}
