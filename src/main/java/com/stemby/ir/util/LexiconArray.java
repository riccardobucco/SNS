package com.stemby.ir.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stemby.ir.util.Lexicon;

/**
 * Array implementation of the {@link com.stemby.ir.util.Lexicon Lexicon}
 * interface.
 * 
 * @author stemby
 */
public class LexiconArray implements Lexicon {

    private final List<String> terms = new ArrayList<>();

    /**
     * It adds a new lexeme to the lexicon
     * 
     * @param   termId  Identifier of the new lexeme.
     * @param   term    Term to be added to the lexicon.
     */
    public void addLexeme(int termId, String term) {
        terms.add(termId, term);
    }

    /**
     * It returns the lexeme with the given identifier.
     * 
     * @param   termId  Identifier of the lexeme.
     * @return          The lexeme with the given identifier.
     */
    public String getLexeme(int termId) {
        return terms.get(termId);
    }

    /**
     * It returns the size of the lexicon (i.e. how many terms are in the
     * lexicon).
     * 
     * @return The number of terms in the lexicon.
     */
    public int size() {
        return terms.size();
    }

    /**
     * Returns an iterator over the terms of the lexicon.
     * 
     * @return An iterator over the terms of the lexicon.
     */
    public Iterator<String> iterator() {
        return terms.iterator();
    }

}