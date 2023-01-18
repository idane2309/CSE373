package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;

    }

    public boolean contains(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != 0;
    }
    public int get(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(overallRoot, key, 0);
        if (x == null) return 0;
        return x.data;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, CharSequence key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.data)    {          return get(x.left,  key, d);}
        else if (c > x.data)       {       return get(x.right, key, d);}
        else if (d < key.length() - 1)  {return get(x.mid,   key, d+1);}
        return x;
    }

    //Add string
    public void put(CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) {
            overallRoot = put(overallRoot, key, 0);
        }
    }

    //ADAPTED
    private Node put(Node x, CharSequence key, int d) {
        char nodeData = key.charAt(d);
        if (x == null) {
            x = new Node(nodeData);
        }
        if      (nodeData < x.data)      {         x.left  = put(x.left,  key,  d);}
        else if (nodeData > x.data)  {             x.right = put(x.right, key,  d);}
        else if (d < key.length() - 1) {  x.mid   = put(x.mid,   key, d+1);}
        else if (d == key.length() - 1) { x.isTerm = true; }
        return x;
    }






    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        List<CharSequence> Strings = new ArrayList<>(terms);
        for(CharSequence term : Strings) {
            put(term);
        }
    }

    private void collect (Node x, List<CharSequence> strings, StringBuilder sb) {
        if (x != null) {
            StringBuilder sbnew = new StringBuilder(sb.toString()); // sbnew holds
            sbnew.append(x.data);                                   // current node's character

            if (x.isTerm) {
                strings.add(sbnew);
            }

            // old sb left and right, new sb down middle
            collect(x.left, strings, sb);
            collect(x.mid, strings, sbnew);
            collect(x.right, strings, sb);
            }
        }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        if (prefix.length() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<CharSequence> strings = new ArrayList<>();
        Node x = get(overallRoot, prefix, 0);
        if(x != null) {
            sb.append(prefix);
            if (x.isTerm) {
                strings.add(sb);
            }
            if (x.mid != null) {
                collect(x.mid, strings, sb);
            }
        }
            return strings;

    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
