package hr.fer.zemris.java.webserver;

/**
 * Interface which describes a web worker - worker which processes a request
 * based on the type of the worker.
 * 
 * @author Dinz
 *
 */
public interface IWebWorker {
	
	/**
	 * Method which processes the request and outputs it to the client.
	 * 
	 * @param context
	 *            Context.
	 * @throws Exception
	 */
	public void processRequest(RequestContext context) throws Exception;
}
