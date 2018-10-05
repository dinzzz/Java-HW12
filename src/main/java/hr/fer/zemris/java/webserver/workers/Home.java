package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents a worker for the homepage. When called, this worker
 * determines the needed color for the homepage background and dispatches the
 * request to the home smart script.
 * 
 * @author Dinz
 *
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("text/html");
		String bgcolor = context.getPersistentParameter("bgcolor");

		if (bgcolor != null) {
			context.setTemporaryParameter("background", bgcolor);
		} else {
			context.setTemporaryParameter("background", "7F7F7F");
		}

		context.getDispatcher().dispatchRequest("/private/home.smscr");

	}

}
