import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation n
 *
 *  Percolation uses WeightedQuickUnionUF to connect open sites on an n x n grid
 *  To determine if a system has a connected branch from top to bottom
 *  "Does the system Percolate?"
 *
 ******************************************************************************/

public class Percolation {
    private final int gridSites;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF dataStructureFS; // Fullsite
    private final WeightedQuickUnionUF dataStructureVS; // Virtualsite
    private int openSites;
    private final int sitesVS;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        gridSites = n;
        int sites = n * n;
        sitesVS = sites + 2;
        grid = new boolean[n][n];
        dataStructureVS = new WeightedQuickUnionUF(sitesVS);
        dataStructureFS = new WeightedQuickUnionUF(sitesVS - 1);
        openSites = 0;
    }

    public void open(int row, int col) {
        cornerCases(row, col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        openSites++;

        int virtualSite = (row - 1) * gridSites + col;
        // check for possible union l
        if (existsOnGrid(row, col - 1) && isOpen(row, col - 1)) {
            dataStructureVS.union(virtualSite, virtualSite - 1);
            dataStructureFS.union(virtualSite, virtualSite - 1);
        }
        // check for possible union r
        if (existsOnGrid(row, col + 1) && isOpen(row, col + 1)) {
            dataStructureVS.union(virtualSite, virtualSite + 1);
            dataStructureFS.union(virtualSite, virtualSite + 1);
        }
        // check for possible union u
        if (row == 1) {
            dataStructureVS.union(virtualSite, 0);
            dataStructureFS.union(virtualSite, 0);
        } else {
            if (existsOnGrid(row - 1, col) && isOpen(row - 1, col)) {
                dataStructureVS.union(virtualSite, virtualSite - gridSites);
                dataStructureFS.union(virtualSite, virtualSite - gridSites);
            }
        }
        // check for possible union d
        if (row == gridSites) {
            dataStructureVS.union(virtualSite, sitesVS - 1);
        } else {
            if (existsOnGrid(row + 1, col) && isOpen(row + 1, col)) {
                dataStructureVS.union(virtualSite, virtualSite + gridSites);
                dataStructureFS.union(virtualSite, virtualSite + gridSites);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        cornerCases(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        cornerCases(row, col);
        int fullSite = (row - 1) * gridSites + col;
        return dataStructureFS.find(0) == dataStructureFS.find(fullSite);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return dataStructureVS.find(0) == dataStructureVS.find(sitesVS - 1);
    }

    // testing is optional
    public static void main(String[] args) {
        // body is empty
    }

    private boolean existsOnGrid(int row, int col) {
        row = row - 1;
        col = col - 1;
        return row >= 0 && row < gridSites && col >= 0 && col < gridSites;
    }

    private void cornerCases(int row, int col) {
        if (!existsOnGrid(row, col)) {
            throw new IllegalArgumentException("Argument is outside of its prescribed range");
        }
    }
    /*
    public void test() {

        this.open(1, 1);
        System.out.println(this.existsOnGrid(1, 1));
        System.out.println(this.isFull(1, 1));
        System.out.println(this.percolates());
    }
     */
}
