package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Class that represents a general expression construction as a Node which is
 * used in a parser. Elements inside an EchoNode can be any type of an element.
 * 
 * @author Dinz
 *
 */
public class EchoNode extends Node {
	/**
	 * Elements of an EchoNode.
	 */
	Element[] elements;

	/**
	 * Constructs a new EchoNode from an array of elements.
	 * 
	 * @param elements
	 *            Array of elements.
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * Returns the array of elements in EchoNode.
	 * 
	 * @return Array of elements.
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
