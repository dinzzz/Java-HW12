package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents an object multistack - A collection that stores
 * stack-like architecture for every given key. Elements are stored into the
 * multistack by wrapping them into the ValueWrapper class. Key of the elements
 * and the ValueWrapper reference must not be null, as opposed to objects before
 * wrapping.
 * 
 * @author Dinz
 *
 */

public class ObjectMultistack {
	/**
	 * Map used as a storage for the class.
	 */
	private Map<String, MultistackEntry> elements = new HashMap<>();

	/**
	 * Pushed an element to the stack according to the given key.
	 * 
	 * @param name
	 *            Key of the element.
	 * @param valueWrapper
	 *            Element wrapped in the ValueWrapper class to be pushed to the
	 *            stack.
	 * @throws IllegalArgumentException
	 *             If either key or ValueWrapper reference is null.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (name == null) {
			throw new IllegalArgumentException("Key must not be null.");
		}
		if (valueWrapper == null) {
			throw new IllegalArgumentException("Element to push must not be null.");
		}
		if (!elements.containsKey(name)) {
			elements.put(name, new MultistackEntry(valueWrapper, null));
		} else {
			MultistackEntry entry = elements.get(name);
			while (entry.next != null) {
				entry = entry.next;
			}
			entry.next = new MultistackEntry(valueWrapper, null);
		}
	}

	/**
	 * Pops the element from the stack with the given key.
	 * 
	 * @param name
	 *            Key of the stack.
	 * @return ValueWrapper instance from the top of the stack.
	 * @throws NullPointerException
	 *             If null is given as a key.
	 * @throws EmptyStackException
	 *             If the stack with the given key is empty.
	 */
	public ValueWrapper pop(String name) {
		if (name == null) {
			throw new NullPointerException("Key of the multistack cannot be null.");
		}
		if (this.isEmpty(name)) {
			throw new EmptyStackException("Stack is empty.");
		}
		if (elements.containsKey(name)) {
			MultistackEntry entry = elements.get(name);
			if (entry.next == null) {
				ValueWrapper object = entry.getObject();
				elements.remove(name);
				return object;
			}
			MultistackEntry reference = entry;
			while (entry.next != null) {
				reference = entry;
				entry = entry.next;
			}

			ValueWrapper object = entry.getObject();
			reference.next = null;

			return object;
		}

		return null;
	}

	/**
	 * Peeks at the element from the stack with the given key.
	 * 
	 * @param name
	 *            Key of the stack.
	 * @return ValueWrapper instance from the top of the stack.
	 * @throws NullPointerException
	 *             If null is given as a key.
	 * @throws EmptyStackException
	 *             If the stack with the given key is empty.
	 */
	public ValueWrapper peek(String name) {
		if (name == null) {
			throw new NullPointerException("Key of the multistack cannot be null.");
		}
		if (this.isEmpty(name)) {
			throw new EmptyStackException("Stack is empty.");
		}
		if (elements.containsKey(name)) {
			MultistackEntry entry = elements.get(name);
			while (entry.next != null) {
				entry = entry.next;
			}

			return entry.getObject();
		}

		return null;
	}

	/**
	 * Checks if the stack with the given key is empty.
	 * 
	 * @param name
	 *            Key of the stack.
	 * @return True if the stack is empty, false otherwise.
	 */
	public boolean isEmpty(String name) {
		return !elements.containsKey(name);
	}

	/**
	 * Class that represents a node that stores elements into the ObjectMultistack
	 * collection. Every node is consisted of a ValueWrapper instance which holds an
	 * object and a reference to the next MultistackEntry in the stack.
	 * 
	 * @author Dinz
	 *
	 */
	static class MultistackEntry {
		/**
		 * ValueWrapper that holds an object.
		 */
		ValueWrapper object;
		/**
		 * Reference to the next entry in stack.
		 */
		MultistackEntry next;

		/**
		 * Constructs a new MultistackEntry.
		 * 
		 * @param object
		 *            ValueWrapper instance.
		 * @param next
		 *            Reference to the next entry.
		 */
		public MultistackEntry(ValueWrapper object, MultistackEntry next) {
			this.object = object;
			this.next = next;
		}

		/**
		 * Returns the object from the entry.
		 * 
		 * @return Object wrapped as a ValueWrapper instance.
		 */
		public ValueWrapper getObject() {
			return object;
		}

		/**
		 * Returns the next entry in the stack.
		 * 
		 * @return Next entry in the stack.
		 */
		public MultistackEntry getNext() {
			return next;
		}

	}

}
