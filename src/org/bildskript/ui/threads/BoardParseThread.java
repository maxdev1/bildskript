package org.bildskript.ui.threads;

import java.awt.Rectangle;
import java.util.List;

import org.bildskript.interpretation.parts.Board;
import org.bildskript.interpretation.parts.ValidationError;
import org.bildskript.parser.BoardParser;
import org.bildskript.parser.input.CircuitSource;
import org.bildskript.ui.threads.workerdialog.WorkerThread;

/**
 *
 */
public class BoardParseThread extends WorkerThread {

	private BoardThreadCallback boardThreadCallback;
	private CircuitSource source;
	private boolean backToEditorOnFail;

	/**
	 * @param boardThreadCallback
	 * @param source
	 */
	public BoardParseThread(BoardThreadCallback boardThreadCallback, CircuitSource source,
			boolean backToEditorOnFail) {
		this.source = source;
		this.boardThreadCallback = boardThreadCallback;
		this.backToEditorOnFail = backToEditorOnFail;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		this.callback.progressStarted(0);

		BoardParser parser = new BoardParser(this.source, new Rectangle(0, 0,
				this.source.getWidth(), this.source.getHeight()));
		Board board = parser.parse();

		List<ValidationError> errors = board.validate();
		this.boardThreadCallback.boardParsingDone(board, errors);

		this.callback.progressFinished();
	}
}
