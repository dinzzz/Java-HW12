package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Class that represents a value wrapper which acts as a wrapper for any type of
 * the object. This class is allowed to perform certain mathematical operation
 * on the wrapped number after checking the object data.
 * 
 * @author Dinz
 *
 */
public class ValueWrapper {
	/**
	 * Value of the object inside the wrapper.
	 */
	private Object value;

	/**
	 * Wraps a new object and constructs a new ValueWrapper instance.
	 * 
	 * @param value
	 *            Value of the object.
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Gets the value of the object inside the wrapper.
	 * 
	 * @return Value of the object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value of the object.
	 * 
	 * @param value
	 *            Value to be set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Performs an addition operation on the object and stores the result. Object
	 * has to be a number.
	 * 
	 * @param incValue
	 *            Number to add to the object.
	 * @throws RuntimeException
	 *             if the object is neither a number nor null.
	 */
	public void add(Object incValue) {
		if (!checkArguments(this.value, incValue)) {
			throw new RuntimeException("Invalid operation for the given objects.");
		}
		this.value = processOperation(this.value, incValue, OperationType.ADD);

	}

	/**
	 * Performs a multiplication operation on the object and stores the result.
	 * Object has to be a number.
	 * 
	 * @param mulValue
	 *            Number to multiply the object with.
	 * @throws RuntimeException
	 *             if the object is neither a number nor null.
	 */
	public void multiply(Object mulValue) {
		if (!checkArguments(this.value, mulValue)) {
			throw new RuntimeException("Invalid operation for the given objects.");
		}
		this.value = processOperation(this.value, mulValue, OperationType.MULTIPLY);

	}

	/**
	 * Performs a subtraction operation on the object and stores the result. Object
	 * has to be a number.
	 * 
	 * @param decValue
	 *            Number to subtract the object with.
	 * @throws RuntimeException
	 *             if the object is neither a number nor null.
	 */
	public void subtract(Object decValue) {
		if (!checkArguments(this.value, decValue)) {
			throw new RuntimeException("Invalid operation for the given objects.");
		}
		this.value = processOperation(this.value, decValue, OperationType.SUBTRACT);
	}

	/**
	 * Performs a division operation on the object and stores the result. Object has
	 * to be a number or null.
	 * 
	 * @param divValue
	 *            Number to divide the object with.
	 * @throws RuntimeException
	 *             if the object is neither a number nor null.
	 */
	public void divide(Object divValue) {
		if (!checkArguments(this.value, divValue)) {
			throw new RuntimeException("Invalid operation for the given objects.");
		}
		this.value = processOperation(this.value, divValue, OperationType.DIVIDE);

	}

	/**
	 * Performs a comparison between the object and the given another object.
	 * 
	 * @param withValue
	 *            Number to compare the object with.
	 * @return Positive integer if the object is bigger then the given object,
	 *         negative integer if the situation is contrary, and 0 if the values
	 *         are equal.
	 * @throws RuntimeException
	 *             if the object is neither a number nor null.
	 */
	public int numCompare(Object withValue) {
		if (!checkArguments(this.value, withValue)) {
			throw new RuntimeException("Invalid operation for the given objects.");
		}

		Object result = processOperation(this.value, withValue, OperationType.NUMCOMPARE);

		if (getObjectType(result) == ObjectType.DOUBLE) {
			double d = (Double) result;
			int resultInt = (int) d;
			return resultInt;
		}

		return Integer.parseInt(processOperation(this.value, withValue, OperationType.NUMCOMPARE).toString());

	}

	/**
	 * Checks the argument for arithmetic operations.
	 * 
	 * @param current
	 *            Current object we process with the operation.
	 * @param argument
	 *            Argument object.
	 * @return True if both arguments are fine, false otherwise.
	 */
	private boolean checkArguments(Object current, Object argument) {
		if (current instanceof Integer || current instanceof Double || current instanceof String || current == null) {
			if (argument instanceof Integer || argument instanceof Double || argument instanceof String
					|| argument == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the string value is integer.
	 * 
	 * @param s
	 *            String value.
	 * @return True if the string is parseable to integer, false otherwise.
	 */
	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * Checks if the string value is double.
	 * 
	 * @param s
	 *            String value.
	 * @return True if the string is parseable to double, false otherwise.
	 */
	private static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * Parses a string to either double or integer.
	 * 
	 * @param s
	 *            String to parse.
	 * @return Parsed number.
	 * @throws RuntimeException
	 *             If the string is in invalid format.
	 */
	private Object parseString(String s) {
		if (isInteger(s)) {
			return Integer.parseInt(s);
		} else if (isDouble(s)) {
			return Double.parseDouble(s);
		} else
			throw new RuntimeException("Invalid operation for the given string: " + s);
	}

	/**
	 * Method that checks and returns the type of the object. Valid types are
	 * integer, double and string.
	 * 
	 * @param object
	 *            Object to be checked.
	 * @return Type of the object in form of an enumeration value.
	 * @throws IllegalArgumentException
	 *             If the object is in invalid type.
	 */
	private ObjectType getObjectType(Object object) {
		if (object instanceof Integer) {
			return ObjectType.INTEGER;
		}
		if (object instanceof Double) {
			return ObjectType.DOUBLE;
		}
		if (object instanceof String) {
			return getObjectType(parseString((String) object));
		} else {
			throw new IllegalArgumentException("Invalid objects.");
		}
	}

	/**
	 * Processes the general operation on the object. Method defines the right
	 * output type and forwards the operation accordingly.
	 * 
	 * @param current
	 *            Object which is being processed.
	 * @param argument
	 *            Argument which is used as a second operator in operation.
	 * @param type
	 *            Type of the operation.
	 * @return Result of the operation.
	 * @throws IllegalArgumentException
	 *             If the objects used in an operation are invalid.
	 */
	@SuppressWarnings("deprecation")
	private Object processOperation(Object current, Object argument, OperationType type) {
		if (current == null) {
			current = new Integer(0);
		}
		if (argument == null) {
			argument = new Integer(0);
		}
		if (getObjectType(current) == ObjectType.INTEGER && getObjectType(argument) == ObjectType.INTEGER) {
			return intOperation(Integer.parseInt(current.toString()), Integer.parseInt(argument.toString()), type);
		}
		if (getObjectType(current) == ObjectType.DOUBLE || getObjectType(argument) == ObjectType.DOUBLE) {
			return doubleOperation(Double.parseDouble(current.toString()), Double.parseDouble(argument.toString()),
					type);
		}
		throw new IllegalArgumentException("Invalid objects used in operation.");

	}

	/**
	 * Processes an operation on two integer objects.
	 * 
	 * @param current
	 *            Object to be processed.
	 * @param argument
	 *            Argument object which is used as a second operator in an
	 *            operation.
	 * @param type
	 *            Type of the operation.
	 * @return Result of the operation-
	 * @throws IllegalArgumentException
	 *             If the argument is 0 and is used as a divider in an operation or
	 *             the operation is unknown.
	 */
	private int intOperation(int current, int argument, OperationType type) {
		if (type == OperationType.ADD) {
			return current + argument;
		} else if (type == OperationType.SUBTRACT) {
			return current - argument;
		} else if (type == OperationType.MULTIPLY) {
			return current * argument;
		} else if (type == OperationType.DIVIDE) {
			if (argument == 0) {
				throw new IllegalArgumentException("Can't divide with zero.");
			}
			return current / argument;
		} else if (type == OperationType.NUMCOMPARE) {
			return Integer.compare(current, argument);
		} else {
			throw new IllegalArgumentException("Unknown operation.");
		}
	}

	/**
	 * Processes an operation on two double objects.
	 * 
	 * @param current
	 *            Object to be processed.
	 * @param argument
	 *            Argument object which is used as a second operator in an
	 *            operation.
	 * @param type
	 *            Type of the operation.
	 * @return Result of the operation-
	 * @throws IllegalArgumentException
	 *             If the argument is 0 and is used as a divider in an operation or
	 *             the operation is unknown.
	 */
	private double doubleOperation(double current, double argument, OperationType type) {
		if (type == OperationType.ADD) {
			return current + argument;
		} else if (type == OperationType.SUBTRACT) {
			return current - argument;
		} else if (type == OperationType.MULTIPLY) {
			return current * argument;
		} else if (type == OperationType.DIVIDE) {
			if (argument == 0) {
				throw new IllegalArgumentException("Can't divide with zero.");
			}
			return current / argument;
		} else if (type == OperationType.NUMCOMPARE) {
			return Double.compare(current, argument);
		} else {
			throw new IllegalArgumentException("Unknown operation.");
		}
	}

}
