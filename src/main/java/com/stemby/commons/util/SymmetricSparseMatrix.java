package com.stemby.commons.util;

/**
 * Class that represents a symmetric sparse matrix.
 * 
 * @author stemby
 */
public class SymmetricSparseMatrix extends SparseMatrix {

    public SymmetricSparseMatrix(int size) {
        super(size, size);
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
        if (x > y) {
            return super.getAsInt(y, x);
        }
        return super.getAsInt(x, y);
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
        if (x > y) {
            return super.getAsFloat(y, x);
        }
        return super.getAsFloat(x, y);
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
        if (x > y) {
            super.setAsInt(y, x, value);
            return;
        }
        super.setAsInt(x, y, value);
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
        if (x > y) {
            super.setAsFloat(y, x, value);
            return;
        }
        super.setAsFloat(x, y, value);
    }

}