package ie.tus.budget.exception;

import java.io.IOException;

public class ExportException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExportException(String message, IOException cause) {
        super(message, cause);
    }
}
