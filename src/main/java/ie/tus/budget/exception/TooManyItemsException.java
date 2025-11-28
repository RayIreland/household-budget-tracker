package ie.tus.budget.exception;

public class TooManyItemsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TooManyItemsException(String message) {
        super(message);
    }

    public TooManyItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}
