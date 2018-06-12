package com.stemby.ir.util;

import java.util.List;

/**
 * Root interface that represents an inverted index.
 * 
 * @author stemby
 */
public interface Inverted {

    /**
     * It adds a new posting list to the inverted index for the term with the
     * given identifier.
     * 
     * @param   termId  Identifier of the term (i.e. of the new posting list).
     */
    void addTerm(int termId);

    /**
     * It sets the frequency of a term in a document.
     * 
     * @param   termId      The identifier of the term (i.e. the idenfitier of
     *                      the posting list).
     * @param   frequency   The number or times the given term is used in the
     *                      given document.
     * @param   docId       The identifier of the document.
     */
    void setTermFrequencyInDocument(int termId, int frequency, int docId);
    
    /**
     * It returns the frequency of a term in a document.
     * 
     * @param   termId      The identifier of the term (i.e. the idenfitier of
     *                      the posting list).
     * @param   docIndex    The position of the document in the posting list.
     * @return              The frequency of a term in a document.
     */
    int getTermFrequencyInDocument(int termId, int docIndex);
    
    /**
     * It returns the length of the posting list of a term.
     * 
     * @param   termId  The identifier of the term (i.e. the identifier of
     *                  the posting list).
     * @return          The length of the posting list of a term.
     */
    int getTermDocumentsNumber(int termId);

    /**
     * It return how many terms are in the inverted index (i.e. how many
     * posting lists are in the inverted index).
     * 
     * @return The number of posting lists in the inverted index.
     */
    int getTermsNumber();

    /**
     * It returns a list of the common documents of two posting lists. Each
     * element of the list is an array of two integers: they both represent
     * the position in which it is possible to find a document in the
     * relating posting list.
     * 
     * @param   termIdA The identifier of the first posting list.
     * @param   termIdB The identifier of the second posting list.
     * @return          A list of positions in which it is possible to find
     *                  common documents in both the posting lists.
     */
    List<int[]> getIndexesOfCommonDocuments(int termIdA, int termIdB);

}