package com.stemby.ir.algorithms.stemming.sns.builder;

import com.stemby.ir.util.Inverted;
import com.stemby.ir.util.Lexicon;
import com.stemby.ir.algorithms.stemming.sns.SnsStemmer;
import com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy;

/**
 * Class that implements the
 * {@link com.stemby.ir.algorithms.stemming.sns.builder.SnsStemmerBuilder}
 * interface. It allows to build a
 * {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer} object.
 * 
 * @author stemby
 */
public class SnsStemmerBuilderImpl implements SnsStemmerBuilder {

    private SnsStemmer snsStemmer;

    /**
     * Class constructor.
     */
    public SnsStemmerBuilderImpl() {
        this.snsStemmer = new SnsStemmer();
    }

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Lexicon Lexicon}
     * object to the Stemmer.
     * 
     * @param   lexicon {@link com.stemby.ir.util.Lexicon Lexicon} object to be
     *                  assigned to the Stemmer.
     * @return          This object.
     */
    public SnsStemmerBuilder setLexicon(Lexicon lexicon) {
        snsStemmer.setLexicon(lexicon);
        return this;
    }

    /**
     * Set method for assigning a {@link com.stemby.ir.util.Inverted Inverted}
     * object to the Stemmer.
     * 
     * @param   inverted    {@link com.stemby.ir.util.Inverted Inverted} object
     *                      to be assigned to the Stemmer.
     * @return              This object.
     */
    public SnsStemmerBuilder setInverted(Inverted inverted) {
        snsStemmer.setInverted(inverted);
        return this;
    }

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   coOccurrencesStrategy   {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy CoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     * @return                          This object.
     */
    public SnsStemmerBuilder setCoOccurrencesStrategy(CoOccurrencesStrategy coOccurrencesStrategy) {
        snsStemmer.setCoOccurrencesStrategy(coOccurrencesStrategy);
        return this;
    }

    /**
     * Set method for assigning a
     * {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     * object to the Stemmer.
     * 
     * @param   rCoOccurrencesStrategy  {@link com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy RCoOccurrencesStrategy}
     *                                  object to be assigned to the Stemmer.
     * @return                          This object.
     */
    public SnsStemmerBuilder setRCoOccurrencesStrategy(RCoOccurrencesStrategy rCoOccurrencesStrategy) {
        snsStemmer.setRCoOccurrencesStrategy(rCoOccurrencesStrategy);
        return this;
    }

    /**
     * Method that build a
     * {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer SnsStemmer}
     * object using the parameters setted by the user.
     * 
     * @return  A {@link com.stemby.ir.algorithms.stemming.sns.SnsStemmer SnsStemmer}
     *          object, built using the parameters setted by the user.
     */
    public SnsStemmer build() {
        return snsStemmer;
    }
    
}