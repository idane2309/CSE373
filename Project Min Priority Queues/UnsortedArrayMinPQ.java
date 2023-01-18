package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class UnsortedArrayMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the item-priority pairs in no specific order.
     */
    private final List<PriorityNode<T>> items;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        items = new ArrayList<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        items.add(new PriorityNode<T>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        PriorityNode <T> contains;
        for (int i = 0; i < items.size(); i++) {
           contains = items.get(i);
           if (contains.item().equals(item)) {
               return true;
           }
       }
       return false;
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> peekMin = items.get(0);
        PriorityNode<T> temp;
        for(int i = 1; i < items.size(); i++) {
           temp = items.get(i);
           if (temp.priority() < peekMin.priority()) {
               peekMin = temp;
           }
        }
        return peekMin.item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        int i = 0;
        PriorityNode<T> temp = items.get(i);
        while (!(temp.item().equals(peekMin()))) {
            i++;
            temp = items.get(i);
        }
        items.remove(temp);
        return temp.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        PriorityNode<T> temp;
        for (int i = 0; i < items.size(); i++) {
            temp = items.get(i);
            if (temp.item().equals(item)) {
                items.get(i).setPriority(priority);
            }
        }
    }

    @Override
    public int size() {
        return items.size();
    }
}
