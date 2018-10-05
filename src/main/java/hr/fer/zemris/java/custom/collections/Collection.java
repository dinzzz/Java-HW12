package hr.fer.zemris.java.custom.collections;

/**
 * Class that represent the use of general concept of object collections.
 * 
 * @author Dinz
 *
 */
public class Collection {

	/**
	 * Constructor that defines the collection without any arguments.
	 */
	protected Collection() {
		super();
	}

	/**
	 * Checks if the collection is empty or not.
	 * 
	 * @return true if the collection is empty, false otherwise
	 *         otherwise
	 */
	public boolean isEmpty() {
		if (this.size() == 0)
			return true;
		else
			return false;
	}

	/**
	 * Returns the current size of the collection.
	 * 
	 * @return Size of the collection. Zero by default.
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds an object to the collection.
	 * 
	 * @param value
	 *            Object to be added
	 */
	public void add(Object value) {

	}

	/**
	 * Checks if collection contains the given object.
	 * 
	 * @param value
	 *            Object to be checked
	 * @return true if the collection contains the object, false otherwise. Returns
	 *         false by default.
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes the given object from the collection.
	 * 
	 * @param value
	 *            Object to be removed
	 * @return True if the object is removed, false otherwise. False by default.
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Transforms the collection to the object array.
	 * 
	 * @return An array of object that were stored in a collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Applies the action of the processor to the whole collection.
	 * 
	 * @param processor
	 *            Processor instance
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Adds all elements of the other collection to the current one.
	 * 
	 * @param other
	 *            Another collection
	 */
	public void addAll(Collection other) {
		class LocalProcessor extends Processor {

			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new LocalProcessor());
	}

	/**
	 * Clears the collection.
	 */
	public void clear() {

	}

}
