import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private Point points[];
    private ArrayList<LineSegment> lineSeg;
    private Point[] subset;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points){
        if(!NoException(points)) throw new IllegalArgumentException();

        this.points = points;
        lineSeg = new ArrayList<>();
        subset = new Point[4];

        for(int i = 0; i < points.length - 3; i++) {
            subset[0] = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                subset[1] = points[j];
                for (int k = j + 1; k < points.length - 1; k++) {
                    subset[2] = points[k];
                    for (int l = k + 1; l < points.length; l++) {
                        subset[3] = points[l];
                        if(checkSegment(i, j, k, l)){
                            Arrays.sort(subset, pointOrder());
                            LineSegment line = new LineSegment(subset[0], subset[3]);
                            if(!lineSeg.contains(line)){
                                lineSeg.add(line);
                            }
                        }
                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments(){
        return lineSeg.size();
    }

    // the line segments
    public LineSegment[] segments(){
        return lineSeg.toArray(new LineSegment[lineSeg.size()]);
    }

    private boolean checkSegment(int i, int j, int k, int l){
        return (points[i].slopeTo(points[j]) ==
                points[i].slopeTo(points[k]) &&
                points[i].slopeTo(points[j]) == points[i].slopeTo(points[l]));
    }

    private boolean NoException(Point points[]){
        if (points == null) return false;
        for (Point p : points)
            if (p == null) return false;

        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if ((points[i].compareTo(points[j]) == 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Comparator<Point> pointOrder(){
        return new PointComp();
    }

    private class PointComp implements Comparator<Point>{
        public int compare(Point a, Point b){
            if(a.compareTo(b) < 0) return -1;
            else if(a.compareTo(b) > 0) return 1;
            else return 0;
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("collinear/input48.txt");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
