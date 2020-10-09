package org.bildskript.ui.threads;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bildskript.parser.input.ImageCircuitSource;
import org.bildskript.ui.threads.workerdialog.WorkerThread;

/**
 *
 */
public class BoardImageLoadThread extends WorkerThread {

	private String path;
	private boolean errorOnFail;
	private BoardThreadCallback boardThreadCallback;

	/**
	 * @param boardThreadCallback
	 * @param errorOnFail
	 */
	public BoardImageLoadThread(BoardThreadCallback boardThreadCallback, String path,
			boolean errorOnFail) {
		this.boardThreadCallback = boardThreadCallback;
		this.path = path;
		this.errorOnFail = errorOnFail;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.callback.progressStarted(0);

			BufferedImage image = ImageIO.read(new File(this.path));
			ImageCircuitSource source = new ImageCircuitSource();
			source.load(image);
			this.boardThreadCallback.boardImageLoadingSuccessful(source);

			this.callback.progressFinished();
		} catch (IOException e) {
			if (this.errorOnFail) {
				this.boardThreadCallback.boardImageLoadingFailed(this.path);
			}
		}
	}
}
