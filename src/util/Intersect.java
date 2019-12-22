package util;

public class Intersect {
    public static int hptb2(double a1, double b1, double c1, double a2, double b2, double c2, Point p) {
        double delta = a1 * b2 - a2 * b1;
        double deltax = c1 * b2 - c2 * b1;
        double deltay = c2 * a1 - c1 * a2;
        if (delta == 0)
            return deltax == 0 && deltay == 0 ? -2 : -1;
        p.x = deltax / delta;
        p.y = deltay / delta;
        return 0;
    }

    public static boolean doIntersect2Line(Point p1, Point q1, Point p2, Point q2) {
        Point g = new Point(0, 0);
        int ret = hptb2(p1.y - q1.y, q1.x - p1.x, q1.x * p1.y - p1.x * q1.y, p2.y - q2.y, q2.x - p2.x,
                q2.x * p2.y - p2.x * q2.y, g);
        if (ret == -1)
            return false;
        if (ret == -2) {
            if (p1.x == q1.x) {
                if ((p2.y - p1.y) * (p2.y - q1.y) < 0)
                    return true;
                if ((q2.y - p1.y) * (q2.y - q1.y) < 0)
                    return true;
            } else {
                if ((q2.x - p1.x) * (q2.x - q1.x) < 0)
                    return true;
                if ((q2.x - p1.x) * (q2.x - q1.x) < 0)
                    return true;
            }
            return false;
        } // check diem giao
        if (p1.x == q1.x) {
            if ((g.y - p1.y) * (g.y - q1.y) > 0)
                return false;
        } else if ((g.x - p1.x) * (g.x - q1.x) > 0)
            return false;
        if (p2.x == q2.x) {
            if ((g.y - p2.y) * (g.y - q2.y) > 0)
                return false;
        } else if ((g.x - p2.x) * (g.x - q2.x) > 0)
            return false;
        return true;
    }

    public static Point getIntersectLine2Segment(Point p1, Point q1, double a, double b, double c) {
        Point g = new Point(0, 0);
        int ret = hptb2(p1.y - q1.y, q1.x - p1.x, q1.x * p1.y - p1.x * q1.y, a, b, c, g);
        if (ret == -1)
            return null;
        if (ret == -2) {
            return null;
        } // check diem giao
        if (p1.x == q1.x) {
            if ((g.y - p1.y) * (g.y - q1.y) > 0)
                return null;
        } else if ((g.x - p1.x) * (g.x - q1.x) > 0)
            return null;
        return g;
    }

    double sign(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }
}
