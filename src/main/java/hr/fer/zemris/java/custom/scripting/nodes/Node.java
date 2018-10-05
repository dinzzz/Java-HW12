package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Class that represents a general node which is used by a parser. It uses an
 * ArrayIndexedCollection as a tool to store nodes that construct it, called
 * childNodes. This class is a base for DocumentNode, EchoNode, ForLoopNode and
 * TextNode.
 * 
 * @author Dinz
 *
 */
public class Node {
	/**
	 * Collection of child nodes.
	 */
	private ArrayIndexedCollection childNodes;

	/**
	 * Constructs a new Node.
	 */
	public Node() {
		super();
	}

	/**
	 * Adds a new child to the collection of child nodes. If there is no previous
	 * child nodes, the method uses another method called addFirstChildNode to
	 * allocate a new collection and store the first node.
	 * 
	 * @param child
	 *            Child Node to add.
	 */
	public void addChildNode(Node child) {
		if (childNodes == null)
			addFirstChildNode(child);
		else {

			childNodes.add(child);
		}
	}

	/**
	 * Allocates a collection to store child nodes and stores the first one inside.
	 * 
	 * @param child
	 *            Child to add.
	 */
	private void addFirstChildNode(Node child) {
		childNodes = new ArrayIndexedCollection();
		childNodes.add(child);
	}

	/**
	 * Returns a number of children of a Node.
	 * 
	 * @return Number of children of a Node.
	 */
	public int numberOfChildren() {
		if (childNodes == null) {
			return 0;
		}
		return childNodes.size();
	}

	/**
	 * Gets the child at a wanted index.
	 * 
	 * @param index
	 *            Wanted index.
	 * @return Child at a wanted index.
	 * @throws IllegalArgumentException
	 *             If the index is invalid.
	 */
	public Node getChild(int index) {
		if (index >= childNodes.size() || index < 0)
			throw new IllegalArgumentException("Invalid index.");
		return (Node) childNodes.get(index);
	}

	/**
	 * Method that accepts a visitor to this particular node and allows the visitor
	 * to process it.
	 * 
	 * @param visitor
	 *            Node visitor.
	 */
	public void accept(INodeVisitor visitor) {
		// TODO Auto-generated method stub

	}
}
