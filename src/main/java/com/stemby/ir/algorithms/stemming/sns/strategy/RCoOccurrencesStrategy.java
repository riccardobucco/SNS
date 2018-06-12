package com.stemby.ir.algorithms.stemming.sns.strategy;

import com.stemby.commons.util.Matrix;
import com.stemby.ir.util.Lexicon;

/**
 * Root interface that represents a strategy for computing the RCO Matrix.
 * 
 * @author stemby
 */
public interface RCoOccurrencesStrategy {

    /**
     * Method that computes the RCO Matrix starting from the CO Matrix and the
     * {@link com.stemby.ir.util.Lexicon Lexicon} object.
     * 
     * @param   coOccurrences   The CO Matrix.
     * @param   lexicon         Lexicon which contains all the terms whose
     *                          co-occurrences are in the CO Matrix.
     * @return                  The RCO Matrix.
     */
    Matrix getRCoOccurrences(Matrix coOccurrences, Lexicon lexicon);

    /**
     * Get method for providing access to the minLongestCommonPrefixLength
     * parameter holded by this object.
     * 
     * @return  The minLongestCommonPrefixLength parameter holded by this
     *          object.
     */
    int getMinLongestCommonPrefixLength();

    /**
     * Get method for providing access to the prefixLength parameter holded by
     * this object.
     * 
     * @return  The prefixLength parameter holded by this object.
     */
    int getPrefixLength();

    /**
     * Get method for providing access to the rcoWeight parameter holded by
     * this object.
     * 
     * @return  The rcoWeight parameter holded by this object.
     */
    float getRcoWeight();

}