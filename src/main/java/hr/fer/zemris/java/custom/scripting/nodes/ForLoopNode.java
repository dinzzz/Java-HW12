package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Class that represents a node which stores a for loop type of expression. It
 * is consisted of an initial variable, start expression, end expression and if
 * needed, from step expression. Parser makes sure that ForLoopNode is in valid
 * format.
 * 
 * @author Dinz
 */
public class ForLoopNode extends Node {
	/**
	 * Initial variable of a ForLoopNode.
	 */
	ElementVariable variable;
	/**
	 * Start expression.
	 */
	Element startExpression;
	/**
	 * End expression.
	 */
	Element endExpression;
	/**
	 * Step expression.
	 */
	Element stepExpression;

	/**
	 * Constructs a new ForLoopNode without the pressence of a step expression.
	 * 
	 * @param variable
	 *            Initial variable.
	 * @param startExpression
	 *            Start expression.
	 * @param endExpression
	 *            End expression.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
	}

	/**
	 * Constructs a new ForLoopNode with the pressence of all expressions.
	 * 
	 * @param variable
	 *            Initial variable.
	 * @param startExpression
	 *            Start expression.
	 * @param endExpression
	 *            End expression.
	 * @param stepExpression
	 *            Step expression.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		this(variable, startExpression, endExpression);
		this.stepExpression = stepExpression;
	}

	/**
	 * Returns the variable of the ForLoopNode.
	 * 
	 * @return Variable of the ForLoopNode.
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Returns the start expression.
	 * 
	 * @return Start expression.
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Returns the end expression.
	 * 
	 * @return End expression.
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Returns the step expression.
	 * 
	 * @return Step expression.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

}
