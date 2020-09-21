/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: April 9th 2020
 *  Description: Creates a percolation model based on an n-by-n grid where sites
 *               can be open or closed. Uses the Union-Find data structure.
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int width; // size of grid
    private final WeightedQuickUnionUF uf;
    private int openSize; // number of open sites
    private final int virtualTop;
    private final int virtualBottom;


    // creates an n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Size of grid must be greater than 0");
        width = n;
        grid = new boolean[width + 2][width + 2];
        openSize = 0;

        virtualTop = 0;
        virtualBottom = width + 1;
        grid[virtualTop][virtualTop] = true;
        grid[virtualBottom][virtualBottom] = true;

        uf = new WeightedQuickUnionUF(width * width + 2);

        // virtualTop will translate to index 0 in uf, connect to the top row
        for (int i = 1; i <= width; i++) {
            uf.union(virtualTop, i);
        }

        // virtualBottom will translate to index width * width +1 in uf, connect to bottom row
        for (int i = 1; i <= width; i++) {
            uf.union(width * width + 1, (width - 1) * width + i);
        }


    }

    // maps a 2D pt (row, col) to a 1d pt in a UF array
    private int xyTo1D(int row, int col) {
        if (row == 0 && col == 0) return 0;
        else if (row == width + 1 && col == width + 1) return width * width + 1;
        else {
            validate(row, col);
            return (row - 1) * width + col;
        }
    }

    // validates indices
    private void validate(int row, int col) {
        if (row <= 0 || row > width || col <= 0 || col > width) {
            throw new IllegalArgumentException("Indices out of range");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!grid[row][col]) {
            grid[row][col] = true;
            openSize++;
        }
        int index = xyTo1D(row, col);
        if (row > 1 && isOpen(row - 1, col)) {
            int index1 = xyTo1D(row - 1, col);
            uf.union(index, index1);
        }

        if (row < width && isOpen(row + 1, col)) {
            int index2 = xyTo1D(row + 1, col);
            uf.union(index, index2);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            int index3 = xyTo1D(row, col - 1);
            uf.union(index, index3);
        }

        if (col < width && isOpen(row, col + 1)) {
            int index4 = xyTo1D(row, col + 1);
            uf.union(index, index4);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = xyTo1D(row, col);
        return (isOpen(row, col) && uf.connected(0, index));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSize;
    }

    // does the system percolate?
    public boolean percolates() {
        if (width == 1) {
            return isOpen(1, 1);
        }
        else {
            return uf.connected(0, xyTo1D(virtualBottom, virtualBottom));
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // tested and then left empty
    }
}
