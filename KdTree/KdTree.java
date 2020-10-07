import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;

public class KdTree
{
    // Use enumeration to represent orientation for partitioning space
    private enum Orientation { VERTICAL, HORIZONTAL }
    private Node root;
    private int size;

    private class Node
    {

        private final Orientation orientation;
        private final RectHV rect;
        private final Point2D point;
        private Node left;
        private Node right;

        Node(Point2D p, Orientation o, RectHV rect)
        {
            point = p;
            orientation = o;
            this.rect = rect;
        }

        // Alternating orientation between odd and even levels
        private Orientation nextOrientation()
        {
            if(orientation == Orientation.VERTICAL)
                return Orientation.HORIZONTAL;
            else
                return Orientation.VERTICAL;
        }

        private RectHV rectLeft()
        {
            if(orientation == Orientation.VERTICAL)
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            else
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        private RectHV rectRight()
        {
            if(orientation == Orientation.VERTICAL)
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }

        // Horizontal separation: compare y-coordinate
        // Vertical separation: compare x-coordinate
        private boolean toTheRightOrTop(Point2D q)
        {
            return (orientation == Orientation.HORIZONTAL && point.y() > q.y())
                    || (orientation == Orientation.VERTICAL && point.x() > q.x());
        }
    }

    public KdTree()
    {
        root = null;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    public void insert(Point2D p) {
        if(p == null) throw new IllegalArgumentException();

        if (root == null)
        {
            root = new Node(p, Orientation.VERTICAL, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        Node prev = null;
        Node curr = root;

        do {
            if (curr.point.equals(p)) return;
            prev = curr;
            curr = curr.toTheRightOrTop(p) ? curr.left : curr.right;
        } while (curr != null);

        // initialise new node for insertion
        if (prev.toTheRightOrTop(p))
            prev.left = new Node(p, prev.nextOrientation(), prev.rectLeft());
        else
            prev.right = new Node(p, prev.nextOrientation(), prev.rectRight());

        size++;
    }

    public boolean contains(Point2D p)
    {
        if(p == null) throw new IllegalArgumentException();

        Node node = root;
        while (node != null)
        {
            if (node.point.equals(p)) return true;
            node = node.toTheRightOrTop(p) ? node.left : node.right;
        }
        return false;
    }

    public void draw()
    {
        Node query = root;
        recursDraw(query);
    }

    // recursively draw all points
    private void recursDraw(Node node)
    {
        if(node != null)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.05);
            node.point.draw();
        }
        else return;
        recursDraw(node.left);
        recursDraw(node.right);
    }


    // Points within a rectangle
    public Iterable<Point2D> range(RectHV rect)
    {
        if(rect == null) throw new IllegalArgumentException();
        LinkedList<Point2D> inRange = new LinkedList<>();
        addPoints(root, rect, inRange);
        return inRange;
    }

    // recursively identify all points within rectangle
    private void addPoints(Node node, RectHV rect, LinkedList<Point2D> inRange)
    {
        if (node == null) return;

        if (rect.contains(node.point)) {
            inRange.add(node.point);
            addPoints(node.left, rect, inRange);
            addPoints(node.right, rect, inRange);
            return;
        }
        if (node.toTheRightOrTop(new Point2D(rect.xmin(), rect.ymin())))
            addPoints(node.left, rect, inRange);

        if (!node.toTheRightOrTop(new Point2D(rect.xmax(), rect.ymax())))
            addPoints(node.right, rect, inRange);
    }

    public Point2D nearest(Point2D p)
    {
        if(p == null) throw new IllegalArgumentException();

        if(isEmpty())
            return null;
        else
            return nearest(p, root.point, root);
    }

    // identify the closest point to a query
    private Point2D nearest(Point2D target, Point2D closest, Node node)
    {
        if (node == null) return closest;

        double closestDist = closest.distanceTo(target);
        if (node.rect.distanceTo(target) < closestDist) {
            double nodeDist = node.point.distanceTo(target);
            if (nodeDist < closestDist) closest = node.point;
            if (node.toTheRightOrTop(target)) {
                closest = nearest(target, closest, node.left);
                closest = nearest(target, closest, node.right);
            } else {
                closest = nearest(target, closest, node.right);
                closest = nearest(target, closest, node.left);
            }
        }
        return closest;
    }


    // Testing
    public static void main(String[] args)
    {
        KdTree kdTree =  new KdTree();
        Point2D A = new Point2D(0.0,0.0);
        Point2D B = new Point2D(1.0,0.0);
        Point2D C = new Point2D(0.0,1.0);
        Point2D D = new Point2D(1.0,1.0);
        kdTree.insert(A);
        kdTree.insert(B);
        kdTree.insert(C);
        kdTree.insert(D);

        System.out.println("Drawing points in KdTree: ");
        kdTree.draw();

        RectHV rectHV = new RectHV(0.5, 0.5, 1.5, 1.5);

        System.out.println("Points within rectHv are: ");
        for(Point2D p : kdTree.range(rectHV))
        {
            System.out.println(p.toString());
            StdDraw.setPenRadius(0.05);
            StdDraw.setPenColor(StdDraw.BLUE);
            p.draw();
        }

    }
}
