package com.stemby.ir.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;

import com.stemby.ir.util.Inverted;
import com.stemby.ir.util.InvertedArray;

/**
 * Class that is responsible for building an
 * {@link com.stemby.ir.util.Inverted Inverted} object, reading relating data
 * from a file on a specified path. The file has a line for each posting list.
 * The format of a line is 'termId postingList'.
 * <ul>
 *   <li> termId is the identifier of the posting list
 *   <li> The format of postingList is
 *        '(docId,frequency) ... (docId,frequency)'
 *   <ul>
 *     <li> docId is the identifier of the document
 *     <li> frequency is the number of times the term represented by termId
 *          appears in the document
 *   </ul>
 * </ul>
 * 
 * @author stemby
 */
public class InvertedReader {

    private Path path;
    private Charset charset;
    
    /**
     * Class constructor.
     * 
     * @param   path    The path where the file containing data of the inverted
     *                  index can be found.
     * @param   charset Charset to use while reading the file.
     */
    public InvertedReader(Path path, Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    /**
     * It reads the file containing data of the inverted index, it build
     * a new {@link com.stemby.ir.util.Inverted Inverted} object and it
     * returns it.
     * 
     * @return An {@link com.stemby.ir.util.Inverted Inverted} object
     * containing data of the inverted index file.
     */
    public Inverted read() {
        Inverted inverted = new InvertedArray();

        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            int termId = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                inverted.addTerm(termId);
                String[] items = line.split(" ");
                for (int i = 1; i < items.length; i++) {
                    String[] values = items[i].substring(1, items[i].length() - 1).split(",");
                    int docId = Integer.parseInt(values[0]);
                    int frequency = Integer.parseInt(values[1]);
                    inverted.setTermFrequencyInDocument(termId, frequency, docId);
                }
                termId++;
            }
        }
        catch (IOException x) {
            System.err.format("IOException in InvertedReader: %s%n", x);
            return null;
        }

        return inverted;
    }

}