package com.stemby.commons.algorithms;

import java.util.Collection;

/**
 * Class that includes algorithms on strings. It contains only static methods,
 * so it is not possible to use any constructor.
 * 
 * @author stemby
 */
public class Strings {

    private Strings() {}

    /**
     * It finds the length of the longest common prefix of two strings.
     * 
     * @param   a   The first string in which to search the longest common
     *              prefix.
     * @param   b   The second string in which to search the longest common
     *              prefix.
     * @return      The length of the longest common prefix of two strings.
     */
    public static int getLongestCommonPrefixLength(String a, String b) {
        int minLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return i;
            }
        }
        return minLength;
    }

    /**
     * It finds the length of the longest common prefix of a collection of
     * strings.
     * 
     * @param   stringCollection    The collection of strings in which to
     *                              search the longest common prefix.
     * @return                      The length of the longest common prefix of
     *                              the given collection of strings.
     */
    public static int getLongestCommonPrefixLength(Collection<String> stringCollection) {
        if (stringCollection.isEmpty()) {
            return 0;
        }
        String[] stringArray = stringCollection.toArray(new String[0]);
        return getLongestCommonPrefixLength(stringArray, 0, (stringArray.length - 1));
    }

    private static int getLongestCommonPrefixLength(String[] stringArray, int first, int last) {
        int subArrayLength = last - first + 1;
        if (subArrayLength == 1) {
            return stringArray[first].length();
        }
        if (subArrayLength == 2) {
            return getLongestCommonPrefixLength(stringArray[first], stringArray[last]);
        }
        int firstA = first;
        int lastA = (last + first) / 2;
        int firstB = (last + first) / 2 + 1;
        int lastB = last;
        int longestCommonPrefixLengthA = getLongestCommonPrefixLength(stringArray, firstA, lastA);
        int longestCommonPrefixLengthB = getLongestCommonPrefixLength(stringArray, firstB, lastB);
        String longestCommonPrefixA = stringArray[first].substring(0, longestCommonPrefixLengthA);
        String longestCommonPrefixB = stringArray[last].substring(0, longestCommonPrefixLengthB);
        return getLongestCommonPrefixLength(longestCommonPrefixA, longestCommonPrefixB);
    }

}