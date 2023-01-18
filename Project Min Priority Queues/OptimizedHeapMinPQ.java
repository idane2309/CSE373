package minpq;

import jakarta.annotation.Priority;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items;
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
        PriorityNode<T> root = new PriorityNode<T>( null, -1.0);  //SET A NULL VALUE IN THE 0 POSITION.
        items.add(root);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        } else {
            PriorityNode<T> newItem = new PriorityNode<T>(item, priority);
            items.add(newItem);  //Adds new item into the end of our array list
            itemToIndex.put(item, size());  //Add new item into Hash Map with the index of end of array list.
            if (checkSwim(size())) {  //Checks if we need to swim the new item in its position
                swim(size());
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return itemToIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(1).item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        T min = peekMin();
        items.set(1, items.get( size())); //SWAP THE LAST ITEM IN THE ARRAY INTO THE ROOT NODE.
        PriorityNode<T> newRoot = items.get(1);
        if (size() == 1) {  //FOR THE CASE WHEN THERE IS ONLY 1 ITEM REMAINING.
                itemToIndex.remove(min);
                items.remove(1);
        } else {
                items.remove(size());
                itemToIndex.remove(min);
                itemToIndex.replace(newRoot.item(), 1);
                sink(1);
        }

        return min;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int itemIndex = itemToIndex.get(item);
        double ogPriority = items.get(itemIndex).priority();
        items.get(itemIndex).setPriority(priority);
        if (priority < ogPriority) {                    //IF NEW PRIORITY IS SMALLER, THEN WE WANT TO SWIM THE NODE
            swim(itemIndex);
        } else {
            sink(itemIndex);                            //IF NEW PRIORITY IS BIGGER, THEN WE WANT TO SINK THE NODE
        }
    }

    @Override
    public int size() {
        return items.size() - 1;
    }

    private void sink(int index) {
        PriorityNode<T> store = items.get(index); //STORE OUR PARENT NODE TO SWAP
        if (!(checkSinkLeft(index)) & !(checkSinkRight(index))) {  //BASE CASE OF CORRECT POSITION
            itemToIndex.replace(store.item(), index);
        } else {
            int lcPosition = 2 * index;
            int rcPosition = 2 * index + 1;
            if(checkSinkLeft(index) & checkSinkRight(index)) {  //IF BOTH CHILD NODES NEED A SWAP
                double rightPriority = items.get(rcPosition).priority();
                double leftPriority = items.get(lcPosition).priority();
                if( leftPriority < rightPriority) {   //LEFT CHILD IS SMALLER
                    itemToIndex.replace(store.item(), lcPosition ); //UPDATE PARENT NODE HASH MAP TO NEW INDEX
                    itemToIndex.replace(items.get(lcPosition).item(), index); //UPDATE THE LEFT CHILD NODE HASH MAP TO NEW INDEX
                    items.set(index, items.get(lcPosition)); //SETS THE PARENT NODE TO THE LEFT CHILD NODE
                    items.set(lcPosition, store);            //SETS THE LEFT CHILD NODE TO THE PARENT NODE
                    sink( lcPosition);
                } else {                              //RIGHT CHILD IS SMALLER
                    itemToIndex.replace(store.item(), rcPosition); //UPDATE PARENT NODE HASH MAP TO NEW INDEX
                    itemToIndex.replace(items.get(rcPosition).item(), index); //UPDATE THE RIGHT CHILD NODE HASH MAP TO NEW INDEX
                    items.set(index, items.get(rcPosition)); //SETS THE PARENT NODE TO THE RIGHT CHILD NODE
                    items.set(rcPosition, store);            //SETS THE RIGHT CHILD NODE TO THE PARENT NODE
                    sink(rcPosition);
                }
            } else if (checkSinkLeft(index)) {
                itemToIndex.replace(store.item(), lcPosition ); //UPDATE PARENT NODE HASH MAP TO NEW INDEX
                itemToIndex.replace(items.get(lcPosition).item(), index); //UPDATE THE LEFT CHILD NODE HASH MAP TO NEW INDEX
                items.set(index, items.get(lcPosition)); //SETS THE PARENT NODE TO THE LEFT CHILD NODE
                items.set(lcPosition, store);            //SETS THE LEFT CHILD NODE TO THE PARENT NODE
                sink( lcPosition);
            } else if (checkSinkRight(index)) {
                itemToIndex.replace(store.item(), rcPosition); //UPDATE PARENT NODE HASH MAP TO NEW INDEX
                itemToIndex.replace(items.get(rcPosition).item(), index); //UPDATE THE RIGHT CHILD NODE HASH MAP TO NEW INDEX
                items.set(index, items.get(rcPosition)); //SETS THE PARENT NODE TO THE RIGHT CHILD NODE
                items.set(rcPosition, store);            //SETS THE RIGHT CHILD NODE TO THE PARENT NODE
                sink(rcPosition);
            }
        }

    }

    private void swim(int index) {
        PriorityNode<T> store = items.get(index);
        int parentPosition = index / 2;
        if(!(checkSwim(index))) {
            itemToIndex.replace(store.item(), index);
        } else {

            itemToIndex.replace(store.item(), parentPosition); //UPDATE THE HASH MAP AND SWAP CHILD POSITION WITH PARENT POSITION (CHILD SWAP)
            itemToIndex.replace(items.get(parentPosition).item(), index); //UPDATE THE HASH MAP AND SWAP PARENT POSITION WITH CHILD POSITION (PARENT SWAP)
            items.set(index, items.get(parentPosition)); //SET OUR CURRENT CHILD NODE IN ARRAYLIST TO THE PARENT NODE (1).
            items.set( parentPosition, store ); //SET THE PARENT NODE IN THE ARRAYLIST TO OUR CURRENT CHILD NODE (2)   SWAP IS NOW DONE.
            swim(parentPosition); //NOW RECURSION SWIM THE CHILD NODE TO KEEP MOVING IT UP.
        }
    }

    private boolean checkSinkLeft(int index) {
        double currPriority = items.get(index).priority();
        int lcPosition = 2 * index;
        if (lcPosition > size()) {
            return false;
        } else if (items.get(lcPosition).priority() < currPriority) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSinkRight(int index) {
        double currPriority = items.get(index).priority();
        int rcPosition = 2 * index + 1;
        if (rcPosition > size()) {
            return false;
        } else if (items.get(rcPosition).priority() < currPriority) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSwim(int index) {
        if (index == 1) {
            return false;
        }
        int parentPosition = index / 2;
        double currPriority = items.get(index).priority();
        double parentPriority = items.get(parentPosition).priority();
        if (currPriority < parentPriority) {
            return true;
        } else {
            return false;
        }
    }

}
