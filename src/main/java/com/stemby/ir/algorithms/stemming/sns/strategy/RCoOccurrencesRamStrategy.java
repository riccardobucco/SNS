package com.stemby.ir.algorithms.stemming.sns.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.stemby.commons.util.Matrix;
import com.stemby.commons.util.SymmetricSparseMatrix;
import com.stemby.ir.util.Lexicon;

/**
 * A possible implementation of the RCoOccurrencesStrategy interface.
 * This class is based on the
 * {@link com.stemby.ir.algorithms.stemming.sns.strategy.AbstractRCoOccurrencesStrategy}
 * class, and it defines every abstract method. Specifically, this class is
 * designed to keep everything in RAM, without saving data on disk.
 * 
 * @author stemby
 */
public class RCoOccurrencesRamStrategy extends AbstractRCoOccurrencesStrategy {

    /**
     * Class constructor.
     * 
     * @param   minLongestCommonPrefixLength    First parameter of the SNS
     *                                          algorithm.
     * @param   prefixLength                    Second parameter of the SNS
     *                                          algorithm.
     * @param   rcoWeight                       Third parameter of the SNS
     *                                          algorithm.
     */
    public RCoOccurrencesRamStrategy(int minLongestCommonPrefixLength, int prefixLength, float rcoWeight) {
        super(minLongestCommonPrefixLength, prefixLength, rcoWeight);
    }

    /**
     * Method that computes the RCO Matrix starting from the CO Matrix
     * and the {@link com.stemby.ir.util.Lexicon Lexicon} object.
     * 
     * @param   coOccurrences   The CO Matrix.
     * @param   lexicon         Lexicon which contains all the terms whose
     *                          co-occurrences are in the CO Matrix.
     * @return                  The RCO Matrix.
     */
    public Matrix getRCoOccurrences(Matrix coOccurrences, Lexicon lexicon) {
        Map<String, Integer> suffixesOfCoOccurringTerms = getSuffixesOfCooccurringTerms(coOccurrences, lexicon);
        int size = coOccurrences.getRowCount();
        Matrix rcoMatrix = new SymmetricSparseMatrix(size);
        Iterable<long[]> nonZeroCoordinates = coOccurrences.nonZeroCoordinates();
        Iterator<long[]> it = nonZeroCoordinates.iterator();
        while (it.hasNext()) {
            long[] coordinates = it.next();
            int termIdA = (int)coordinates[0];
            int termIdB = (int)coordinates[1];
            if (termIdB > termIdA) {
                String termA = lexicon.getLexeme(termIdA);
                String termB = lexicon.getLexeme(termIdB);
                if (prefixesAreEqual(termA, termB) && !suffixesAreBothUnique(termA, termB, suffixesOfCoOccurringTerms)) {
                    int value = coOccurrences.getAsInt(termIdA, termIdB);
                    rcoMatrix.setAsFloat(termIdA, termIdB, value);
                    for (int w = 0; w < size; w++) {
                        float value1 = coOccurrences.getAsFloat(termIdA, w);
                        float value2 = coOccurrences.getAsFloat(w, termIdB);
                        if ((value1 > 0) && (value2 > 0)) {
                            float oldValue = rcoMatrix.getAsFloat(termIdA, termIdB);
                            float newValue = oldValue + (Math.min(value1, value2) * getRcoWeight());
                            rcoMatrix.setAsFloat(termIdA, termIdB, newValue);
                        }
                    }
                }
            }
        }
        return rcoMatrix;
    }

}