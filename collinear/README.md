# Collinear Points

The problem: given a set of *n* distinct points in the plane, find every maximal line segment that connects a subset of 4 or more points. That is, we are looking for groups of 4 or more collinear points.

Here, we implement two algorithms to solve this. One is a brute force algorithm that simple checks all points. The other employs mergesort to produce a more efficient algorithm.

First, we implement a *Point* data type to represent the points. We shall make use of the *LineSegment* data type from algs4.jar library to represent line segments. Then in *BruteCollinearPoints* we implement a brute-force algorithm.

To do this efficiently, we do the following in *FastCollinearPoints*. For each point *p*, to check if it participates in a line segment of 4 or more points, we sort all other points by the slope they make with *p* using Java's *Arrays.sort* (which uses Quicksort). Then we search for 3 consecutive points whose slope with *p* are all equal, then *p* and these three points lie on a line.

See [https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php) for the full specification.
