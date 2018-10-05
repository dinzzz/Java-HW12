package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Class that represents an exception when the empty stack is being accessed for
 * the purpose of getting its elements.
 * 
 * @author Dinz
 *
 */

public class EmptyStackException extends RuntimeException {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new EmptyStackException.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Constructs a new EmptyStackException with the appropriate message for the
	 * user.
	 * 
	 * @param message
	 *            Message for the user.
	 */
	public EmptyStackException(String message) {
		super(message);
	}

}
