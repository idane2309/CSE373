package autocomplete;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;


    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        Collections.sort(this.terms, CharSequence::compare);

    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> results = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return results;
        }
        int i = Collections.binarySearch(terms, prefix, CharSequence::compare);
            if (i >= 0) {
                CharSequence start = terms.get(i);
                boolean match = Autocomplete.isPrefixOf(prefix, start);
                while (match) {
                    results.add(start);
                    i++;
                    start = terms.get(i);
                    match = Autocomplete.isPrefixOf(prefix, start);
                }
            } else {
                CharSequence start = terms.get((-(i + 1)));
                boolean match = Autocomplete.isPrefixOf(prefix, start);
                while (match) {
                    results.add(start);
                    i--;
                    start = terms.get((-(i + 1)));
                    match = Autocomplete.isPrefixOf(prefix, start);

                }

            }

        return results;
    }

}
