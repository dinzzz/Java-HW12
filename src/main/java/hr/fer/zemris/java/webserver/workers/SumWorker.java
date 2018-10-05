package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that represents a worker which sums two numbers. This class takes two
 * given parameters, transforms them to a default value if they are invalid, and
 * dispatches the request with correct parameters to the calculation smart
 * script.
 * 
 * @author Dinz
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String a = context.getParameter("a");
		String b = context.getParameter("b");

		if (!isInteger(a)) {
			a = "1";
		}
		if (!isInteger(b)) {
			b = "2";
		}

		String zbroj = Integer.toString((Integer.parseInt(a) + Integer.parseInt(b)));
		context.setTemporaryParameter("a", a);
		context.setTemporaryParameter("b", b);
		context.setTemporaryParameter("zbroj", zbroj);
		context.getDispatcher().dispatchRequest("/private/calc.smscr");

	}

	/**
	 * Method which checks if the given string is an integer.
	 * 
	 * @param s
	 *            String to be checked.
	 * @return True if the string is an integer, false otherwise.
	 */
	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

}
