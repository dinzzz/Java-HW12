package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Class that represents a parser. Parser receives a document in string format,
 * does a lexical analysis using the Lexer class and then builds a Node tree
 * based on the tokens received. Then it can be used to create an original
 * document body. 
 * 
 * @author Dinz
 *
 */
public class SmartScriptParser {
	/**
	 * Lexer used in a parser.
	 */
	private SmartScriptLexer lexer;
	/**
	 * Document node that is at the top of the tree.
	 */
	private DocumentNode documentNode;

	/**
	 * Constructs a new Parser from a document and analyzes the document by running
	 * it in SmartScriptLexer.
	 * 
	 * @param document
	 *            Document to parse.
	 */
	public SmartScriptParser(String document) {
		if(document == null) {
			throw new NullPointerException("Document must not be null!");
		}
		lexer = new SmartScriptLexer(document);
		this.parse();
	}

	/**
	 * Method that parses the tokens given by a lexer. This is the key method of the
	 * class where most of the work is done. The method checks if the given
	 * operation expression are in valid format - precisely ForLoopNodes. Firstly,
	 * the method stores the token inside an ArrayIndexedCollection and processes it
	 * further. The method adds child nodes to the beggining documentNode and forms
	 * a node-tree.
	 * 
	 * @throws SmartScriptParserException
	 *             If the expression is in invalid format.
	 */
	public void parse() {
		ObjectStack stack = new ObjectStack();
		this.documentNode = new DocumentNode();
		stack.push(documentNode);

		ArrayIndexedCollection tokens = new ArrayIndexedCollection();
		do {
			SmartScriptToken actual = lexer.nextToken();
			if (actual.getType() == SmartScriptTokenType.EOF) {
				break;
			}
			tokens.add(actual);
		} while (true);

		// imamo polje puno ispravnih tagova. Sad, prolazimo kroz polje i slijedno
		// gradimo stablo

		for (int i = 0; i < tokens.size(); i++) {
			SmartScriptToken actual = (SmartScriptToken) tokens.get(i);
			if (actual.getType() == SmartScriptTokenType.TEXT) {
				TextNode textNode = new TextNode((String) actual.getValue());
				Node node = (Node) stack.peek();
				node.addChildNode(textNode);

			} else if (actual.getType() == SmartScriptTokenType.STARTTAG) {
				i++;
				actual = (SmartScriptToken) tokens.get(i);
				if (actual.getType() == SmartScriptTokenType.FOR) {
					// završi tag, addaj child, pushaj for, getaj i provjeri elemente
					i++;
					actual = (SmartScriptToken) tokens.get(i);
					if (actual.getType() == SmartScriptTokenType.VARIABLE) {
						ElementVariable elementVariable = new ElementVariable((String) actual.getValue());
						i++;

						ArrayIndexedCollection forArray = new ArrayIndexedCollection();
						while (true) {
							actual = (SmartScriptToken) tokens.get(i);
							if (actual.getType() == SmartScriptTokenType.ENDTAG) {
								break;
							}
							if (actual.getType() == SmartScriptTokenType.VARIABLE) {
								ElementVariable elementV = new ElementVariable((String) actual.getValue());
								forArray.add(elementV);
							} else if (actual.getType() == SmartScriptTokenType.DOUBLE) {
								ElementConstantDouble elementD = new ElementConstantDouble((Double) actual.getValue());
								forArray.add(elementD);
							} else if (actual.getType() == SmartScriptTokenType.INTEGER) {
								ElementConstantInteger elementI = new ElementConstantInteger(
										(Integer) actual.getValue());
								forArray.add(elementI);
							} else if (actual.getType() == SmartScriptTokenType.STRING) {
								ElementString elementS = new ElementString((String) actual.getValue());
								forArray.add(elementS);
							} else {
								throw new SmartScriptParserException("Wrong construction of the for construct.");
							}
							i++;

						}

						ForLoopNode forLoopNode;
						if (forArray.size() == 2) {
							forLoopNode = new ForLoopNode(elementVariable, (Element) forArray.get(0),
									(Element) forArray.get(1));
						} else if (forArray.size() == 3) {
							forLoopNode = new ForLoopNode(elementVariable, (Element) forArray.get(0),
									(Element) forArray.get(1), (Element) forArray.get(2));

						} else {
							throw new SmartScriptParserException("Invalid number of arguments in for loop.");
						}

						Node node = (Node) stack.peek();
						node.addChildNode(forLoopNode);
						stack.push(forLoopNode);

					} else {
						throw new SmartScriptParserException("Unable to parse, no variable in for loop.");
					}

				} else if (actual.getType() == SmartScriptTokenType.TAGNAME) {
					i++;
					ArrayIndexedCollection forArray = new ArrayIndexedCollection();
					while (true) {
						actual = (SmartScriptToken) tokens.get(i);
						if (actual.getType() == SmartScriptTokenType.ENDTAG) {
							break;
						}
						if (actual.getType() == SmartScriptTokenType.VARIABLE) {
							ElementVariable elementV = new ElementVariable((String) actual.getValue());
							forArray.add(elementV);
						} else if (actual.getType() == SmartScriptTokenType.DOUBLE) {
							ElementConstantDouble elementD = new ElementConstantDouble((Double) actual.getValue());
							forArray.add(elementD);
						} else if (actual.getType() == SmartScriptTokenType.INTEGER) {
							ElementConstantInteger elementI = new ElementConstantInteger((Integer) actual.getValue());
							forArray.add(elementI);
						} else if (actual.getType() == SmartScriptTokenType.STRING) {
							ElementString elementS = new ElementString((String) (actual.getValue()));
							forArray.add(elementS);
						} else if (actual.getType() == SmartScriptTokenType.FUNCTION) {
							ElementFunction elementF = new ElementFunction((String) actual.getValue());
							forArray.add(elementF);
						} else if (actual.getType() == SmartScriptTokenType.OPERATOR) {
							ElementOperator elementO = new ElementOperator((String) actual.getValue());
							forArray.add(elementO);
						} else {
							throw new SmartScriptParserException("Unable to parse.");
						}
						i++;
					}
					Element[] elements = new Element[forArray.size()];

					for (int counter = 0; counter < forArray.size(); counter++) {
						elements[counter] = (Element) forArray.get(counter);
					}

					EchoNode echoNode = new EchoNode(elements);
					Node node = (Node) stack.peek();
					node.addChildNode(echoNode);

				} else if (actual.getType() == SmartScriptTokenType.END) {
					// popaj sa stacka
					i++;
					actual = (SmartScriptToken) tokens.get(i);
					if (actual.getType() == SmartScriptTokenType.ENDTAG) {
						@SuppressWarnings("unused")
						Node poppedNode = (Node) stack.pop();
					} else {
						throw new SmartScriptParserException("Invalid tag.");
					}
				} else {
					throw new SmartScriptParserException("Unable to parse");
				}

			} else {
				throw new SmartScriptParserException("Unable to parse.");
			}
		}

	}

	/**
	 * Returns the documentNode - node at the top of the node-tree.
	 * 
	 * @return Document Node.
	 */
	public DocumentNode getDocumentNode() {
		return this.documentNode;
	}

	/**
	 * Creates an original document body from the document node. It 
	 * stores it into a string file and is ready to be printed into a file or
	 * a console.
	 * 
	 * @param documentNode Document Node
	 * @return String representation of the document body.
	 */
	public String createOriginalDocumentBody(Node documentNode) {
		StringBuilder documentBuilder = new StringBuilder();

		if (documentNode instanceof ForLoopNode) {
			documentBuilder.append("{$ FOR");
			documentBuilder.append(" " + ((ForLoopNode) documentNode).getVariable().asText());
			documentBuilder.append(" " + ((ForLoopNode) documentNode).getStartExpression().asText());
			documentBuilder.append(" " + ((ForLoopNode) documentNode).getEndExpression().asText());
			if (((ForLoopNode) documentNode).getStepExpression() != null) {
				documentBuilder.append(" " + ((ForLoopNode) documentNode).getStepExpression().asText());
			}
			documentBuilder.append(" $}");
			if (documentNode.numberOfChildren() > 0) {
				for (int i = 0; i < documentNode.numberOfChildren(); i++) {
					Node child = documentNode.getChild(i);
					documentBuilder.append(createOriginalDocumentBody(child));
				}
			}
			documentBuilder.append("{$END$}");
		}
		if (documentNode instanceof EchoNode) {
			documentBuilder.append("{$=");
			for (int i = 0; i < ((EchoNode) documentNode).getElements().length; i++) {
				documentBuilder.append(" " + ((EchoNode) documentNode).getElements()[i].asText());
			}
			documentBuilder.append(" $}");
		}
		if (documentNode instanceof TextNode) {
			documentBuilder.append(((TextNode) documentNode).getText());
		}

		if (documentNode instanceof DocumentNode) {
			if (documentNode.numberOfChildren() > 0) {
				for (int i = 0; i < documentNode.numberOfChildren(); i++) {
					Node child = documentNode.getChild(i);
					documentBuilder.append(createOriginalDocumentBody(child));
				}
			}
		}

		return documentBuilder.toString();

	}
}
