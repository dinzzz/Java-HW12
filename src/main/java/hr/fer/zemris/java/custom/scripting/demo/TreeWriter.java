package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Class that represents a tree writer. This is an implementation of the
 * execution of the smart script parser which outputs the document body to the
 * console. It uses a visitor pattern to achieve its functionality.
 * 
 * @author Dinz
 *
 */
public class TreeWriter {

	/**
	 * Inner class that represents a visitor used in a tree writer class.
	 * 
	 * @author Dinz
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());

		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			StringBuilder documentBuilder = new StringBuilder();
			documentBuilder.append("{$ FOR");
			documentBuilder.append(" " + node.getVariable().asText());
			documentBuilder.append(" " + node.getStartExpression().asText());
			documentBuilder.append(" " + (node).getEndExpression().asText());
			if (node.getStepExpression() != null) {
				documentBuilder.append(" " + node.getStepExpression().asText());
			}
			documentBuilder.append(" $}");
			System.out.print(documentBuilder.toString());
			if (node.numberOfChildren() > 0) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
			}
			System.out.print("{$END$}");

		}

		@Override
		public void visitEchoNode(EchoNode node) {
			StringBuilder documentBuilder = new StringBuilder();
			documentBuilder.append("{$=");
			for (int i = 0; i < node.getElements().length; i++) {
				documentBuilder.append(" " + node.getElements()[i].asText());
			}
			documentBuilder.append(" $}");
			System.out.print(documentBuilder.toString());

		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			if (node.numberOfChildren() > 0) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
			}

		}
	}

	/**
	 * Main method that runs the class.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("The program accepts a file name as an argument!");
			return;
		}

		Path path = Paths.get(args[0]);
		byte[] file = Files.readAllBytes(path);
		String docBody = new String(file, StandardCharsets.UTF_8);

		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

}
