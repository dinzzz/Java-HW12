package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents a worker which echoes the parameters to the client
 * based on the request. Class takes the parameters from the request and wraps
 * them into a table which is then presented on the page.
 * 
 * @author Dinz
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		context.write("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<style>\r\n" + "table, th, td {\r\n"
				+ "    border: 1px solid black;\r\n" + "}\r\n" + "</style>\r\n" + "</head>");
		context.write("<table>");
		for (String string : context.getParameterNames()) {
			context.write("<tr>");
			context.write("<th>" + string + "</th>");
			context.write("<th>" + context.getParameter(string) + "</th>");
			context.write("</tr>");
		}
		context.write("</table>");
	}

}
