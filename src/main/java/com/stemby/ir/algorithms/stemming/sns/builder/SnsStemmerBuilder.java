package com.stemby.ir.algorithms.stemming.sns.builder;

import com.stemby.ir.algorithms.stemming.sns.SnsStemmer;
import com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy;
import com.stemby.ir.util.Inverted;
import com.stemby.ir.util.Lexicon;

/**
 * Root interface that allows to build a
 * {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer} object.
 * 
 * @author stemby
 */
public interface SnsStemmerBuilder {

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Lexicon Lexicon}
     * object to the Stemmer.
     * 
     * @param   lexicon {@link com.stemby.ir.util.Lexicon Lexicon} object to be
     *                  assigned to the Stemmer.
     * @return          This object.
     */
    SnsStemmerBuilder setLexicon(Lexicon lexicon);

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Inverted Inverted}
     * object to the Stemmer.
     * 
     * @param   inverted    {@link com.stemby.ir.util.Inverted Inverted} object
     *                      to be assigned to the Stemmer.
     * @return              This object.
     */
    SnsStemmerBuilder setInverted(Inverted inverted);

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   coOccurrencesStrategy   {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     * @return                          This object.
     */
    SnsStemmerBuilder setCoOccurrencesStrategy(CoOccurrencesStrategy coOccurrencesStrategy);

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   rCoOccurrencesStrategy  {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     * @return                          This object.
     */
    SnsStemmerBuilder setRCoOccurrencesStrategy(RCoOccurrencesStrategy rCoOccurrencesStrategy);

    /**
     * Method that build a
     * {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer SnsStemmer}
     * object using the parameters setted by the user.
     * 
     * @return  A {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer SnsStemmer}
     *          object, built using the parameters setted by the user.
     */
    SnsStemmer build();
    
}