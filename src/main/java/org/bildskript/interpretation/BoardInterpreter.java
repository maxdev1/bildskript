package org.bildskript.interpretation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bildskript.interpretation.events.Event;
import org.bildskript.interpretation.events.EventAcceptor;
import org.bildskript.interpretation.parts.Board;

/**
 *
 */
public class BoardInterpreter implements EventAcceptor {

	private Board board;
	private BoardView view;

	private Lock acceptLock;
	private int eventsWaitingForStep;
	private boolean stopAll;

	private boolean paused;
	private boolean boardInitialized;

	private BoardRunnerThread boardRunnerThread;

	/**
	 *
	 */
	public BoardInterpreter(BoardView view) {

		this.view = view;

		this.paused = true;
		this.stopAll = false;
		this.acceptLock = new ReentrantLock();
		this.eventsWaitingForStep = 0;
	}

	/**
	 *
	 */
	public void setBoard(Board board) {
		this.reset();

		this.board = board;
		board.setEventAcceptor(this);
	}

	/**
	 *
	 */
	private void letEventStep() {
		synchronized (this) {
			this.notify();
		}
	}

	/**
	 *
	 */
	private void waitForEventStep() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 *
	 */
	public void step() {
		if (!this.boardInitialized) {
			this.init();
		} else {
			this.view.publishState(BoardViewEvent.STEP_STARTING);
			this.letEventStep();
		}
	}

	/**
	 *
	 */
	public void run() {
		if (!this.boardInitialized) {
			this.init();
		}

		this.view.publishState(BoardViewEvent.RUNNING);
		this.paused = false;
		this.letEventStep();
	}

	/**
	 *
	 */
	private void init() {
		if (this.board != null) {
			this.boardRunnerThread = new BoardRunnerThread(this.board);
			this.boardRunnerThread.start();
			this.boardInitialized = true;
		}
	}

	/**
	 *
	 */
	public void pause() {
		this.view.publishState(BoardViewEvent.PAUSED);
		this.paused = true;
	}

	/**
	 *
	 */
	public void reset() {

		if (this.board != null) {
			this.pause();

			this.view.publishState(BoardViewEvent.RESET_STARTING);
			this.stopAll = true;

			while (this.eventsWaitingForStep > 0) {
				this.letEventStep();
			}

			this.board.reset();
			this.stopAll = false;
			this.boardInitialized = false;

			this.view.publishState(BoardViewEvent.RESET_FINISHED);
		}
	}

	/**
	 * @see org.bildskript.interpretation.events.EventAcceptor#acceptEvent(org.bildskript.interpretation.events.Event)
	 */
	@Override
	public boolean acceptEvent(Event e) {

		this.acceptLock.lock();
		this.eventsWaitingForStep++;
		this.acceptLock.unlock();

		this.acceptLock.lock();

		if (!this.stopAll) {
			if (this.paused) {
				this.view.publishState(BoardViewEvent.PAUSED);
				this.waitForEventStep();

			} else {
				int sleep = this.view.getStepPause();
				if (sleep > 0) {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}

			if (!this.stopAll) {
				this.view.showEvent(e);
			}

		}

		this.eventsWaitingForStep--;
		this.acceptLock.unlock();

		return !this.stopAll;
	}
}
