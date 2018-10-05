package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Class that demonstrates the funcionality of SmartScriptEngine running a
 * prewritten script for addition of two numbers.
 * 
 * @author Dinz
 *
 */
public class AdditionDemo {

	/**
	 * Main method that runs the program.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String documentBody = readFromDisk("src/main/resources/zbrajanje.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();

	}

	/**
	 * Method which reads the data from disk.
	 * 
	 * @param string
	 *            Path of the file to be read.
	 * @return String format of the file data.
	 * @throws IOException
	 */
	private static String readFromDisk(String string) throws IOException {
		Path path = Paths.get(string);
		byte[] file = Files.readAllBytes(path);
		String docBody = new String(file, StandardCharsets.UTF_8);
		return docBody;
	}

}
