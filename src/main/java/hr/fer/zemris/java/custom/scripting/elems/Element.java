package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents an element used for a parser. Element can be a number,
 * function, operator, string and a variable. All of these types of elements
 * have its own classes derived from this one.
 * 
 * @author Dinz
 *
 */
public class Element {
	/**
	 * Method that returns an element as a readable String.
	 * 
	 * @return Element as text.
	 */
	public String asText() {
		return "";
	}
}
