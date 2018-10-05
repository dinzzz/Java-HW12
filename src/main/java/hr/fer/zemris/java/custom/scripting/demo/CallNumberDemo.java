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
 * Method that demonstrates the functionality of smart script engine when
 * running a script which counts the number of times an action has ocurred.
 * 
 * @author Dinz
 *
 */
public class CallNumberDemo {

	/**
	 * Main method that runs the program.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String documentBody = readFromDisk("src/main/resources/brojPoziva.smscr");
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

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
