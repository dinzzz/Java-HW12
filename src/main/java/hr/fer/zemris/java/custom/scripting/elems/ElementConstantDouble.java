package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents a real number as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementConstantDouble extends Element {
	/**
	 * Value of the element.
	 */
	private double value;

	/**
	 * Constructs an instance of an ElementConstantDouble.
	 * 
	 * @param value
	 *            Value of the element.
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * Returns the value of the element.
	 * 
	 * @return Value of the element.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Method that returns the ElementConstantDouble as a text.
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
