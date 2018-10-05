package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Class that demonstrates functionality of the ObjectMultistack class. This
 * class is an example how the ObjectMulticlass must work and is written on the
 * official task directions.
 * 
 * @author Dinz
 *
 */
public class ObjectMultistackDemo {
	/**
	 * Method that executes the class.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 */
	public static void main(String[] args) {
		ObjectMultistack multistack = new ObjectMultistack();
		ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
		multistack.push("year", year);
		ValueWrapper price = new ValueWrapper(200.51);
		multistack.push("price", price);
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		System.out.println("Current value for price: " + multistack.peek("price").getValue());
		multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		multistack.peek("year").setValue(((Integer) multistack.peek("year").getValue()).intValue() + 50);
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		multistack.pop("year");
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		multistack.peek("year").add("5");
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		multistack.peek("year").add(5);
		System.out.println("Current value for year: " + multistack.peek("year").getValue());
		multistack.peek("year").add(5.0);
		System.out.println("Current value for year: " + multistack.peek("year").getValue());

	}
}
