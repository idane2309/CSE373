package deques;

/**
 * An array implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class ArrayDeque<T> implements Deque<T> {
    /**
     * The underlying array of elements stored in this deque.
     */
    private T[] data;
    /**
     * The index for the next element to be inserted by addFirst.
     */
    private int front;
    /**
     * The index for the next element to be inserted by addLast.
     */
    private int back;
    /**
     * The number of elements in this deque.
     */
    private int size;

    /**
     * Constructs an empty deque.
     */
    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        data = (T[]) new Object[8];
        front = 0;
        back = 1;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[front] = item;
        front = decrement(front, data.length);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == data.length) {  //changed from size == data.length --> back == data.length-1
            resize(data.length * 2);
        }
        data[back] = item;
        back = increment(back, data.length);
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        front = increment(front, data.length);
        T result = data[front];
        data[front] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        back = decrement(back, data.length);
        T result = data[back];
        data[back] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    @Override
    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        int place = front + 1 + index;
        return data[place % data.length];
    }

    @Override
    public String toString() {
        // StringBuilder concatenates strings more efficiently than += in a loop
        StringBuilder output = new StringBuilder();
        if (size >= 0) {
            int counter = 0;
            int i = increment(front, data.length);
            while (counter < size) {
                output.append(data[i]).append(" ");
                i = increment(i, data.length);
                counter += 1;
            }
        }
        return output.toString();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Increments the given index i, wrapping back to 0 if it is equal to length - 1.
     *
     * @param i the given index
     * @param length the wrap limit
     * @return the incremented index
     */
    private static int increment(int i, int length) {
        if (i == length -1) {
            return 0;
        }
        return i + 1;
    }

    /**
     * Decrements the given index i, wrapping back to length - 1 if it is equal to 0.
     *
     * @param i the given index
     * @param length the wrap limit
     * @return the decremented index
     */
    private static int decrement(int i, int length) {
        if (i == 0) {
            return length - 1;
        }
        return i - 1;
    }

    /**
     * Updates the length of the underlying element data array to the given capacity, copying over
     * items as necessary.
     *
     * @param capacity the length of the new element data array
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] newData = (T[]) new Object[capacity];
        int i = increment(front, data.length); //changed size --> data.length

        for (int newIndex = 0; newIndex < size; newIndex += 1) {
            newData[newIndex] = data[i];
            i = increment(i, data.length); // changed size --> data.length
        }

        //Changed front from data.length to newData.length as we have resized and have to
        //accomadate for the new data set length (newData.length -1). I noticed this because my starting front numbers
        //on the Jgrasp visualizer was incorrect as it was not dependent on the new data set but continuing off of
        //the old data array e.g ended on 7, resized and started on 8. Therefore I knew there was something wrong with the resize,
        //especially where front and back were being allocated. Through deductive reasoining, back looked correct as it was correlated
        //to size, therefore it had to be the front variable that was incorrectly set. Here i found that it was being set to
        //data.length which was always 8 regardless of the resize, therefore it had to be changed to the new dataset array
        //to accomadate for the resize function.

        front = newData.length-1;
        back = size;
        data = newData;
    }

    //check code question about deques linked one to fix error Kevin made

    /**
     * Returns true if and only if the underlying element data array needs to be downsized. This
     * helps minimize unused memory when many items are removed from the deque.
     *
     * @return true if an element data downsize is necessary
     */
    private boolean needsDownsize() {
        return ((double) size) / data.length < 0.25 && data.length >= 16;
    }
}
