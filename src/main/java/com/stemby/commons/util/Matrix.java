package com.stemby.commons.util;

/**
 * The root interface for a matrix.
 * 
 * @author stemby
 */
public interface Matrix {

    /**
     * It returns how many rows the matrix has.
     * 
     * @return The number of rows in the matrix.
     */
    int getRowCount();

    /**
     * It returns how many columns the matrix has.
     * 
     * @return The number of columns in the matrix.
     */
    int getColumnCount();

   /**
     * It returns true if the matrix contains no elements, false otherwise.
     * 
     * @return Whether the matrix contains no elements or not.
     */
    boolean isEmpty();

    /**
     * It returns the element of the matrix at the specified coordinates as a
     * float.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @return          The element of the matrix at the specified coordinates.
     */
    int getAsInt(int x, int y);

    /**
     * It returns the element of the matrix at the specified coordinates as a
     * float.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @return          The element of the matrix at the specified coordinates.
     */
    float getAsFloat(int x, int y);

    /**
     * It sets the given value as a new int element of the matrix at the
     * specified coordinates.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @param   value   The value to be inserted at the specified coordinates.
     */
    void setAsInt(int x, int y, int value);

    /**
     * It sets the given value as a new float element of the matrix at the
     * specified coordinates.
     * 
     * @param   x       The column in which the new value has to be inserted.
     * @param   y       The row in which the new value has to be inserted.
     * @param   value   The value to be inserted at the specified coordinates.
     */
    void setAsFloat(int x, int y, float value);

    /**
     * It clears the entire matrix. Every element of the matrix is set to 0.
     */
    void clear();

    /**
     * It returns an iterable over non-zero coordinates. The returned object
     * can be the target of the "foreach" statement.
     * 
     * @return An iterable over non-zero coordinates of the matrix.
     */
    Iterable<long[]> nonZeroCoordinates();

}