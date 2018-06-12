package com.stemby;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.stemby.ir.algorithms.stemming.sns.builder.SnsStemmerBuilder;
import com.stemby.ir.algorithms.stemming.sns.builder.SnsStemmerBuilderImpl;
import com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesRamStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesRamStrategy;
import com.stemby.ir.algorithms.stemming.sns.strategy.RCoOccurrencesStrategy;
import com.stemby.ir.algorithms.stemming.Stemmer;
import com.stemby.ir.io.InvertedReader;
import com.stemby.ir.io.LexiconReader;
import com.stemby.ir.util.Lexicon;
import com.stemby.ir.util.Inverted;

public class App {

    private static final Properties config = new Properties();

    static {
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            config.load(input);
        }
        catch (IOException x) {
            System.err.format("IOException in App: %s%n", x);
        }
    }

    /*
        It creates a new file and prints to it a word and its stem on each line.
    */

    private static void printStemsOnFile(Map<String, String> stems) {
        Path outputPath = Paths.get(config.getProperty("outputPath"));
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = new ArrayList<>();
        Set<String> words = stems.keySet();
        Iterator<String> iteratorOverWords = words.iterator();
        while (iteratorOverWords.hasNext()) {
            String word = iteratorOverWords.next();
            String line = word + "\t" + stems.get(word);
            lines.add(line);
        }
        try {
            Files.write(outputPath, lines, charset);
        }
        catch (IOException e) {
            System.err.format("IOException in App: %s%n", e);
        }
    }

    public static void main(String[] args) {
        Path lexiconPath = Paths.get(config.getProperty("lexiconPath"));
        Path invertedPath = Paths.get(config.getProperty("invertedPath"));
        int minLongestCommonPrefixLength = Integer.parseInt(config.getProperty("minLongestCommonPrefixLength"));
        int prefixLength = Integer.parseInt(config.getProperty("prefixLength"));
        float rcoWeight = Float.parseFloat(config.getProperty("rcoWeight"));

        Charset charset = Charset.forName("UTF-8");
        LexiconReader lexiconReader = new LexiconReader(lexiconPath, charset);
        InvertedReader invertedReader = new InvertedReader(invertedPath, charset);
        Lexicon lexicon = lexiconReader.read();
        Inverted inverted = invertedReader.read();

        SnsStemmerBuilder snsStemmerBuilder = new SnsStemmerBuilderImpl();
        CoOccurrencesStrategy coOccurrencesStrategy = new CoOccurrencesRamStrategy();
        RCoOccurrencesStrategy rCoOccurrencesStrategy = new RCoOccurrencesRamStrategy(minLongestCommonPrefixLength, prefixLength, rcoWeight);
        Stemmer snsStemmer = snsStemmerBuilder
            .setLexicon(lexicon)
            .setInverted(inverted)
            .setCoOccurrencesStrategy(coOccurrencesStrategy)
            .setRCoOccurrencesStrategy(rCoOccurrencesStrategy)
            .build();

        Map<String, String> stems = snsStemmer.getStems();

        printStemsOnFile(stems);
    }

}
