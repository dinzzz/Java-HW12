package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents a variable as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementVariable extends Element {
	/**
	 * Name of the variable.
	 */
	private String name;

	/**
	 * Constructs a new ElementVariable.
	 * 
	 * @param name
	 *            Name of the variable element.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the variable element.
	 * 
	 * @return Name of the ElementVariable
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the ElementVariable as a text.
	 */
	@Override
	public String asText() {
		return name;
	}
}
