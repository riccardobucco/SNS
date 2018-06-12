package com.stemby.ir.algorithms.stemming;

import java.util.Map;

/**
 * Root interface that represents a Stemmer.
 * 
 * @author stemby
 */
public interface Stemmer {

    Map<String, String> getStems();

}