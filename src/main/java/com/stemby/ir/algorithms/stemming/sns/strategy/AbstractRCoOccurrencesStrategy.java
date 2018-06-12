package com.stemby.ir.algorithms.stemming.sns.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.stemby.commons.algorithms.Strings;
import com.stemby.commons.util.Matrix;
import com.stemby.ir.util.Lexicon;

/**
 * This class provides a skeletal implementation of the RCoOccurrencesStrategy
 * interface to minimize the effort required to implement it.
 * 
 * @author stemby
 */
public abstract class AbstractRCoOccurrencesStrategy implements RCoOccurrencesStrategy {

    private int minLongestCommonPrefixLength;
    private int prefixLength;
    private float rcoWeight;

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
    public AbstractRCoOccurrencesStrategy(int minLongestCommonPrefixLength, int prefixLength, float rcoWeight) {
        this.minLongestCommonPrefixLength = minLongestCommonPrefixLength;
        this.prefixLength = prefixLength;
        this.rcoWeight = rcoWeight;
    }

    /**
     * Get method for providing access to the minLongestCommonPrefixLength
     * parameter holded by this object.
     * 
     * @return  The minLongestCommonPrefixLength parameter holded by this
     *          object.
     */
    public int getMinLongestCommonPrefixLength() {
        return minLongestCommonPrefixLength;
    }

    /**
     * Get method for providing access to the prefixLength parameter holded by
     * this object.
     * 
     * @return  The prefixLength parameter holded by this object.
     */
    public int getPrefixLength() {
        return prefixLength;
    }

    /**
     * Get method for providing access to the rcoWeight parameter holded by
     * this object.
     * 
     * @return  The rcoWeight parameter holded by this object.
     */
    public float getRcoWeight() {
        return rcoWeight;
    }

    /**
     * Abstract method that computes the RCO Matrix starting from the CO Matrix
     * and the {@link com.stemby.ir.util.Lexicon Lexicon} object (concrete
     * subclasses have to implement it).
     * 
     * @param   coOccurrences   The CO Matrix.
     * @param   lexicon         Lexicon which contains all the terms whose
     *                          co-occurrences are in the CO Matrix.
     * @return                  The RCO Matrix.
     */
    public abstract Matrix getRCoOccurrences(Matrix coOccurrences, Lexicon lexicon);

    /**
     * This method creates a map with all the suffixes of co-occurring terms.
     * Specifically, it associates to each suffix a number that represents
     * how many times one of two co-occurring terms has that specific suffix.
     * This method can be used only by subclasses.
     * 
     * @param   coOccurrences   The CO Matrix.
     * @param   lexicon         The Lexicon object associated to the CO Matrix.
     * @return                  A map with all the suffixes of co-occurring
     *                          terms. 
     */
    protected Map<String, Integer> getSuffixesOfCooccurringTerms(Matrix coOccurrences, Lexicon lexicon) {
        Map<String, Integer> suffixesOfCoOccurringTerms = new HashMap<>();
        Iterable<long[]> nonZeroCoordinates = coOccurrences.nonZeroCoordinates();
        Iterator<long[]> it = nonZeroCoordinates.iterator();
        while (it.hasNext()) {
            long[] coordinates = it.next();
            int termIdA = (int)coordinates[0];
            int termIdB = (int)coordinates[1];
            if (termIdB > termIdA) {
                String termA = lexicon.getLexeme(termIdA);
                String termB = lexicon.getLexeme(termIdB);
                int longestCommonPrefixLength = Strings.getLongestCommonPrefixLength(termA, termB);
                if (longestCommonPrefixLength >= minLongestCommonPrefixLength) {
                    String suffixA = termA.substring(longestCommonPrefixLength);
                    String suffixB = termB.substring(longestCommonPrefixLength);
                    addSuffix(suffixesOfCoOccurringTerms, suffixA);
                    addSuffix(suffixesOfCoOccurringTerms, suffixB);
                }
            }
        }
        return suffixesOfCoOccurringTerms;
    }

    /**
     * It updates a given map (that contains how many times each suffix can be
     * found), adding a new suffix. If the suffix is already in the map, then
     * the method increments the value; if the suffix is not in the map yet,
     * then it is added to the map. The method can be used only by subclasses.
     * 
     * @param   suffixesOfCoOccurringTerms  A map that assigns a number to
     *                                      each suffix (the number represents
     *                                      how many times it's possible to
     *                                      find the given suffix).
     * @param   suffix                      A new suffix to be added to the
     *                                      map.
     */
    protected void addSuffix(Map<String, Integer> suffixesOfCoOccurringTerms, String suffix) {
        if (suffix.length() == 0) {
            return;
        }
        if (suffixesOfCoOccurringTerms.containsKey(suffix)) {
            int newValue = suffixesOfCoOccurringTerms.get(suffix).intValue() + 1;
            suffixesOfCoOccurringTerms.put(suffix, newValue);
            return;
        }
        suffixesOfCoOccurringTerms.put(suffix, 1);
    }

    /**
     * It checks whether two terms have a common prefix or not. Prefixes which
     * are shorter than the parameter prefixLength are not taken into account.
     * The method can be used only by subclasses.
     * 
     * @param   termA   The first string to compare.
     * @param   termB   The second string to compare.
     * @return          The method returns true if the two terms have a common
     *                  prefix with a minimum length.
     */
    protected boolean prefixesAreEqual(String termA, String termB) {
        String prefixA = termA.substring(0, Math.min(termA.length(), prefixLength));
        String prefixB = termB.substring(0, Math.min(termB.length(), prefixLength));
        return prefixA.equals(prefixB);
    }

    /**
     * It works with the suffixes of two terms. These suffixes are obtained
     * removing the longest common prefix. The method checks whether these
     * suffixes are the suffixes of some other terms. This method can be used
     * only by subclasses.
     * 
     * @param   termA                       The first term.
     * @param   termB                       The second term.
     * @param   suffixesOfCoOccurringTerms  A map that assigns a number to
     *                                      each suffix (the number represents
     *                                      how many times it's possible to
     *                                      find the given suffix).
     * @return                              It returns true if both the
     *                                      suffixes are unique, i.e. they are
     *                                      not in the map.
     */
    protected boolean suffixesAreBothUnique(String termA, String termB, Map<String, Integer> suffixesOfCoOccurringTerms) {
        int longestCommonPrefixLength = Strings.getLongestCommonPrefixLength(termA, termB);
        String suffixA = termA.substring(longestCommonPrefixLength);
        String suffixB = termB.substring(longestCommonPrefixLength);
        if (suffixesOfCoOccurringTerms.containsKey(suffixA) && suffixesOfCoOccurringTerms.containsKey(suffixB)) {
            if ((suffixesOfCoOccurringTerms.get(suffixA).intValue() > 1) && (suffixesOfCoOccurringTerms.get(suffixB).intValue() > 1)) {
                return false;
            }
        }
        return true;
    }

}