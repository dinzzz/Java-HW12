package hr.fer.zemris.java.custom.scripting.elems;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Class that represents an integer number as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * Value of the element.
	 */
	int value;

	/**
	 * Constructs an instance of ElementConstantInteger.
	 * 
	 * @param value
	 *            Value of the element.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/**
	 * Returns the value of the integer element.
	 * 
	 * @return Value of the integer element.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns the integer element as a text.
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
