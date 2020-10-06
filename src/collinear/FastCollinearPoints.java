import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private Point points2[];
    private LineSegment segments[];
    private ArrayList<LineSegment> lineSeg;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (!NoException(points)) throw new IllegalArgumentException();

        this.points2 = Arrays.copyOf(points, points.length);
        lineSeg = new ArrayList<>();

        Arrays.sort(points, pointOrder());
        for (int i = 0; i < points.length; ++i) {
            Point p = points[i];
            Arrays.sort(points2);
            Arrays.sort(points2, p.slopeOrder());
            int count = 1;
            Point lineOrigin = null;
            for (int j = 0; j < points2.length - 1; ++j) {
                if (points2[j].slopeTo(p) == points2[j + 1].slopeTo(p)) {
                    count++;
                    if (count == 2) {
                        lineOrigin = points2[j];
                        count++;
                    } else if (count >= 4 && j + 1 == points2.length - 1) {
                        if (lineOrigin.compareTo(p) > 0) {
                            lineSeg.add(new LineSegment(p, points2[j + 1]));
                        }
                        count = 1;
                    }
                } else if (count >= 4) {
                    if (lineOrigin.compareTo(p) > 0) {
                        lineSeg.add(new LineSegment(p, points2[j]));
                    }
                    count = 1;
                } else {
                    count = 1;
                }

            }

        }
        segments = lineSeg.toArray(new LineSegment[lineSeg.size()]);
    }


    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

    private boolean NoException(Point points[]) {

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


    private Comparator<Point> pointOrder() {
        return new PointComp();
    }

    private class PointComp implements Comparator<Point> {
        public int compare(Point a, Point b) {
            if (a.compareTo(b) < 0) return -1;
            else if (a.compareTo(b) > 0) return 1;
            else return 0;
        }
    }

    public static void main(String[] args)
    {

        Point p[] = new Point[5];
        for(int i = 0; i < p.length; i++)
            p[i] = new Point(i,i);

        System.out.println("The points are: ");

        for(Point m : p)
            System.out.println(m.toString());

        FastCollinearPoints fcp = new FastCollinearPoints(p);

        System.out.println("The line segements are: ");
        for(LineSegment l : fcp.segments())
            System.out.println(l.toString());
        System.out.println("# segments: " + fcp.numberOfSegments());

    }
}
