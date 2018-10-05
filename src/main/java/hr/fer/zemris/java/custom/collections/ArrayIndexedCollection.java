package hr.fer.zemris.java.custom.collections;

/**
 * Class that represents the usage of the resizeable indexed array-backed
 * collection. It supports duplicate elements and doesn't support null objects.
 * The collection got its initial capacity and dynamicaly reallocates according
 * to the usage. The objects are stored and manipulated with in an object array.
 * 
 * @author Dinz
 *
 */
public class ArrayIndexedCollection extends Collection {

	private int size;
	private int capacity;
	Object[] elements;

	private static final int DEFAULT_CAPACITY = 16;
	private static final int MINIMUM_CAPACITY = 1;

	/**
	 * Creates a default ArrayIndexedCollection with a default capacity.
	 */
	public ArrayIndexedCollection() {
		this.capacity = DEFAULT_CAPACITY;
		elements = new Object[this.capacity];
	}

	/**
	 * Creates an ArrayIndexedCollection with the desired capacity. Capacity cannot
	 * be fewer then 1.
	 * 
	 * @param initialCapacity
	 *            Capacity that allocates a new collection.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException("Array size cannot be fewer then 1!");
		}

		this.capacity = initialCapacity;
		elements = new Object[this.capacity];

	}

	/**
	 * Creates an ArrayIndexedCollection based on the given collection. Adds all the
	 * elements from the given collection to the current, new one.
	 * 
	 * @param other
	 *            Given collection
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other.size());
		if (other.equals(null)) {
			throw new NullPointerException("Initial collection must not be null!");
		}
		this.addAll(other);

	}

	/**
	 * Creates a new ArrayIndexedCollection based on a given collection and
	 * capacity. If the given capacity is fewer then the size of the given
	 * collection, the capacity of the newly made collection is equal to the given
	 * one.
	 * 
	 * @param other
	 *            Given collection
	 * @param initialCapacity
	 *            Desired capacity of the new collection
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(other.size() > initialCapacity ? other.size() : initialCapacity);
		if (other.equals(null)) {
			throw new NullPointerException("Initial collection must not be null!");
		}
		this.addAll(other);

	}

	/**
	 * Returns the current size of the ArrayIndexedCollection.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Adds an object to the ArrayIndexedCollection. Throws an exception if the
	 * given object is null. Reallocates an array if the collection filled its
	 * capacity by doubling it.
	 */
	@Override
	public void add(Object value) {
		if (value.equals(null))
			throw new NullPointerException("Object to add must not be null!");

		if (this.size == capacity) {
			this.reallocateArray();
		}

		elements[this.size] = value;
		this.size++;

	}

	/**
	 * Checks if the ArrayIndexedCollection contains the given object.
	 */
	@Override
	public boolean contains(Object value) {
		// foreach?
		for (int i = 0; i < this.size; i++)
			if (elements[i].equals(value))
				return true;

		return false;

	}

	/**
	 * Gets an object from the collection at the desired index
	 * 
	 * @param index
	 *            Index of the desired element
	 * @return Object at the given index
	 */
	public Object get(int index) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index out of bounds.");
		}
		return elements[index];

	}

	/**
	 * Clears the whole ArrayIndexedCollection
	 */
	@Override
	public void clear() {
		// foreach?
		for (int i = 0; i < this.size; i++) {
			elements[i] = null;
			this.size = 0;
		}

	}

	/**
	 * Inserts an object into the ArrayIndexedCollection at the desired position.
	 * Translates all the other elements of the collection by one place towards the
	 * end. Object must not be null and the position has to be in range of the
	 * current collection size - from 0 to the size of the collection.
	 * 
	 * @param value
	 *            Object to be added
	 * @param position
	 *            Desired position
	 */
	public void insert(Object value, int position) {
		if (value.equals(null))
			throw new NullPointerException("Object must not be null.");

		if (position < 0 || position > this.size)
			throw new IndexOutOfBoundsException("Invalid position.");

		if (this.size == this.capacity)
			this.reallocateArray();

		for (int i = position; i < this.size; i++) {
			elements[i + 1] = elements[i];
		}

		elements[position] = value;
		this.size++;

	}

	/**
	 * Returns the index of the first occurance of the given object.
	 * 
	 * @param value
	 *            Object to be checked
	 * @return index of the first occurance of the object. If there is no object in
	 *         a collection, returns -1
	 */
	public int indexOf(Object value) {
		if (this.contains(value)) {
			for (int i = 0; i < this.size; i++) {
				if (elements[i].equals(value))
					return i;
			}
			return -1;
		} else
			return -1;
	}

	/**
	 * Removes the first occurance of the given object from the
	 * ArrayIndexedCollection.
	 */
	@Override
	public boolean remove(Object value) {
		int index = this.indexOf(value);

		if (index == -1)
			return false;

		this.remove(index);
		return true;
	}

	public void remove(int index) {
		if (index < 0 || index > size - 1)
			throw new IndexOutOfBoundsException("Index out of bounds.");

		for (int i = index; i < this.size - 1; i++)
			elements[i] = elements[i + 1];

		this.size--;

	}

	/**
	 * Transforms and ArrayIndexedCollection into an object array.
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size];

		// foreach?
		for (int i = 0; i < this.size; i++)
			array[i] = elements[i];

		return array;
	}

	/**
	 * Applies the action of the processor to every element in the
	 * ArrayIndexedCollection
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < this.size; i++)
			processor.process(elements[i]);
	}

	/**
	 * Reallocated the ArrayIndexedCollection by doubleing the capacity and the
	 * elements array size.
	 */
	public void reallocateArray() {
		this.capacity *= 2;
		Object[] temp = elements;
		elements = new Object[this.capacity];
		for (int i = 0; i < temp.length; i++)
			elements[i] = temp[i];
	}

}
