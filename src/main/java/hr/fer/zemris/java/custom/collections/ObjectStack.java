package hr.fer.zemris.java.custom.collections;

/**
 * Class that represents the stack-like collection. It uses an
 * ArrayIndexedCollection as a tool to implement usage and manipulation
 * techniques. Stack forbids the storage of null objects and permits the storage
 * of duplicate objects.
 * 
 * @author Dinz
 *
 */
public class ObjectStack {

	private ArrayIndexedCollection adaptee = new ArrayIndexedCollection();

	/**
	 * Constructs a new instance of the stack.
	 */
	public ObjectStack() {

	}

	/**
	 * Checks if the stack is empty.
	 * 
	 * @return true if the stack is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return (adaptee.isEmpty());
	}

	/**
	 * Returns the current size of the stack.
	 * 
	 * @return Current size of the stack
	 */
	public int size() {
		return (adaptee.size());
	}

	/**
	 * Pushes an object to the top of the stack. If an object is null, the method
	 * throws an exception.
	 * 
	 * @param value
	 *            Object to be pushed.
	 */
	public void push(Object value) {
		adaptee.add(value);
	}

	/**
	 * Pops an object from the top of the stack. If the stack is empty, the method
	 * throws an exception.
	 * 
	 * @return Object popped from the top of the stack.
	 */
	public Object pop() {
		if (adaptee.isEmpty())
			throw new EmptyStackException("Stack is empty.");

		Object popped = adaptee.get(adaptee.size() - 1);
		adaptee.remove(adaptee.size() - 1);
		return popped;
	}

	/**
	 * Peeks an object from the top of the stack. That means that the object will
	 * not be removed from the stack. If the stack is empty, the method throws an
	 * exception.
	 * 
	 * @return Peeked object
	 */
	public Object peek() {
		if (adaptee.isEmpty())
			throw new EmptyStackException("Stack is empty.");

		return adaptee.get(adaptee.size() - 1);
	}

	/**
	 * Clears the stack - removes all its elements
	 */
	public void clear() {
		adaptee.clear();
	}

}
