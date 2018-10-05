package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Class that demonstrates the use of ValueWrapper class. The output has to
 * match the output noted in the official directions for this task.
 * 
 * @author Dinz
 *
 */
public class ValueWrapperDemo {
	/**
	 * Method that executes the class.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 */
	public static void main(String[] args) {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
		System.out.println(v1.getValue()); // 0
		System.out.println(v2.getValue()); // null

		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
		System.out.println(v3.getValue()); // 13.0
		System.out.println(v4.getValue()); // 1

		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
		System.out.println(v5.getValue()); // 13
		System.out.println(v6.getValue()); // 1

		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		try {
			v7.add(v8.getValue()); // throws RuntimeException
		} catch (RuntimeException ex) {
			System.out.println(ex.getMessage());
		}

	}

}
