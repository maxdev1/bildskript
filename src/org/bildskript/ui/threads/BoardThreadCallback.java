package org.bildskript.ui.threads;

import java.util.List;

import org.bildskript.interpretation.parts.Board;
import org.bildskript.interpretation.parts.ValidationError;
import org.bildskript.parser.input.ImageCircuitSource;

/**
 *
 */
public interface BoardThreadCallback {

	/**
	 * @param board
	 */
	void boardParsingDone(Board board, List<ValidationError> errors);

	/**
	 * @param source
	 */
	void boardImageLoadingSuccessful(ImageCircuitSource source);

	/**
	 * @param path
	 */
	void boardImageLoadingFailed(String path);

	/**
	 * @param path
	 */
	void boardImageSavingFailed(String path);

	/**
	 *
	 */
	void boardImageSavingSuccessful();
}
