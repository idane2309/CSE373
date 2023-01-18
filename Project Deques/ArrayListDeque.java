package deques;
import java.util.ArrayList;

/**
 * An arraylist implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class ArrayListDeque<T> implements Deque<T> {

    private final ArrayList<T> list;


    private int size;

    public ArrayListDeque() {
         list = new ArrayList<T>();
         size = 0;
    }

    /**
     * Adds the given item to the front of this deque.
     *
     * @param item the element to add
     */
    public void addFirst(T item) {
        list.add(0,item);
        size += 1;
    }

    /**
     * Adds the given item to the back of this deque.
     *
     * @param item the element to add
     */
    public void addLast(T item) {
        list.add(list.size(), item);
        size += 1;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     *
     * @param index the index to get
     * @return the element at the given index
     */
    public T get(int index) {
        if(list.size() == 0) {
            return null;
        } else {
            return list.get(index);
        }
    }


    /**
     * Returns the number of items in this deque.
     *
     * @return the number of items in this deque
     */
    public int size() {
        return size;
    }

    /**
     * Removes and returns the item at the front of this deque. Returns null if the deque is empty.
     *
     * @return the item at the front of this deque, or null if the deque is empty
     */
    public T removeFirst() {
        if(list.size() == 0) {
            return null;
        } else {
            size -= 1;
            return list.remove(0);

        }
    }

    /**
     * Removes and returns the item at the back of this deque. Returns null if the deque is empty.
     *
     * @return the item at the back of this deque, or null if the deque is empty
     */
    public T removeLast() {
        if(list.size() == 0) {
            return null;
        } else {
            size -= 1;
            return list.remove(list.size()-1);

        }
    }
}
