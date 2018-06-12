package com.stemby.commons.algorithms;

import com.stemby.commons.util.Matrix;

/**
 * Class that includes algorithms on matrices. It contains only static methods,
 * so it is not possible to use any constructor.
 * 
 * @author stemby
 */
public class Matrices {

    private Matrices() {}

    /**
     * It returns the index (i.e. the column) of the maximum element of the
     * row.
     * 
     * @param   matrix      The matrix to which belongs the row. 
     * @param   rowIndex    The raw in which the method has to search for
     *                      the index of the maximum element.
     * @return              The index of the maximum element of the given row.
     */
    public static int getIndexOfMaxElementOfRow(Matrix matrix, int rowIndex) {
        int columnCount = matrix.getColumnCount();
        int maxIndex = 0;
        float max = matrix.getAsFloat(maxIndex, rowIndex);
        for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
            float value = matrix.getAsFloat(columnIndex, rowIndex);
            if (max < value) {
                maxIndex = columnIndex;
                max = value;
            }
        }
        return maxIndex;
    }

}