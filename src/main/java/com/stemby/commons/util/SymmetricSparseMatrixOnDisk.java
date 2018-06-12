package com.stemby.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.Iterable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;
import org.ujmp.core.SparseMatrix;

/**
 * This class can use a big sparse matrix stored on disk. It is useful
 * when there is not enough RAM to store everything.
 * <p>
 * It assumes that data of the matrix are stored in some files. These files
 * can be found ad a specified path. In this path there must be two subfolders:
 * <ul>
 *     <li>indexed_by_x</li>
 *     <li>indexed_by_y</li>
 * </ul>
 * They contains, respectively, files representing blocks of columns and blocks
 * of rows. Every file in the folder indexed_by_x has this format:
 * PREFIX-START-END. It must be interpreted as follows:
 * <ul>
 *     <li>PREFIX is a general prefix that represents the content of files
 *         in the folder.</li>
 *     <li>START is the index of the first column in the file.</li>
 *     <li>END is the index of the last column in the file.</li>
 * </ul>
 * The folder indexed_by_y is similar.
 * <p>
 * Every file has a line for every non-zero element. A line contains 3 values:
 * the first is the column of the element, the second is the row of the element
 * and the third is the value of the specified coordinates.
 * <p>
 * THIS CLASS IS NOT STABLE AND IT IS USED FOR EXPERIMENTAL PURPOSE. USE AT
 * YOUR OWN RISK.
 * 
 * @author  stemby
 */
public class SymmetricSparseMatrixOnDisk implements Matrix {

    private long matrix_size;
    private String base_path;
    private String file_prefix;
    private int file_size;
    private long min_x_in_memory = Long.MAX_VALUE;
    private long max_x_in_memory = Long.MIN_VALUE;
    private long min_y_in_memory = Long.MAX_VALUE;
    private long max_y_in_memory = Long.MIN_VALUE;
    private SparseMatrix col_block;
    private SparseMatrix row_block;

    /**
     * Class constructor.
     * 
     * @param   base_path       The base path where the files can be found
     *                          (inside two subsolders).
     * @param   file_prefix     The prefix of each file.
     */
    public SymmetricSparseMatrixOnDisk(String base_path, String file_prefix) {
        this.base_path = base_path;
        this.file_prefix = file_prefix;
        final String prefix = file_prefix;
        File directory = new File(base_path + "/indexed_by_x");
        String first_file_name = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().startsWith(prefix+"-0-");
            }
        })[0].getName();
        this.file_size = Integer.parseInt(first_file_name.split("-")[2]) + 1;
        final int number_of_files = directory.listFiles().length;
        String last_file_name = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().startsWith(prefix+"-" + ((number_of_files-1)*file_size) + "-");
            }
        })[0].getName();
        this.matrix_size = Long.parseLong(last_file_name.split("-")[2]) + 1;
    }

    /**
     * It returns how many rows the matrix has.
     * 
     * @return The number of rows in the matrix.
     */
    public int getRowCount() {
        return (int)(matrix_size);
    }

    /**
     * It returns how many columns the matrix has.
     * 
     * @return The number of columns in the matrix.
     */
    public int getColumnCount() {
        return (int)(matrix_size);
    }

   /**
     * It returns true if the matrix contains no elements, false otherwise.
     * 
     * @return Whether the matrix contains no elements or not.
     */
    public boolean isEmpty() {
        Iterable<long[]> non_zero_coordinates = nonZeroCoordinates();
        Iterator<long[]> it = non_zero_coordinates.iterator();
        return !(it.hasNext());
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
        if (isInMemory(x, y)) {
            return getValueInMemoryAsInt(x, y);
        }
        if (isInMemory(y, x)) {
            return getValueInMemoryAsInt(y, x);
        }
        updateMemory(x, y);
        return getValueInMemoryAsInt(x, y);
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
        if (isInMemory(x, y)) {
            return getValueInMemoryAsFloat(x, y);
        }
        if (isInMemory(y, x)) {
            return getValueInMemoryAsFloat(y, x);
        }
        updateMemory(x, y);
        return getValueInMemoryAsFloat(x, y);
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
        // TODO
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
        // TODO
    }

    /**
     * It clears the entire matrix. Every element of the matrix is set to 0.
     */
    public void clear() {
        // TODO
    }

    /**
     * It returns an iterable over non-zero coordinates. The returned object
     * can be the target of the "foreach" statement.
     * 
     * @return An iterable over non-zero coordinates of the matrix.
     */
    public Iterable<long[]> nonZeroCoordinates() {
        return new NonZeroCoordinates();
    }

    /**
     * This method can be used to load in memory the block of columns which
     * contains a specified column.
     * 
     * @param   column_number   The index of the column to load in memory.
     */
    public void loadColumnInMemory(long column_number) {
        if (!isInMemory(column_number, 0)) {
            updateMemoryX(column_number);
        }
    }

    /**
     * This method can be used to load in memory the block of rows which
     * contains a specified row.
     * 
     * @param   row_number  The index of the row to load in memory.
     */
    public void loadRowInMemory(long row_number) {
        if (!isInMemory(0, row_number)) {
            updateMemoryY(row_number);
        }
    }

    /**
     * It returns an Iterable object which represents non-zero coordinates of
     * a specified block of columns. It is possible to call the iterator method
     * on it.
     * 
     * @param   block_number    The index of the block of columns.
     * @return                  It returns an Iterable object which represents
     *                          non-zero coordinates of specified block of
     *                          columns.
     */
    public Iterable<long[]> nonZeroCoordinatesOfColumnsBlock(int block_number) {
        System.out.println("Getting non zero coordinates of block number " + block_number);
        updateMemoryX(block_number * file_size);
        List<long[]> sortedList = new ArrayList<>();
        Iterable<long[]> non_zero_coordinates_of_col_block = col_block.nonZeroCoordinates();
        Iterator<long[]> it = non_zero_coordinates_of_col_block.iterator();
        while (it.hasNext()) {
            long[] coordinates = new long[2];
            long[] next = it.next();
            coordinates[0] = next[0];
            coordinates[1] = next[1];
            sortedList.add(coordinates);
        }
        System.out.println("Sorting coordinates");
        Collections.sort(sortedList, new Comparator<long[]>() {
            public int compare(long[] a, long[] b) {
                if (a[1] != b[1]) {
                    return (int)(a[1] - b[1]);
                }
                else {
                    return (int)(a[0] - b[0]);
                }
            }
        });
        return sortedList;
    }

    private boolean isInMemory(long x, long y) {
        if (((x < min_x_in_memory) || (x > max_x_in_memory)) && ((y < min_y_in_memory) || (y > max_y_in_memory))) {
            return false;
        }
        else {
            return true;
        }
    }

    private float getValueInMemoryAsFloat(long x, long y) {
        if ((x < min_x_in_memory) || (x > max_x_in_memory)) {
            return row_block.getAsFloat(x, y - min_y_in_memory);
        }
        else {
            return col_block.getAsFloat(x - min_x_in_memory, y);
        }
    }

    private int getValueInMemoryAsInt(long x, long y) {
        if ((x < min_x_in_memory) || (x > max_x_in_memory)) {
            return row_block.getAsInt(x, y - min_y_in_memory);
        }
        else {
            return col_block.getAsInt(x - min_x_in_memory, y);
        }
    }

    private void updateMemory(long x, long y) {
        updateMemoryX(x);
        updateMemoryY(y);
    }

    private void updateMemoryX(long x) {
        min_x_in_memory = (x/file_size) * file_size;
        max_x_in_memory = Math.min(min_x_in_memory+file_size - 1, matrix_size-1);
        if (col_block != null) {
            col_block.clear();
            col_block = null;
        }
        col_block = SparseMatrix.Factory.zeros(max_x_in_memory - min_x_in_memory + 1, matrix_size);
        Path path_col_block = Paths.get(base_path + "/indexed_by_x/"+file_prefix+"-" + min_x_in_memory + "-" + max_x_in_memory);
        System.out.println("Reading file " + path_col_block);
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(path_col_block, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(" ");
                col_block.setAsInt(Integer.parseInt(items[2]), Integer.parseInt(items[0]) - min_x_in_memory, Integer.parseInt(items[1]));
            }
            reader.close();
        }
        catch (IOException e) {
            // TODO
        }
    }

    private void updateMemoryY(long y) {
        min_y_in_memory = (y/file_size) * file_size;
        max_y_in_memory = Math.min(min_y_in_memory+file_size - 1, matrix_size-1);
        if (row_block != null) {
            row_block.clear();
            row_block = null;
        }
        row_block = SparseMatrix.Factory.zeros(matrix_size, max_y_in_memory - min_y_in_memory + 1);
        Path path_row_block = Paths.get(base_path + "/indexed_by_y/"+file_prefix+"-" + min_y_in_memory + "-" + max_y_in_memory);
        Charset charset = Charset.forName("UTF-8");
        System.out.println("Reading file " + path_row_block);
        try (BufferedReader reader = Files.newBufferedReader(path_row_block, charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(" ");
                row_block.setAsInt(Integer.parseInt(items[2]), Integer.parseInt(items[0]), Integer.parseInt(items[1]) - min_y_in_memory);
            }
            reader.close();
        }
        catch (IOException e) {
            // TODO
        }
    }

    private class NonZeroCoordinates implements Iterable<long[]> {
        public Iterator<long[]> iterator() {
            return new IteratorOverNonZeroCoordinates();
        }
    }

    private class IteratorOverNonZeroCoordinates implements Iterator<long[]> {

        private Iterator<long[]> current_iterator;
        private long min_x_of_iterator;

        public IteratorOverNonZeroCoordinates() {
            min_x_of_iterator = 0;
            updateMemoryX(min_x_of_iterator);
            current_iterator = col_block.nonZeroCoordinates().iterator();
        }

        public boolean hasNext() {
            if (current_iterator.hasNext()) {
                return true;
            }
            min_x_of_iterator = min_x_of_iterator + file_size;
            if (min_x_of_iterator >= matrix_size) {
                return false;
            }
            updateMemoryX(min_x_of_iterator);
            current_iterator = null;
            current_iterator = col_block.nonZeroCoordinates().iterator();
            return hasNext();
        }

        public long[] next() {
            if (hasNext()) {
                long[] non_zero_coordinates = current_iterator.next();
                non_zero_coordinates[0] = non_zero_coordinates[0] + min_x_of_iterator;
                return non_zero_coordinates;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}