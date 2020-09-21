/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: April 9th 2020
 *  Description: takes command-line arguments n and T and runs T independent
 *               trials on an n-by-n percolation grid to determine mean,
 *               standard deviation, and 95% confidence interval of the
 *               percolation threshold
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int[] percTrials; // array with results of trials
    private final int numTrials; // number of trials
    private final int width; // size of grid
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException(
                "Size of grid and number of trials must be greater than 0");
        numTrials = trials;
        percTrials = new int[numTrials];
        width = n;
        mean = 0;
        stddev = 0;
        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                perc.open(row, col);
            }
            percTrials[t] = perc.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(percTrials) / (width * width);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (numTrials == 1) {
            stddev = Double.NaN;
        }
        else {
            stddev = StdStats.stddev(percTrials) / (width * width);
        }
        return stddev;

    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(numTrials);

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(numTrials);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, T);
        StdOut.println("mean = " + stats.mean());
        StdOut.println("stddec = " + stats.stddev());
        StdOut.println(
                "95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi()
                        + "]");
    }
}
