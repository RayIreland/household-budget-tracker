package ie.tus.budget.exception;

public class NotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
