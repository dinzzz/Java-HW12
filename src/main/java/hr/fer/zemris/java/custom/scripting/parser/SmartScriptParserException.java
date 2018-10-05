package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class that represents an exception that is being thrown if the expression
 * like ForLoopNode is in invalid format.
 * 
 * @author Dinz
 *
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new SmartScriptParserException.
	 */
	public SmartScriptParserException() {
		super();
	}

	/**
	 * Constructs a new SmartScriptParserException with the appropriate message.
	 * 
	 * @param message
	 *            Message that describes the problem.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
}
