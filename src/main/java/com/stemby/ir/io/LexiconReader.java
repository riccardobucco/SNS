package com.stemby.ir.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;

import com.stemby.ir.util.Lexicon;
import com.stemby.ir.util.LexiconArray;

/**
 * Class that is responsible for building a
 * {@link com.stemby.ir.util.Lexicon Lexicon} object, reading relating data
 * from a file on a specified path. The file has a line for each posting list.
 * The format of a line is 'term,uselessStuff'.
 * <ul>
 *   <li> term is a word, composed by numbers or characters
 *   <li> uselessStuff can be anything, it is ignored by the reader
 * </ul>
 * 
 * @author stemby
 */
public class LexiconReader {

    private Path path;
    private Charset charset;

    /**
     * Class constructor.
     * 
     * @param   path    The path where the file containing data of the lexicon
     *                  can be found.
     * @param   charset Charset to use while reading the file.
     */
    public LexiconReader(Path path, Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    /**
     * It reads the file containing data of the lexicon, it build a new
     * {@link com.stemby.ir.util.Lexicon Lexicon} object and it returns it.
     * 
     * @return A {@link com.stemby.ir.util.Lexicon Lexicon} object containing
     * data of the lexicon file.
     */
    public Lexicon read() {
        Lexicon lexicon = new LexiconArray();
        
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            int termId = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lexicon.addLexeme(termId, line.split(",")[0]);
                termId++;
            }
        }
        catch (IOException x) {
            System.err.format("IOException in LexiconReader: %s%n", x);
            return null;
        }

        return lexicon;
    }

}