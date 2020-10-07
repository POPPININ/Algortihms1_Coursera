import edu.princeton.cs.algs4.*;

import java.util.Comparator;

public class PointSET
{
    private SET<Point2D> pointSET;

    // construct an empty set of points
    public PointSET(){ pointSET = new SET<>(); }

    // is the set empty?
    public boolean isEmpty() { return pointSET.isEmpty(); }

    // number of points in the set
    public int size() { return pointSET.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if(p == null) throw new IllegalArgumentException();
        if(!contains(p)) pointSET.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if(p == null) throw new IllegalArgumentException();
        else return pointSET.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        for(Point2D p : pointSET)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.05);
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        Point2D min = new Point2D(rect.xmin(), rect.ymin());
        Point2D max = new Point2D(rect.xmax(), rect.ymax());
        Queue<Point2D> pointRange = new Queue<>();

        for(Point2D p : pointSET){
            if(p.compareTo(min) >= 0 && p.compareTo(max) <= 0)
                pointRange.enqueue(p);
        }
        return pointRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        // neighbor between floor and ceiling
        if(p == null) throw new IllegalArgumentException();
        if(isEmpty()) return null;

        Comparator<Point2D> distComp = new DistanceTo(p);
        MinPQ<Point2D> neighbors = new MinPQ<>(distComp);
        for(Point2D q : pointSET) neighbors.insert(q);

        return neighbors.delMin();
    }

    // Compare points based on Euclidean distance to query
    private class DistanceTo implements Comparator<Point2D>
    {
        private Point2D point;
        DistanceTo(Point2D p) { point = p; }
        public int compare(Point2D A, Point2D B)
        {
            double distA = point.distanceTo(A);
            double distB = point.distanceTo(B);
            if(distA > distB) return 1;
            else if(distA < distB) return -1;
            else return 0;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {


    }
}
