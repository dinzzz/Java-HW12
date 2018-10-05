package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents a smart script engine - An engine which executes the
 * script which was previously formed into a document tree. The engine and the
 * scripts support various single and multi-variable operations.
 * 
 * @author Dinz
 *
 */
public class SmartScriptEngine {

	/**
	 * The top node of the document to be executed.
	 */
	private DocumentNode documentNode;

	/**
	 * Context.
	 */
	private RequestContext requestContext;

	/**
	 * Multistack used for storing objects in the process of executing the script.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();

	/**
	 * Visitor used in the executing process for running through the document nodes.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) throws IOException {
			String text = node.getText();
			requestContext.write(text);

		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().getName();
			multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));

			while (multistack.peek(variable).numCompare(node.getEndExpression().asText()) < 0) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					Node child = node.getChild(i);
					child.accept(visitor);
				}
				multistack.peek(variable).add(node.getStepExpression().asText());
			}

			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();
			for (Element element : node.getElements()) {
				if (element instanceof ElementConstantDouble || element instanceof ElementConstantInteger
						|| element instanceof ElementString) {
					stack.push(element.asText());
				} else if (element instanceof ElementVariable) {
					Object varValue = multistack.peek(element.asText()).getValue();
					stack.push(varValue.toString());
				} else if (element instanceof ElementOperator) {
					ValueWrapper operOne = new ValueWrapper(stack.pop().toString());
					String operTwo = stack.pop().toString();
					stack.push(operator(element, operOne, operTwo));

				} else if (element instanceof ElementFunction) {
					function(stack, element);
				}
			}
			Collections.reverse(stack);
			while (!stack.isEmpty()) {
				try {
					requestContext.write(stack.pop().toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		/**
		 * Method that executes the functions which occur in the echo node.
		 * 
		 * @param stack
		 *            Temporary echo node stack used to store the elements for
		 *            processing operations.
		 * @param element
		 *            Function to be processed.
		 */
		private void function(Stack<Object> stack, Element element) {
			if (element.asText().equals("@sin")) {
				Object num = stack.pop();
				double arg;
				if (num instanceof Integer) {
					arg = Double.parseDouble(num.toString());
				} else {
					arg = (double) num;
				}
				stack.push(Math.sin(arg));

			} else if (element.asText().equals("@decfmt")) {
				String formatString = stack.pop().toString();
				DecimalFormat format = new DecimalFormat(formatString);
				Object num = stack.pop();
				stack.push(format.format(num));

			} else if (element.asText().equals("@dup")) {
				Object x = stack.pop();
				stack.push(x);
				stack.push(x);

			} else if (element.asText().equals("@swap")) {
				Object x = stack.pop();
				Object y = stack.pop();
				stack.push(x);
				stack.push(y);

			} else if (element.asText().equals("@setMimeType")) {
				String x = stack.pop().toString();
				requestContext.setMimeType(x);

			} else if (element.asText().equals("@paramGet")) {
				String defaultValue = stack.pop().toString();
				String name = stack.pop().toString();

				String value = requestContext.getParameter(name);
				stack.push(value == null ? defaultValue : value);

			} else if (element.asText().equals("@pparamGet")) {
				String defaultValue = stack.pop().toString();
				String name = stack.pop().toString();

				String value = requestContext.getPersistentParameter(name);
				stack.push(value == null ? defaultValue : value);

			} else if (element.asText().equals("@pparamSet")) {
				String name = stack.pop().toString();
				String value = stack.pop().toString();

				requestContext.setPersistentParameter(name, value);

			} else if (element.asText().equals("@pparamDel")) {
				String name = stack.pop().toString();
				requestContext.removePersistentParameter(name);

			} else if (element.asText().equals("@tparamGet")) {
				String defaultValue = stack.pop().toString();
				String name = stack.pop().toString();

				String value = requestContext.getTemporaryParameter(name);
				stack.push(value == null ? defaultValue : value);

			} else if (element.asText().equals("@tparamSet")) {
				String name = stack.pop().toString();
				String value = stack.pop().toString();

				requestContext.setTemporaryParameter(name, value);

			} else if (element.asText().equals("@tparamDel")) {
				String name = stack.pop().toString();
				requestContext.removeTemporaryParameter(name);
			}
		}

		/**
		 * Method that executes the basic mathematical operations between two values. In
		 * other words, Double binary operator operations.
		 * 
		 * @param element
		 *            Operator.
		 * @param operOne
		 *            First number in the operation.
		 * @param operTwo
		 *            Second number in the operation.
		 * @return
		 */
		private Object operator(Element element, ValueWrapper operOne, String operTwo) {
			if (element.asText().equals("*")) {
				operOne.multiply(operTwo);
				return operOne.getValue();
			} else if (element.asText().equals("/")) {
				operOne.divide(operTwo);
				return operOne.getValue();
			} else if (element.asText().equals("+")) {
				operOne.add(operTwo);
				return operOne.getValue();
			} else if (element.asText().equals("-")) {
				operOne.subtract(operTwo);
				return operOne.getValue();
			} else {
				throw new IllegalArgumentException("Invalid operator");
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				Node child = node.getChild(i);
				child.accept(visitor);
			}

		}

	};

	/**
	 * Constructs a new smart script engine.
	 * 
	 * @param documentNode
	 *            Document node.
	 * @param requestContext
	 *            Context.
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Method which executes and runs the engine.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

}
