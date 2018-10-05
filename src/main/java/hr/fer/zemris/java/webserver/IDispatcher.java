package hr.fer.zemris.java.webserver;

/**
 * Interface that describes a dispatcher which dispatches a given request from
 * the client to another URL.
 * 
 * @author Dinz
 *
 */
public interface IDispatcher {
	/**
	 * Method which dispatches a given request to another URL.
	 * 
	 * @param urlPath
	 *            Url where the request will be dispatched.
	 * @throws Exception
	 */
	void dispatchRequest(String urlPath) throws Exception;
}
