package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents a function as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementFunction extends Element {
	/**
	 * Name of the function.
	 */
	private String name;

	/**
	 * Constructs a new ElementFunction.
	 * 
	 * @param name
	 *            Name of the function
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the function.
	 * 
	 * @return Name of the function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the ElementFunction as a text.
	 */
	@Override
	public String asText() {
		return name;
	}
}
