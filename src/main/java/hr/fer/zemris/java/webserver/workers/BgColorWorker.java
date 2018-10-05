package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents a background color worker. This worker sets the
 * background color of the home page of the server.
 * 
 * @author Dinz
 *
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String color = context.getParameter("bgcolor");
		try {
			checkFormat(color);
			context.setMimeType("text/html");
			context.setPersistentParameter("bgcolor", color);
			context.write("<html>\r\n" + " <head>\r\n" + " <title>Color updated</title>\r\n" + " </head>\r\n"
					+ " <body>\r\n" + " <h1>Color updated</h1>" + "<a href=\"/index2.html\">Home</a>"
					+ "<p>The color of the background has been updated.</p>" + "</body>" + "</html>");
		} catch (IllegalArgumentException ex) {
			context.setMimeType("text/html");
			context.write("<html>\r\n" + " <head>\r\n" + " <title>Color not updated</title>\r\n" + " </head>\r\n"
					+ " <body>\r\n" + " <h1>Color not updated</h1>" + "<a href=\"/index2.html\">Home</a>"
					+ "<p>The color of the background has not been updated, the value was not valid.</p>" + "</body>"
					+ "</html>");
		}

	}

	/**
	 * Method that checks the format of the given color from the request.
	 * 
	 * @param color
	 *            Given color.
	 */
	private void checkFormat(String color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		if (color.length() != 6) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < color.length(); i++) {
			char c = color.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F')) {
				throw new IllegalArgumentException();
			}
		}

	}

}
