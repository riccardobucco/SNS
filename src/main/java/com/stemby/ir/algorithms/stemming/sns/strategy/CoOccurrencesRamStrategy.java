package com.stemby.ir.algorithms.stemming.sns.strategy;

import java.util.List;

import com.stemby.commons.util.Matrix;
import com.stemby.commons.util.SymmetricSparseMatrix;
import com.stemby.ir.util.Inverted;

/**
 * Class that implements the
 * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy}
 * interface. It represents a strategy for computing co-occurrences of
 * some given terms (i.e. the CO Matrix). It is designed to keep everything in
 * RAM.
 * 
 * @author stemby
 */
public class CoOccurrencesRamStrategy implements CoOccurrencesStrategy {

    /**
     * Method that compute co-occurrences of terms in the inverted index.
     * It keeps everything in RAM, without memorizing anything on disk.
     * 
     * @param   inverted    Inverted index which contains terms whose
     *                      co-occurrences need to be computed.
     * @return              The CO Matrix.
     */
    public Matrix getTermsCoOccurrences(Inverted inverted) {
        int size = inverted.getTermsNumber();
        Matrix coOccurrencesMatrix = new SymmetricSparseMatrix(size);
        for (int termIdA = 0; termIdA < size - 1; termIdA++) {
            for (int termIdB = termIdA + 1; termIdB < size; termIdB++) {
                List<int[]> indexesOfCommonDocuments = inverted.getIndexesOfCommonDocuments(termIdA, termIdB);
                for (int i = 0; i < indexesOfCommonDocuments.size(); i++) {
                    int docIndexA = indexesOfCommonDocuments.get(i)[0];
                    int docIndexB = indexesOfCommonDocuments.get(i)[1];
                    int frequencyA = inverted.getTermFrequencyInDocument(termIdA, docIndexA);
                    int frequencyB = inverted.getTermFrequencyInDocument(termIdB, docIndexB);
                    int oldValue = coOccurrencesMatrix.getAsInt(termIdA, termIdB);
                    int newValue = oldValue + Math.min(frequencyA, frequencyB);
                    coOccurrencesMatrix.setAsInt(termIdA, termIdB, newValue);
                }
            }
        }
        return coOccurrencesMatrix;
    }

}