package com.stemby.ir.algorithms.stemming.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stemby.commons.algorithms.Strings;
import com.stemby.commons.util.Matrix;
import com.stemby.ir.algorithms.stemming.Stemmer;
import com.stemby.ir.util.Inverted;
import com.stemby.ir.util.Lexicon;

/**
 * This class provides a skeletal implementation of the Stemmer interface to
 * minimize the effort required to implement it. Its main task is to define a
 * program skeleton of the SNS algorithm using the template method pattern.
 * 
 * @author stemby
 */
public abstract class AbstractSnsStemmer implements Stemmer {

    private Lexicon lexicon;
    private Inverted inverted;

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Lexicon Lexicon}
     * object to the Stemmer.
     * 
     * @param   lexicon {@link com.stemby.ir.util.Lexicon Lexicon} object to be
     *                  assigned to the Stemmer.
     */
    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    /**
     * Get method for providing access to the
     * {@link com.stemby.ir.util.Lexicon Lexicon} object holded by the
     * Stemmer.
     * 
     * @return  The {@link com.stemby.ir.util.Lexicon Lexicon} object holded by
     *          the Stemmer.
     */
    public Lexicon getLexicon() {
        return lexicon;
    }

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Inverted Inverted}
     * object to the Stemmer.
     * 
     * @param   inverted    {@link com.stemby.ir.util.Inverted Inverted} object
     *                      to be assigned to the Stemmer.
     */
    public void setInverted(Inverted inverted) {
        this.inverted = inverted;
    }

    /**
     * Get method for providing access to the
     * {@link com.stemby.ir.util.Inverted Inverted} object holded by the
     * Stemmer.
     * 
     * @return  The {@link com.stemby.ir.util.Inverted Inverted} object holded by
     *          the Stemmer.
     */
    public Inverted getInverted() {
        return inverted;
    }

    /**
     * It defines a program skeleton of the SNS algorithm using the template
     * method pattern. Each step of the algorithm can be overridden by
     * subclasses to allow a different implementation while ensuring that the
     * overarching algorithm is still followed. The method is marked as
     * final since subclasses can't override it.
     * 
     * @return  A map that associates each word of the
     *          {@link com.stemby.ir.util.Lexicon Lexicon} object to the
     *          relating stem.
     */
    public final Map<String, String> getStems() {
        Matrix coOccurrences = getTermsCoOccurrences();
        Matrix adjacencyMatrix = getAdjacencyMatrix(coOccurrences);
        List<Set<Integer>> clusters = getClusters(adjacencyMatrix);
        Map<String, String> stems = getStems(clusters);
        return stems;
    }

    /**
     * Abstract method that computes the CO Matrix (concrete subclasses have to
     * implement it). It is marked as protected since only subclasses can use
     * it.
     * 
     * @return  The CO Matrix.
     */
    abstract protected Matrix getTermsCoOccurrences();

    /**
     * Abstract method that computes the RCO Matrix, i.e. the adjacency matrix
     * (concrete subclasses have to implement it). It is marked as protected
     * since only subclasses can use it.
     * 
     * @param   coOccurrences   The CO Matrix.
     * @return                  The RCO Matrix.
     */
    abstract protected Matrix getAdjacencyMatrix(Matrix coOccurrences);

    /**
     * Abstract method that finds the connected components in the RCO Matrix,
     * i.e. the adjacency matrix (concrete subclasses have to implement it).
     * It is marked as protected since only subclasses can use it.
     * 
     * @param   ajacencyMatrix  The RCO Matrix.
     * @return                  A list of connected components (set of
     *                          graph's nodes).
     */
    abstract protected List<Set<Integer>> getClusters(Matrix ajacencyMatrix);

    private Map<String, String> getStems(List<Set<Integer>> clusters) {
        Map<String, String> stems = new HashMap<>();
        Iterator<Set<Integer>> iteratorOverClusters = clusters.iterator();
        while (iteratorOverClusters.hasNext()) {
            Set<Integer> cluster = iteratorOverClusters.next();
            List<String> clusterWords = new ArrayList<>();
            Iterator<Integer> iteratorOverClusterTerms = cluster.iterator();
            while (iteratorOverClusterTerms.hasNext()) {
                int termId = iteratorOverClusterTerms.next();
                String word = lexicon.getLexeme(termId);
                clusterWords.add(word);
            }
            int longestCommonPrefixLength = Strings.getLongestCommonPrefixLength(clusterWords);
            String clusterStem = clusterWords.get(0).substring(0, longestCommonPrefixLength);
            Iterator<String> iteratorOverClusterWords = clusterWords.iterator();
            while (iteratorOverClusterWords.hasNext()) {
                String word = iteratorOverClusterWords.next();
                stems.put(word, clusterStem);
            }
        }
        return stems;
    }
    
}