package util;

public class Point implements Cloneable {

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point cen, double angle, double r) {
        this.x = cen.x + r * Math.cos(angle);
        this.y = cen.y + r * Math.sin(angle);
    }

    public double distance(Point p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    public Point projectionToSegment(Point p, Point q) {
        double a = p.y - q.y, b= q.x - p.x, c= q.x * p.y - p.x * q.y;
        double c2 = - a * x + b * y;
        Point g = Intersect.getIntersectLine2Segment(p, q, a, -b, c2);
        return g;
    }

    @Override
    public String toString() {
        return "Point: (" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        return ((this.x == p.x) && (this.y == p.y));
    }

    @Override
    public int hashCode() {
        return 100 * (int) (x + y);
    }

    @Override
    public Point clone() {
        return new Point(this.x, this.y);
    }

}
