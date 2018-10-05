package hr.fer.zemris.java.custom.scripting.nodes;

import java.io.IOException;

/**
 * Interface which describes what the implementations of the node visitors
 * should be like. Basically, every node visitor has to have a support for
 * visiting text node, forloop node, echo node and top document node.
 * 
 * @author Dinz
 *
 */
public interface INodeVisitor {
	/**
	 * Method which visits the text node of the document.
	 * 
	 * @param node
	 *            Text node.
	 * @throws IOException
	 */
	public void visitTextNode(TextNode node) throws IOException;

	/**
	 * Method which visits the forLoop node of the document.
	 * 
	 * @param node
	 *            ForLoop node.
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Method which visits the echo node of the document.
	 * 
	 * @param node
	 *            Echo node.
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Method which visits the document node.
	 * 
	 * @param node
	 *            Document node.
	 */
	public void visitDocumentNode(DocumentNode node);

}
