package com.stemby.ir.algorithms.stemming.sns;

import java.util.List;
import java.util.Set;

import com.stemby.commons.algorithms.Graph;
import com.stemby.commons.algorithms.Matrices;
import com.stemby.commons.algorithms.Strings;
import com.stemby.commons.util.Matrix;
import com.stemby.commons.util.SymmetricSparseMatrix;
import com.stemby.ir.util.Inverted;
import com.stemby.ir.util.Lexicon;
import com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.sns.AbstractSnsStemmer;

/**
 * A possible implementation of the SNS algorithm. This class is based on the
 * {@link com.stemby.ir.algorithms.stemming.sns.AbstractSnsStemmer AbstractSnsStemmer}
 * class, and it overrides each step of the algorithm defined in it. This class
 * permits also to define some strategies to use depending on the context.
 * 
 * @author stemby
 */
public class SnsStemmer extends AbstractSnsStemmer {

    private CoOccurrencesStrategy coOccurrencesStrategy;
    private RCoOccurrencesStrategy rCoOccurrencesStrategy;

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   coOccurrencesStrategy   {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     */
    public void setCoOccurrencesStrategy(CoOccurrencesStrategy coOccurrencesStrategy) {
        this.coOccurrencesStrategy = coOccurrencesStrategy;
    }

    /**
     * Get method for providing access to the
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     * object holded by the Stemmer.
     * 
     * @return  The {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     *          object holded by the Stemmer.
     */
    public CoOccurrencesStrategy getCoOccurrencesStrategy() {
        return coOccurrencesStrategy;
    }

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   rCoOccurrencesStrategy  {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     */
    public void setRCoOccurrencesStrategy(RCoOccurrencesStrategy rCoOccurrencesStrategy) {
        this.rCoOccurrencesStrategy = rCoOccurrencesStrategy;
    }

    /**
     * Get method for providing access to the
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     * object holded by the Stemmer.
     * 
     * @return  The {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     *          object holded by the Stemmer.
     */
    public RCoOccurrencesStrategy getRCoOccurrencesStrategy() {
        return rCoOccurrencesStrategy;
    }

    /**
     * Method that computes the CO Matrix. It is marked as protected since only
     * subclasses can use it. The behaviour of this method depends on the
     * strategy setted by the user.
     * 
     * @return  The CO Matrix.
     */
    protected Matrix getTermsCoOccurrences() {
        return coOccurrencesStrategy.getTermsCoOccurrences(getInverted());
    }

    /**
     * Method that computes the RCO Matrix, i.e. the adjacency matrix. It is
     * marked as protected since only subclasses can use it. The behaviour of
     * this method depends on the strategy setted by the user.
     * 
     * @param   coOccurrences   The CO Matrix.
     * @return                  The RCO Matrix.
     */
    protected Matrix getAdjacencyMatrix(Matrix coOccurrences) {
        Matrix rcoMatrix = rCoOccurrencesStrategy.getRCoOccurrences(coOccurrences, getLexicon());
        keepOnlyStrongEdges(rcoMatrix);
        return rcoMatrix;
    }

    /**
     * Method that finds the connected components in the RCO Matrix, i.e. the
     * adjacency matrix. It is marked as protected since only subclasses can
     * use it.
     * 
     * @param   adjacencyMatrix The RCO Matrix.
     * @return                  A list of connected components (set of
     *                          graph's nodes).
     */
    protected List<Set<Integer>> getClusters(Matrix adjacencyMatrix) {
        return Graph.getConnectedComponents(adjacencyMatrix);
    }

    private void keepOnlyStrongEdges(Matrix rcoMatrix) {
        int[] indexOfMaxElements = getIndexOfMaxElements(rcoMatrix);
        int size = rcoMatrix.getRowCount();
        for (int termIdA = 0; termIdA < size - 1; termIdA++) {
            for (int termIdB = termIdA + 1; termIdB < size; termIdB++) {
                if ((indexOfMaxElements[termIdA] != termIdB) && (indexOfMaxElements[termIdB] != termIdA)) {
                    rcoMatrix.setAsFloat(termIdA, termIdB, 0);
                    rcoMatrix.setAsFloat(termIdB, termIdA, 0);
                }
            }
        }
    }

    private int[] getIndexOfMaxElements(Matrix rcoMatrix) {
        int rowCount = rcoMatrix.getRowCount();
        int[] indexOfMaxElements = new int[rowCount];
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            indexOfMaxElements[rowIndex] = Matrices.getIndexOfMaxElementOfRow(rcoMatrix, rowIndex);
        }
        return indexOfMaxElements;
    }

}