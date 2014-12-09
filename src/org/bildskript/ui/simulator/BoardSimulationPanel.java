package org.bildskript.ui.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bildskript.interpretation.BoardInterpreter;
import org.bildskript.interpretation.BoardView;
import org.bildskript.interpretation.BoardViewEvent;
import org.bildskript.interpretation.events.Event;
import org.bildskript.interpretation.parts.Board;
import org.bildskript.ui.BoardStudio;
import org.bildskript.ui.icons.IconLoader;

/**
 *
 */
public class BoardSimulationPanel extends JPanel implements ActionListener,
		BoardView, ChangeListener {

	private static final long serialVersionUID = 1L;

	private BoardStudio boardStudio;
	private ChipSimulationPanel rootChipPanel;

	private JToolBar toolbar;
	private JPanel toolbarWrapper;
	private JButton editButton;
	private JButton stepButton;
	private JButton runButton;
	private JButton pauseButton;
	private JButton resetButton;

	private JButton zoomInButton;
	private JButton zoomOutButton;

	private BoardInterpreter interpreter;

	private JScrollPane rootChipPanelScroller;

	private JLabel delayInfoLabel;
	private JSlider delaySlider;
	private JLabel delayValueLabel;

	/**
	 * @param boardStudio
	 */
	public BoardSimulationPanel(BoardStudio boardStudio, Board board) {
		this.boardStudio = boardStudio;
		this.prepare(board);
		this.initialize();
		this.build();
		this.configure();
	}

	/**
	 * @param board
	 */
	private void prepare(Board board) {

		this.interpreter = new BoardInterpreter(this);
		this.interpreter.setBoard(board);

		ChipSimulationPanel rootChipPanel = new ChipSimulationPanel(
				board.getChip(), board.getSubBoards(), false);
		this.rootChipPanel = rootChipPanel;

		this.rootChipPanelScroller = new JScrollPane(rootChipPanel);
		this.rootChipPanelScroller.setBorder(new MatteBorder(1, 0, 0, 0,
				new Color(180, 180, 180)));

	}

	/**
	 *
	 */
	public void initialize() {

		this.toolbarWrapper = new JPanel();
		this.toolbar = new JToolBar();
		this.editButton = new JButton("Edit");
		this.stepButton = new JButton("Step");
		this.runButton = new JButton("Run");
		this.pauseButton = new JButton("Pause");
		this.resetButton = new JButton("Reset");

		this.zoomInButton = new JButton();
		this.zoomOutButton = new JButton();

		this.delayInfoLabel = new JLabel("Delay:");
		this.delaySlider = new JSlider();
		this.delayValueLabel = new JLabel("-");
		this.delaySlider.setMaximum(1000);
		this.delaySlider.setMinimum(0);
		this.delaySlider.addChangeListener(this);
		this.delayHasChanged();
	}

	/**
	 *
	 */
	private void delayHasChanged() {
		this.delayValueLabel.setText(this.delaySlider.getValue() + "ms");
	}

	/**
	 *
	 */
	public void build() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		// Build toolbar
		this.toolbar.add(this.zoomOutButton);
		this.toolbar.add(this.zoomInButton);
		this.toolbar.addSeparator();
		this.toolbar.add(this.editButton);
		this.toolbar.add(this.stepButton);
		this.toolbar.add(this.runButton);
		this.toolbar.add(this.pauseButton);
		this.toolbar.add(this.resetButton);
		this.toolbar.addSeparator();

		JPanel delayPanel = new JPanel();
		{
			delayPanel.setLayout(new GridBagLayout());
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(0, 5, 0, 3);
			delayPanel.add(this.delayInfoLabel, gbc);

			delayPanel.setLayout(new GridBagLayout());
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(0, 3, 0, 3);
			this.delaySlider.setMinimumSize(new Dimension(100, 0));
			this.delaySlider.setMaximumSize(new Dimension(100, 100));
			delayPanel.add(this.delaySlider, gbc);

			delayPanel.setLayout(new GridBagLayout());
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.insets = new Insets(0, 3, 0, 5);
			this.delayValueLabel.setMinimumSize(new Dimension(70, 0));
			this.delayValueLabel.setPreferredSize(new Dimension(70, 20));
			this.delayValueLabel.setMaximumSize(new Dimension(70, 100));
			delayPanel.add(this.delayValueLabel, gbc);
		}
		this.toolbar.add(delayPanel);

		// Build content
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
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(this.toolbarWrapper, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(this.rootChipPanelScroller, gbc);
	}

	/**
	 *
	 */
	public void configure() {

		this.toolbar.setFloatable(false);
		this.toolbarWrapper.setBorder(new MatteBorder(0, 0, 1, 0, new Color(
				180, 180, 180)));
		this.rootChipPanelScroller.setBorder(null);

		this.pauseButton
				.setIcon(IconLoader.getInstance().getImage("pause.png"));
		this.runButton.setIcon(IconLoader.getInstance().getImage("run.png"));
		this.stepButton.setIcon(IconLoader.getInstance().getImage("step.png"));
		this.editButton.setIcon(IconLoader.getInstance().getImage("edit.png"));
		this.resetButton
				.setIcon(IconLoader.getInstance().getImage("reset.png"));
		this.zoomOutButton.setIcon(IconLoader.getInstance().getImage(
				"zoomout.png"));
		this.zoomInButton.setIcon(IconLoader.getInstance().getImage(
				"zoomin.png"));

		this.editButton.addActionListener(this);
		this.editButton.setFocusable(false);
		this.editButton.setEnabled(false);

		this.stepButton.addActionListener(this);
		this.stepButton.setFocusable(false);
		this.stepButton.setEnabled(false);

		this.runButton.addActionListener(this);
		this.runButton.setFocusable(false);
		this.runButton.setEnabled(false);

		this.pauseButton.addActionListener(this);
		this.pauseButton.setFocusable(false);
		this.pauseButton.setEnabled(false);

		this.resetButton.addActionListener(this);
		this.resetButton.setFocusable(false);
		this.resetButton.setEnabled(false);

		this.zoomInButton.addActionListener(this);
		this.zoomInButton.setFocusable(false);
		this.zoomOutButton.addActionListener(this);
		this.zoomOutButton.setFocusable(false);

		this.editButton.setEnabled(true);
		this.runButton.setEnabled(true);
		this.stepButton.setEnabled(true);
		this.pauseButton.setEnabled(false);
		this.resetButton.setEnabled(true);
	}

	/**
	 * @param i
	 */
	private void zoom(int i) {
		if (this.rootChipPanel != null) {
			this.rootChipPanel.zoom(i);
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == this.runButton) {
			this.interpreter.run();
		} else if (source == this.pauseButton) {
			this.interpreter.pause();
		} else if (source == this.stepButton) {
			this.interpreter.step();
		} else if (source == this.resetButton) {
			this.interpreter.reset();
		} else if (source == this.editButton) {
			this.boardStudio.openEditor();
		} else if (source == this.zoomInButton) {
			this.zoom(1);
		} else if (source == this.zoomOutButton) {
			this.zoom(-1);
		}
	}

	/**
	 * @see org.bildskript.interpretation.BoardView#publishState(org.bildskript.interpretation.BoardViewEvent)
	 */
	@Override
	public void publishState(BoardViewEvent state) {

		if (state == BoardViewEvent.PAUSED) {
			this.editButton.setEnabled(true);
			this.runButton.setEnabled(true);
			this.stepButton.setEnabled(true);
			this.pauseButton.setEnabled(false);
			this.resetButton.setEnabled(true);

		} else if (state == BoardViewEvent.RESET_STARTING) {
			this.editButton.setEnabled(false);
			this.runButton.setEnabled(false);
			this.stepButton.setEnabled(false);
			this.pauseButton.setEnabled(false);
			this.resetButton.setEnabled(false);
			this.rootChipPanel.reset();

		} else if (state == BoardViewEvent.RESET_FINISHED) {
			this.editButton.setEnabled(true);
			this.runButton.setEnabled(true);
			this.stepButton.setEnabled(true);
			this.pauseButton.setEnabled(false);
			this.resetButton.setEnabled(true);

		} else if (state == BoardViewEvent.RUNNING) {
			this.editButton.setEnabled(false);
			this.runButton.setEnabled(false);
			this.stepButton.setEnabled(false);
			this.pauseButton.setEnabled(true);
			this.resetButton.setEnabled(false);

		} else if (state == BoardViewEvent.STEP_STARTING) {
			this.editButton.setEnabled(false);
			this.runButton.setEnabled(false);
			this.stepButton.setEnabled(false);
			this.pauseButton.setEnabled(false);
			this.resetButton.setEnabled(true);

		} else if (state == BoardViewEvent.STEP_FINISHED) {
			this.editButton.setEnabled(false);
			this.runButton.setEnabled(true);
			this.stepButton.setEnabled(true);
			this.pauseButton.setEnabled(false);
			this.resetButton.setEnabled(true);

		}

	}

	/**
	 * @see org.bildskript.interpretation.BoardView#showEvent(org.bildskript.interpretation.events.Event)
	 */
	@Override
	public void showEvent(Event e) {
		this.rootChipPanel.dispatchEvent(e);
	}

	/**
	 * @see org.bildskript.interpretation.BoardView#getStepPause()
	 */
	@Override
	public int getStepPause() {
		return this.delaySlider.getValue();
	}

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();

		if (source == this.delaySlider) {
			this.delayHasChanged();
		}
	}
}
