package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class that represents an exception which is being thrown when the lexer finds
 * a mistake in a given text.
 * 
 * @author Dinz
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor that constructs a new SmartScriptLexerException.
	 */
	public SmartScriptLexerException() {
		super();
	}

	/**
	 * Constructs a new SmartScriptLexerException with an appropriate message.
	 * 
	 * @param message
	 *            Message of the exception.
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}
}
