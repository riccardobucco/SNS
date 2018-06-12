package com.stemby.ir.algorithms.stemming.sns.strategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.stemby.commons.util.Matrix;
import com.stemby.commons.util.SymmetricSparseMatrix;
import com.stemby.commons.util.SymmetricSparseMatrixOnDisk;
import com.stemby.ir.util.Inverted;

import org.ujmp.core.SparseMatrix;

/**
 * Class that implements the
 * {@link com.stemby.ir.algorithms.stemming.sns.strategy.CoOccurrencesStrategy}
 * interface. It represents a strategy for computing co-occurrences of
 * some given terms (i.e. the CO Matrix). It is designed to keep everything in
 * RAM.
 * <p>
 * THIS CLASS IS NOT STABLE AND IT IS USED FOR EXPERIMENTAL PURPOSE. USE AT
 * YOUR OWN RISK.
 * 
 * @author stemby
 */
public class CoOccurrencesDiskStrategy implements CoOccurrencesStrategy {

    private String base_CO_path;
    private int file_size;

    public CoOccurrencesDiskStrategy(String base_CO_path, int file_size) {
        this.base_CO_path = base_CO_path;
        this.file_size = file_size;
    }

    /**
     * Method that compute co-occurrences of terms in the inverted index.
     * It keeps everything in RAM, without memorizing anything on disk.
     * 
     * @param   inverted    Inverted index which contains terms whose
     *                      co-occurrences need to be computed.
     * @return              The CO Matrix.
     */
    public Matrix getTermsCoOccurrences(Inverted inverted) {
        int n = inverted.getTermsNumber();
        for (int i = 0; i < n; i = i + file_size) {
            int first = i;
            int last = Math.min(i+file_size, n);
            SparseMatrix CO = measureCoOccurranceIndexedByX(inverted, first, last);
            writeCoOccurrance(CO, first, last, base_CO_path + "/indexed_by_x");
            CO.clear();
            CO = null;
            CO = measureCoOccurranceIndexedByY(inverted, first, last);
            writeCoOccurrance(CO, first, last, base_CO_path + "/indexed_by_y");
            CO.clear();
            CO = null;
        }
        SymmetricSparseMatrixOnDisk CO = new SymmetricSparseMatrixOnDisk(base_CO_path, "CO");
        return CO;
    }

    private SparseMatrix measureCoOccurranceIndexedByX(Inverted inverted, int first, int last) {
        int n = inverted.getTermsNumber();
        SparseMatrix CO = SparseMatrix.Factory.zeros(n, n);
        for (int termID_A = first; termID_A < last - 1; termID_A++) {
            for (int termID_B = termID_A + 1; termID_B < n; termID_B++) {
                measureCoOccurranceOfTwoTerms(inverted, CO, termID_A, termID_B);
            }
        }
        return CO;
    }

    private SparseMatrix measureCoOccurranceIndexedByY(Inverted inverted, int first, int last) {
        int n = inverted.getTermsNumber();
        SparseMatrix CO = SparseMatrix.Factory.zeros(n, n);
        for (int termID_A = 0; termID_A < n; termID_A++) {
            for (int termID_B = first; termID_B < last; termID_B++) {
                measureCoOccurranceOfTwoTerms(inverted, CO, termID_A, termID_B);
            }
        }
	
        return CO;
    }

    private void measureCoOccurranceOfTwoTerms(Inverted inverted, SparseMatrix CO, int termIdA, int termIdB) {
        List<int[]> indexesOfCommonDocuments = inverted.getIndexesOfCommonDocuments(termIdA, termIdB);
        for (int i = 0; i < indexesOfCommonDocuments.size(); i++) {
            int docIndexA = indexesOfCommonDocuments.get(i)[0];
            int docIndexB = indexesOfCommonDocuments.get(i)[1];
            int frequencyA = inverted.getTermFrequencyInDocument(termIdA, docIndexA);
            int frequencyB = inverted.getTermFrequencyInDocument(termIdB, docIndexB);
            int oldValue = CO.getAsInt(termIdA, termIdB);
            int newValue = oldValue + Math.min(frequencyA, frequencyB);
            CO.setAsInt(termIdA, termIdB, newValue);
        }
	
    }

    private void writeCoOccurrance(SparseMatrix CO, int first, int last, String path) {
        Path CO_path = Paths.get(path + "/CO"+"-"+first+"-"+(last-1));
        Charset charset = Charset.forName("UTF-8");
        List<String> lines = new ArrayList<String>();
        Iterable<long[]> non_zero_coordinates = CO.nonZeroCoordinates();
        Iterator<long[]> iterator_over_non_zero_coordinates = non_zero_coordinates.iterator();
        while (iterator_over_non_zero_coordinates.hasNext()) {
            long[] coordinates = iterator_over_non_zero_coordinates.next();
            String line = coordinates[0] + " " + coordinates[1] + " " + CO.getAsInt(coordinates[0], coordinates[1]);
            lines.add(line);
        }
        try {
            Files.write(CO_path, lines, charset);
        }
        catch (IOException e) {
            // TODO
        }
    }

}