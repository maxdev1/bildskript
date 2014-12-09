package org.bildskript.ui.threads;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bildskript.parser.input.CircuitSource;
import org.bildskript.ui.CircuitSourcePainter;
import org.bildskript.ui.threads.workerdialog.WorkerThread;

/**
 *
 */
public class BoardImageSaveThread extends WorkerThread {

	private CircuitSource source;
	private String path;
	private BoardThreadCallback boardThreadCallback;

	/**
	 * @param boardThreadCallback
	 * @param errorOnFail
	 */
	public BoardImageSaveThread(BoardThreadCallback boardThreadCallback, CircuitSource source,
			String path) {
		this.source = source;
		this.boardThreadCallback = boardThreadCallback;
		this.path = path;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.callback.progressStarted(0);

			BufferedImage image = CircuitSourcePainter.paint(this.source, 1);
			if (!this.path.endsWith(".png")) {
				this.path += ".png";
			}
			ImageIO.write(image, "png", new File(this.path));
			this.boardThreadCallback.boardImageSavingSuccessful();

			this.callback.progressFinished();
		} catch (IOException e) {
			this.boardThreadCallback.boardImageSavingFailed(this.path);
		}
	}
}
