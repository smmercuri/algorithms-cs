/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 29 2020
 *  Description: Seam Carver algorithm for images. Implemented by replicating
 *               the shortest-path algorithm for directed acyclic graphs on
 *               2D arrays.
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private int[][] RGBarray; // RGBarray of current picture

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.RGBarray = new int[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                RGBarray[i][j] = picture.getRGB(j, i);
            }
        }
    }

    // current picture
    public Picture picture() {
        int currentWidth = RGBarray[0].length;
        int currentHeight = RGBarray.length;
        Picture current = new Picture(currentWidth, currentHeight);
        for (int i = 0; i < currentHeight; i++) {
            for (int j = 0; j < currentWidth; j++) {
                Color pixelColour = rgbToColor(RGBarray[i][j]);
                current.set(j, i, pixelColour);
            }
        }
        return current;
    }

    // convert RGB integer to Color type
    private Color rgbToColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;
        Color col = new Color(r, g, b);
        return col;
    }

    // width of current picture
    public int width() {
        return RGBarray[0].length;
    }

    // height of current picture
    public int height() {
        return RGBarray.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000;
        return Math.sqrt(xGrad(x, y) + yGrad(x, y));
    }

    // x-gradient
    private double xGrad(int x, int y) {
        Color xMinus = rgbToColor(RGBarray[y][x - 1]);
        int rMinus = xMinus.getRed();
        int gMinus = xMinus.getGreen();
        int bMinus = xMinus.getBlue();
        Color xPlus = rgbToColor(RGBarray[y][x + 1]);
        int rPlus = xPlus.getRed();
        int gPlus = xPlus.getGreen();
        int bPlus = xPlus.getBlue();
        int Rx = rPlus - rMinus;
        int Gx = gPlus - gMinus;
        int Bx = bPlus - bMinus;
        return Rx * Rx + Gx * Gx + Bx * Bx;
    }


    // y-gradient
    private double yGrad(int x, int y) {
        Color yMinus = rgbToColor(RGBarray[y - 1][x]);
        int rMinus = yMinus.getRed();
        int gMinus = yMinus.getGreen();
        int bMinus = yMinus.getBlue();
        Color yPlus = rgbToColor(RGBarray[y + 1][x]);
        int rPlus = yPlus.getRed();
        int gPlus = yPlus.getGreen();
        int bPlus = yPlus.getBlue();
        int Ry = rPlus - rMinus;
        int Gy = gPlus - gMinus;
        int By = bPlus - bMinus;
        return Ry * Ry + Gy * Gy + By * By;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energiesTranspose = new double[RGBarray[0].length][RGBarray.length];
        for (int i = 0; i < RGBarray[0].length - 1; i++) {
            for (int j = 0; j < RGBarray.length - 1; j++) {
                energiesTranspose[i][j] = energy(i, j);
            }
        }
        return findVerticalEnergySeam(energiesTranspose);

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = new double[RGBarray.length][RGBarray[0].length];
        for (int i = 0; i < RGBarray.length - 1; i++) {
            for (int j = 0; j < RGBarray[0].length - 1; j++) {
                if (i == 0 || i == RGBarray.length - 1 || j == 0 || j == RGBarray[0].length - 1) {
                    energies[i][j] = 1000;
                }
                else energies[i][j] = energy(j, i); // picture object has different indices to array
            }
        }
        return findVerticalEnergySeam(energies);
    }

    // finds the lowest-energy vertical seam -- this is the shortest-path algorithm
    private int[] findVerticalEnergySeam(double[][] energies) {
        double[][] energiesTo = new double[energies.length][energies[0].length];
        int[][] pathTo = new int[energies.length][energies[0].length];

        for (int j = 0; j < energies[0].length; j++) {
            energiesTo[0][j] = 1000;
        }
        for (int i = 1; i < energies.length; i++) {
            for (int j = 0; j < energies[0].length; j++) {
                energiesTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int y = 0; y < energies.length - 1; y++) {
            for (int x = 1; x < energies[0].length - 1; x++) {
                for (int v = x - 1; v < x + 2; v++) {
                    if (energiesTo[y + 1][v] > energiesTo[y][x] + energies[y + 1][v]) {
                        energiesTo[y + 1][v] = energiesTo[y][x] + energies[y + 1][v];
                        pathTo[y + 1][v] = x;
                    }
                }
            }
        }
        double minTotalEnergy = energies.length * 1000;
        int minTotalEnergyIndex = 0;
        for (int x = 0; x < energies[0].length; x++) {
            if (energiesTo[energies.length - 1][x] < minTotalEnergy) {
                minTotalEnergyIndex = x;
                minTotalEnergy = energiesTo[energies.length - 1][x];
            }
        }
        int[] seamPath = new int[energies.length];
        int pointer = minTotalEnergyIndex;
        for (int y = energies.length - 1; y >= 0; y--) {
            seamPath[y] = pointer;
            pointer = pathTo[y][pointer];
        }
        return seamPath;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        if (!isValidHorizontalSeam(seam)) throw new IllegalArgumentException();
        int[][] RGBtransposed = transposeArray(RGBarray);
        RGBarray = transposeArray(removeSeam(seam, RGBtransposed));

    }

    // transpose array
    private static int[][] transposeArray(int[][] m) {
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        if (!isValidVerticalSeam(seam)) throw new IllegalArgumentException();
        RGBarray = removeSeam(seam, RGBarray);
    }

    // helper function for remove seams to work for vertical or horizontal
    private int[][] removeSeam(int[] seam, int[][] array) {
        int newHeight = array.length;
        int newWidth = array[0].length - 1;
        int[][] resizedArray = new int[newHeight][newWidth];
        for (int i = 0; i < seam.length; i++) {
            System.arraycopy(array[i], seam[i] + 1, resizedArray[i], seam[i], newWidth - seam[i]);
            System.arraycopy(array[i], 0, resizedArray[i], 0, seam[i]);
        }
        return resizedArray;
    }

    // checks whether horizontal seam is valid, i.e. all the y coordinates are between 0 and height - 1
    // and consecutive values differ by 1 at most
    private boolean isValidHorizontalSeam(int[] seam) {
        boolean validity = true;
        for (int i = 0; i < width(); i++) {
            if (seam[i] < 0 || seam[i] > height() - 1) validity = false;
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) validity = false;
        }
        return validity;
    }

    // checks whether vertical seam is valid, i.e. all the x coordinates are between 0 and width - 1
    // and consecutive values differ by 1 at most
    private boolean isValidVerticalSeam(int[] seam) {
        boolean validity = true;
        for (int i = 0; i < height(); i++) {
            if (seam[i] < 0 || seam[i] > width() - 1) validity = false;
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) validity = false;
        }
        return validity;
    }

    public static void main(String[] args) {

    }
}
