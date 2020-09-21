/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: May 25 2020
 *  Description: An efficient algorithm for finding all subsets of 4 or more
 *               collinear points. For each point p, we sort all other points
 *               with respect to p by their slope order. Then we look for
 *               3 or more adjecent points, in the sorted order, whose slopes
 *               with p are all equal. These three + p are collinear.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segmentArr;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        else if (hasNull(points)) throw new IllegalArgumentException();
        else if (repeated(points)) throw new IllegalArgumentException();
        int n = points.length;
        segmentArr = new LineSegment[n];
        // to store collinear points
        Point[] collinear = new Point[4];
        // start algorithm
        for (int i = 0; i < n; i++) {
            Point p = points[i];
            collinear[0] = p;
            // copy for swaps
            Point[] copy = new Point[n];
            for (int j = 0; j < n; j++) copy[j] = points[j];
            // swap p so that it is in first position
            copy[i] = copy[0];
            copy[0] = p;
            Comparator<Point> SLOPE_ORDER = p.slopeOrder();
            // sort all other points wrt slopeOrder
            Arrays.sort(copy, 1, n, SLOPE_ORDER);
            for (int j = 0; j < n - 2; j++) {
                if (p.slopeTo(copy[j]) == p.slopeTo(copy[j + 2])) {
                    Point[] a = new Point[4];
                    a[0] = p;
                    for (int k = 1; k < 4; k++) {
                        collinear[k] = copy[j + k - 1];
                        a[k] = copy[j + k - 1];
                    }
                    Arrays.sort(a);
                    if (a[0].equals(collinear[0])) {
                        Arrays.sort(a);
                        LineSegment collinearSeg = new LineSegment(a[0], a[3]);
                        segmentArr[numberOfSegments++] = collinearSeg;
                    }
                }
            }
        }

    }

    // helper function, determines whether a group of points contains a null element
    private boolean hasNull(Point[] points) {
        boolean hasNull = false;
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                hasNull = true;
                break;
            }
        }
        return hasNull;
    }

    // helper function, determines whether there is a repeated point
    private boolean repeated(Point[] points) {
        boolean repeated = false;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    repeated = true;
                    break;
                }
            }
        }
        return repeated;
    }

    // number of segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = segmentArr[i];
        }
        return segments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }


        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

