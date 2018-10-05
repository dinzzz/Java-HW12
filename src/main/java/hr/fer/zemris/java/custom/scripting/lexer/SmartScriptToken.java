package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Class that represents a token for a parser. Each token is derived from the
 * input text with its token type and value.
 * 
 * @author Dinz
 *
 */
public class SmartScriptToken {
	/**
	 * Type of the token.
	 */
	private SmartScriptTokenType type;
	/**
	 * Value of the token.
	 */
	private Object value;

	/**
	 * Constructs a new SmartScriptToken.
	 * 
	 * @param type
	 *            Type of the token.
	 * @param value
	 *            Value of the token.
	 */
	public SmartScriptToken(SmartScriptTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the value of the token.
	 * 
	 * @return Value of the token.
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Returns the type of the token.
	 * 
	 * @return Type of the token.
	 */
	public SmartScriptTokenType getType() {
		return this.type;
	}
}
