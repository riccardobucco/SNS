package com.stemby.commons.util;

/**
 * Class that represents a sparse matrix.
 * 
 * @author stemby
 */
public class SparseMatrix implements Matrix {

    private org.ujmp.core.SparseMatrix matrix;

    /**
     * Class constructor.
     * 
     * @param   columnsNumber   The number of columns of the new matrix.
     * @param   rowsNumber      The number of rows of the new matrix.
     */
    public SparseMatrix(int columnsNumber, int rowsNumber) {
        matrix = org.ujmp.core.SparseMatrix.Factory.zeros(columnsNumber, rowsNumber);
    }

    /**
     * It turns the current matrix to a org.ujmp.core.SparseMatrix
     * 
     * @return  Conversion of the current matrix to a
     *          org.ujmp.core.SparseMatrix.
     */
    protected org.ujmp.core.SparseMatrix getMatrix() {
        return matrix;
    }

    /**
     * It returns how many rows the matrix has.
     * 
     * @return The number of rows in the matrix.
     */
    public int getRowCount() {
        return (int)(matrix.getRowCount());
    }

    /**
     * It returns how many columns the matrix has.
     * 
     * @return The number of columns in the matrix.
     */
    public int getColumnCount() {
        return (int)(matrix.getColumnCount());
    }

    /**
     * It returns true if the matrix contains no elements, false otherwise.
     * 
     * @return Whether the matrix contains no elements or not.
     */
    public boolean isEmpty() {
        return matrix.isEmpty();
    }

    /**
     * It returns the element of the matrix at the specified coordinates as a
     * float.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @return          The element of the matrix at the specified coordinates.
     */
    public int getAsInt(int x, int y) {
        return matrix.getAsInt(x, y);
    }

    /**
     * It returns the element of the matrix at the specified coordinates as a
     * float.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @return          The element of the matrix at the specified coordinates.
     */
    public float getAsFloat(int x, int y) {
        return matrix.getAsFloat(x, y);
    }

    /**
     * It sets the given value as a new int element of the matrix at the
     * specified coordinates.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @param   value   The value to be inserted at the specified coordinates.
     */
    public void setAsInt(int x, int y, int value) {
        matrix.setAsInt(value, x, y);
    }

    /**
     * It sets the given value as a new float element of the matrix at the
     * specified coordinates.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @param   value   The value to be inserted at the specified coordinates.
     */
    public void setAsFloat(int x, int y, float value) {
        matrix.setAsFloat(value, x, y);
    }

    /**
     * It clears the entire matrix. Every element of the matrix is set to 0.
     */
    public void clear() {
        matrix.clear();
    }

    /**
     * It returns an iterable over non-zero coordinates. The returned object
     * can be the target of the "foreach" statement.
     * 
     * @return An iterable over non-zero coordinates of the matrix.
     */
    public Iterable<long[]> nonZeroCoordinates() {
        return matrix.nonZeroCoordinates();
    }

}