package com.stemby.ir.util;

import java.util.ArrayList;
import java.util.List;

import com.stemby.ir.util.Inverted;

/**
 * Array implementation of the {@link com.stemby.ir.util.Inverted Inverted}
 * interface.
 * 
 * @author stemby
 */
public class InvertedArray implements Inverted {

    private List<Term> terms = new ArrayList<>();

    /**
     * It adds a new posting list to the inverted index for the term with the
     * given identifier.
     * 
     * @param   termId  Identifier of the term (i.e. of the new posting list).
     */
    public void addTerm(int termId) {
        Term term = new Term();
        terms.add(termId, term);
    }

    /**
     * It sets the frequency of a term in a document.
     * 
     * @param   termId      The identifier of the term (i.e. the idenfitier of
     *                      the posting list).
     * @param   frequency   The number or times the given term is used in the
     *                      given document.
     * @param   docId       The identifier of the document.
     */
    public void setTermFrequencyInDocument(int termId, int frequency, int docId) {
        terms.get(termId).setFrequencyInDocument(frequency, docId);
    }
    
    /**
     * It returns the frequency of a term in a document.
     * 
     * @param   termId      The identifier of the term (i.e. the idenfitier of
     *                      the posting list).
     * @param   docIndex    The position of the document in the posting list.
     * @return              The frequency of a term in a document.
     */
    public int getTermFrequencyInDocument(int termId, int docIndex) {
        return terms.get(termId).getFrequencyInDocument(docIndex);
    }
    
    /**
     * It returns the length of the posting list of a term.
     * 
     * @param   termId  The identifier of the term (i.e. the identifier of
     *                  the posting list).
     * @return          The length of the posting list of a term.
     */
    public int getTermDocumentsNumber(int termId) {
        return terms.get(termId).getDocumentsNumber();
    }

    /**
     * It return how many terms are in the inverted index (i.e. how many
     * posting lists are in the inverted index).
     * 
     * @return The number of posting lists in the inverted index.
     */
    public int getTermsNumber() {
        return terms.size();
    }

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
    public List<int[]> getIndexesOfCommonDocuments(int termIdA, int termIdB) {
        List<int[]> indexesOfCommonDocuments = new ArrayList<>();
        int docIndexA = 0;
        int docIndexB = 0;
        int documentsNumberA = getTermDocumentsNumber(termIdA);
        int documentsNumberB = getTermDocumentsNumber(termIdB);
        while (docIndexA < documentsNumberA && docIndexB < documentsNumberB) {
            int docIdA = terms.get(termIdA).getDocumentId(docIndexA);
            int docIdB = terms.get(termIdB).getDocumentId(docIndexB);
            if (docIdA == docIdB) {
                int[] indexesOfCommonDocument = new int[2];
                indexesOfCommonDocument[0] = docIndexA;
                indexesOfCommonDocument[1] = docIndexB;
                indexesOfCommonDocuments.add(indexesOfCommonDocument);
                docIndexA++;
                docIndexB++;
            }
            else
                if (docIdA > docIdB) {
                    docIndexB++;
                }
                else {
                    docIndexA++;
                }
        }
        return indexesOfCommonDocuments;
    }

    private static class Term {

        private List<int[]> occurrences = new ArrayList<>();

        public void setFrequencyInDocument(int frequency, int docId) {
            int[] occurrencesInDocument = new int[2];
            occurrencesInDocument[0] = frequency;
            occurrencesInDocument[1] = docId;
            occurrences.add(occurrencesInDocument);
        }

        public int getFrequencyInDocument(int docIndex) {
            return occurrences.get(docIndex)[0];
        }

        public int getDocumentId(int docIndex) {
            return occurrences.get(docIndex)[1];
        }

        public int getDocumentsNumber() {
            return occurrences.size();
        }

    }

}