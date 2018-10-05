package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents a string as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementString extends Element {
	/**
	 * Value of the string.
	 */
	private String value;

	/**
	 * Constructs a new ElementString.
	 * 
	 * @param value
	 *            Value of the string.
	 */
	public ElementString(String value) {
		this.value = value;
	}

	/**
	 * Returns the value of the ElementString.
	 * 
	 * @return Value of the ElementString.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the ElementString as a text.
	 */
	@Override
	public String asText() {
		return value;
	}
}
