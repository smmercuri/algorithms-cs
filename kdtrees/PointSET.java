/* *****************************************************************************
 *  Name: Salvatore Mercuri
 *  Date: June 8 2020
 *  Description: Brute-force method for range search and nearest neighbour
 *               search for points in the 2D plane. This uses a red-black BST
 *               to store the points.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private final SET<Point2D> pointSET;

    // construct an empty set of points
    public PointSET() {
        pointSET = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSET.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!pointSET.contains(p)) {
            pointSET.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointSET.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSET) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> rectPoints = new Stack<Point2D>();
        for (Point2D p : pointSET) {
            if (rect.contains(p)) rectPoints.push(p);
        }
        return rectPoints;
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (pointSET.isEmpty()) return null;
        double min = 1;
        Point2D minPt = p;
        for (Point2D q : pointSET) {
            double dist = q.distanceSquaredTo(p);
            if (dist < min) {
                minPt = q;
                min = dist;
            }
        }
        return minPt;
    }

    // unit testing
    public static void main(String[] args) {
        PointSET kd = new PointSET();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);
        }
        Point2D q = new Point2D(0.606, 0.23);
        StdOut.println(kd.nearest(q));
    }


}
