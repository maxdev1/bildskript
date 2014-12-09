package org.bildskript.ui.threads.workerdialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.bildskript.ui.icons.IconLoader;

/**
 *
 */
public class WorkerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel infoLabel;
	private JProgressBar progressBar;

	private WorkerThread worker;

	/**
	 *
	 */
	private WorkerDialog(WorkerThread worker) {
		this.worker = worker;
		this.worker.setCallback(this);
		this.initialize();
		this.build();
		this.configure();
		this.worker.start();
	}

	/**
	 *
	 */
	public static void perform(WorkerThread worker, Component relativeTo) {
		WorkerDialog dialog = new WorkerDialog(worker);
		dialog.setLocationRelativeTo(relativeTo);
		dialog.setVisible(true);
	}

	/**
	 *
	 */
	public void initialize() {
		this.infoLabel = new JLabel("Loading...");
		this.progressBar = new JProgressBar();
	}

	/**
	 *
	 */
	public void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 10, 5, 10);
		this.add(this.infoLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(5, 10, 10, 10);
		this.add(this.progressBar, gbc);
	}

	/**
	 *
	 */
	public void configure() {
		this.setTitle("Starting...");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setIconImage(IconLoader.getInstance().getImage("icon.png").getImage());
		this.setPreferredSize(new Dimension(300, 100));
		this.setResizable(false);
		this.pack();
	}

	/**
	 *
	 */
	public void progressStarted(int maximum) {
		this.progressBar.setMaximum(maximum);
		if (maximum == 0) {
			this.progressBar.setIndeterminate(true);
		}
	}

	/**
	 * @param i
	 * @param text
	 */
	public void progressContinued(int i, String text) {
		this.progressBar.setValue(this.progressBar.getValue() + i);
		this.infoLabel.setText(text);
	}

	/**
	 *
	 */
	public void progressFinished() {
		this.dispose();
	}

}
