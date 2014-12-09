package org.bildskript.interpretation;

import org.bildskript.interpretation.parts.Board;

/**
 *
 */
public class BoardRunnerThread extends Thread {

	private Board board;

	/**
	 *
	 */
	public BoardRunnerThread(Board board) {
		this.board = board;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		this.board.initialize();
	}
}
