package com.stemby.ir.algorithms.stemming.sns.strategy;

import com.stemby.commons.util.Matrix;
import com.stemby.ir.util.Inverted;

/**
 * Root interface that represents a strategy for computing co-occurrences of
 * some given terms (i.e. the CO Matrix).
 * 
 * @author stemby
 */
public interface CoOccurrencesStrategy {

    /**
     * Method that compute co-occurrences of terms in the inverted index.
     * 
     * @param   inverted    Inverted index which contains terms whose
     *                      co-occurrences need to be computed.
     * @return              The CO Matrix.
     */
    Matrix getTermsCoOccurrences(Inverted inverted);

}