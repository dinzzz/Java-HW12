package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Class that represents a DocumentNode - Node that occurs ones the document is
 * created and given to the parser.
 * 
 * @author Dinz
 *
 */
public class DocumentNode extends Node {
	/**
	 * Constructs a new DocumentNode.
	 */
	public DocumentNode() {
		super();
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
