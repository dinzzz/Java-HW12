package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class that represents an operator as an element for a parser.
 * 
 * @author Dinz
 *
 */
public class ElementOperator extends Element {
	/**
	 * Symbol of an operator.
	 */
	private String symbol;

	/**
	 * Constructs a new ElementOperator.
	 * 
	 * @param symbol
	 *            Symbol of an operator.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Returns the symbol of an operator.
	 * 
	 * @return Symbol of an operator.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Returns an ElementOperator as a text.
	 */
	@Override
	public String asText() {
		return symbol;
	}
}
