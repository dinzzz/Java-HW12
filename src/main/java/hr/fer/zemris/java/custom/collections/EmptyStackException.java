package hr.fer.zemris.java.custom.collections;

/**
 * Represents the exception that is being thrown when the program tries to do
 * actions with an empty stack.
 * 
 * @author Dinz
 *
 */
@SuppressWarnings("serial")
public class EmptyStackException extends RuntimeException {
	/**
	 * Handles the EmptyStackException
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Handles the EmptyStackException with an appropriate message.
	 * 
	 * @param message
	 *            Message that warns about the empty stack
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
