package com.stemby.ir.util;

import java.lang.Iterable;

/**
 * Root interface that represents a lexicon.
 * 
 * @author stemby
 */
public interface Lexicon extends Iterable<String> {

    /**
     * It adds a new lexeme to the lexicon
     * 
     * @param   termId  Identifier of the new lexeme.
     * @param   term    Term to be added to the lexicon.
     */
    void addLexeme(int termId, String term);

    /**
     * It returns the lexeme with the given identifier.
     * 
     * @param   termId  Identifier of the lexeme.
     * @return          The lexeme with the given identifier.
     */
    String getLexeme(int termId);

    /**
     * It returns the size of the lexicon (i.e. how many terms are in the
     * lexicon).
     * 
     * @return The number of terms in the lexicon.
     */
    int size();

}