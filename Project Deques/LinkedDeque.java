package deques;

import java.util.LinkedList;

/**
 * A doubly-linked implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class LinkedDeque<T> implements Deque<T> {
    /**
     * The sentinel node representing the front of this deque.
     */
    private Node<T> front;
    /**
     * The sentinel node representing the back of this deque.
     */
    private Node<T> back;
    /**
     * The number of elements in this deque.
     */
    private int size;

    private LinkedList<T> data;

    /**
     * Constructs an empty deque.
     */
    public LinkedDeque() {
        data = new LinkedList<T>();
        front = new Node<>(null, null, null);   //Set both nodes to null at construction
        back = new Node<>(null, null, null);
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node<T> tempFirst = new Node<>(item, null, null);
        if (size == 0) {
            front.next = tempFirst;
            tempFirst.prev = front;
            tempFirst.next = back;
            back.prev = tempFirst;
        } else {
            Node<T> curr = front.next;
            front.next = tempFirst;
            tempFirst.prev = front;
            tempFirst.next = curr;
            curr.prev = tempFirst;

        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node<T> tempLast = new Node<>(item, null, null);

        if(size == 0) {
            back.prev = tempLast;
            tempLast.next = back;
            tempLast.prev = front;
            front.next = tempLast;
        } else {
            Node<T> curr = back.prev;
            back.prev = tempLast;
            tempLast.next = back;
            tempLast.prev = curr;
            curr.next = tempLast;

        }
        size += 1;

    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> curr = front.next;
        Node<T> curr1 = curr.next;
        front.next = curr1;
        curr1.prev = front;
         size -= 1;

        return curr.value;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> curr = back.prev;
        back.prev = curr.prev;
        curr.prev.next = back;
        size -= 1;

        return curr.value;
    }

    @Override
    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        int count = 0;
        Node<T> getIndex = front;
        while(getIndex.next != null) {
            getIndex = getIndex.next;
            if(count == index) {
                return getIndex.value;
            }

            count+=1;
        }
        return getIndex.value;

    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns null if the front and back nodes form a valid linked deque. Otherwise, returns a
     * string describing the error.
     *
     * @return null if this deque is valid, or a description of the error
     */
    private String checkInvariants() {
        if (front == null) {
            return "Unexpected reference: front should reference a sentinel node but was <null>";
        } else if (front.prev != null) {
            return "Unexpected reference: front.prev should be <null> but was <" + front.prev + ">";
        } else if (back == null) {
            return "Unexpected reference: back should reference a sentinel node but was <null>";
        } else if (back.next != null) {
            return "Unexpected reference: back.next should be <null> but was <" + back.next + ">";
        }
        String message = checkNode(front, -1);
        if (message != null) {
            return message;
        }
        int i = 0;
        for (Node<T> curr = front.next; curr != back; curr = curr.next) {
            message = checkNode(curr, i);
            if (message != null) {
                return message;
            }
            i += 1;
        }
        return null;
    }

    /**
     * Returns null if the current node is valid with correct references in both directions.
     * Otherwise, returns a string describing the error.
     *
     * @param node the node to validate
     * @param i the index of the node in this deque
     * @return null if this node is valid, or a description of the error
     */
    private String checkNode(Node<T> node, int i) {
        if (node.next == null) {
            return "Unexpected null reference in node at index " + i + ": <" + node + ">";
        } else if (node.next.prev == node) {
            return null;
        } else if (node.next.prev == null) {
            return "Unexpected null reference in node at index " + (i + 1) + ": <" + node.next + ">";
        } else {
            return "Mismatched references:\n"
                    + "node at index " + i + ": <" + node + ">\n"
                    + "node at index " + (i + 1) + ": <" + node.next + ">";
        }
    }

    /**
     * A doubly-linked node containing a single element.
     *
     * @param <T> the type of element in this node
     */
    private static class Node<T> {
        /**
         * The element data value.
         */
        public final T value;
        /**
         * The previous node in the deque.
         */
        public Node<T> prev;
        /**
         * The next node in the deque.
         */
        public Node<T> next;

        /**
         * Constructs a new node with the given value.
         *
         * @param value the element data value for the new node
         */
        public Node(T value) {
            this(value, null, null);
        }

        /**
         * Constructs a new node with the given value, previous node, and next node.
         *
         * @param value the element data value for the new node
         * @param prev the previous node in the deque, or null
         * @param next the next node in the deque, or null
         */
        public Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    '}';
        }
    }
}
