package org.bildskript.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bildskript.interpretation.parts.Board;
import org.bildskript.interpretation.parts.ValidationError;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.parser.input.ImageCircuitSource;
import org.bildskript.ui.editor.BoardEditorPanel;
import org.bildskript.ui.icons.IconLoader;
import org.bildskript.ui.simulator.BoardSimulationPanel;
import org.bildskript.ui.threads.BoardImageLoadThread;
import org.bildskript.ui.threads.BoardImageSaveThread;
import org.bildskript.ui.threads.BoardParseThread;
import org.bildskript.ui.threads.BoardThreadCallback;
import org.bildskript.ui.threads.workerdialog.WorkerDialog;

/**
 *
 */
public class BoardStudio extends JFrame implements
		WindowListener,
		ActionListener,
		BoardThreadCallback {

	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem createEmptyItem;
	private JMenuItem saveAsItem;
	private JMenuItem openItem;
	private JMenuItem quitItem;

	private JPanel contentPanel;

	private Board currentBoard;

	/**
	 *
	 */
	public BoardStudio() {
		this.initialize();
		this.build();
		this.configure();
	}

	/**
	 *
	 */
	private void initialize() {
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		this.createEmptyItem = new JMenuItem("Create");
		this.openItem = new JMenuItem("Open...");
		this.saveAsItem = new JMenuItem("Save as...");
		this.quitItem = new JMenuItem("Quit");

		this.contentPanel = new JPanel();
	}

	/**
	 *
	 */
	private void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.createEmptyItem);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.openItem);
		this.fileMenu.add(this.saveAsItem);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.quitItem);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(7, 0, 0, 0);
		this.add(this.contentPanel, gbc);
	}

	/**
	 *
	 */
	private void configure() {
		this.setJMenuBar(this.menuBar);
		this.fileMenu.setMnemonic(KeyEvent.VK_F);
		this.createEmptyItem.setMnemonic(KeyEvent.VK_C);
		this.openItem.setMnemonic(KeyEvent.VK_O);
		this.saveAsItem.setMnemonic(KeyEvent.VK_S);
		this.quitItem.setMnemonic(KeyEvent.VK_Q);

		this.createEmptyItem.addActionListener(this);
		this.openItem.addActionListener(this);
		this.saveAsItem.addActionListener(this);
		this.quitItem.addActionListener(this);

		this.contentPanel.setLayout(new BorderLayout());

		this.addWindowListener(this);
		this.setPreferredSize(new Dimension(1000, 800));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setTitle("Board Studio");
		this.setIconImage(IconLoader.getInstance().getImage("icon.png").getImage());
	}

	/**
	 *
	 */
	private void showContent(JComponent comp) {
		this.contentPanel.removeAll();
		this.contentPanel.add(comp, BorderLayout.CENTER);
		this.contentPanel.revalidate();
	}

	/**
	 *
	 */
	private void showSaveBoardAsDialog() {
		if (this.currentBoard != null) {
			JFileChooser chooser = new JFileChooser();
			chooser.setSelectedFile(new File(BildSkriptPreferences.getLastSelectedFile()));

			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (file != null) {
					BildSkriptPreferences.setLastSelectedFile(file.getAbsolutePath());
					WorkerDialog.perform(
							new BoardImageSaveThread(this, this.currentBoard.flattenToSource(),
									file.getAbsolutePath()), this);
				}
			}
		}
	}

	/**
	 *
	 */
	private void showOpenDialog() {

		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(new File(BildSkriptPreferences.getLastSelectedFile()));

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file != null) {
				BildSkriptPreferences.setLastSelectedFile(file.getAbsolutePath());
				WorkerDialog.perform(new BoardImageLoadThread(this, file.getAbsolutePath(), true),
						this);
			}
		}
	}

	/**
	 *
	 */
	private void quit() {
		System.exit(0);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == this.createEmptyItem) {
			this.openSimulator(ImageCircuitSource.createBlank(), false);
		} else if (source == this.openItem) {
			this.showOpenDialog();
		} else if (source == this.saveAsItem) {
			this.showSaveBoardAsDialog();
		} else if (source == this.quitItem) {
			this.quit();
		}
	}

	/**
	 * @param source
	 * @param backToEditorOnFail
	 * @param currentBoard
	 */
	public void openSimulator(CircuitSource source, boolean backToEditorOnFail) {
		WorkerDialog.perform(new BoardParseThread(this, source, backToEditorOnFail), this);
	}

	/**
	 * @param currentBoard
	 */
	public void openEditor() {
		this.showContent(new BoardEditorPanel(this, this.currentBoard));
	}

	/**
	 * @see org.bildskript.ui.threads.BoardThreadCallback#boardParsingSuccessful(org.bildskript.interpretation.parts.Board)
	 */
	@Override
	public void boardParsingDone(Board board, List<ValidationError> errors) {
		this.showContent(new BoardSimulationPanel(this, board));
		this.currentBoard = board;

		if (errors.size() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("<html><p>Some components are not valid (see error highlightings).</p></html>");

			JOptionPane.showMessageDialog(this, builder.toString(), "Parsing problems",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @see org.bildskript.ui.threads.BoardThreadCallback#boardImageLoadingFailed(java.lang.String)
	 */
	@Override
	public void boardImageLoadingFailed(String path) {
		JOptionPane.showMessageDialog(this, "Could not open file '" + path + "'.", "Load error",
				JFileChooser.ERROR);
	}

	/**
	 * @see org.bildskript.ui.threads.BoardThreadCallback#boardImageLoadingSuccessful(org.bildskript.parser.input.ImageCircuitSource)
	 */
	@Override
	public void boardImageLoadingSuccessful(ImageCircuitSource source) {
		this.openSimulator(source, false);
	}

	/**
	 * @see org.bildskript.ui.threads.BoardThreadCallback#boardImageSavingFailed(java.lang.String)
	 */
	@Override
	public void boardImageSavingFailed(String path) {
		JOptionPane.showMessageDialog(this, "Could not save to file '" + path + "'.", "Save error",
				JFileChooser.ERROR);
	}

	/**
	 * @see org.bildskript.ui.threads.BoardThreadCallback#boardImageSavingSuccessful()
	 */
	@Override
	public void boardImageSavingSuccessful() {

	}

	/**
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.quit();
	}

	/**
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {}

}
