package entity.model;

import util.Intersect;
import util.Point;

import java.util.ArrayList;

public class Sector {
    public Point center;
    public double r;
    public double viangle;
    public double alpha;
    public int c;
    public DirectionalSensor master;

    public Point vex1, vex2;

    public ArrayList<Sensor> listSector;


    public Sector(double x, double y, double r, double viangle, double alpha, int c, DirectionalSensor mt) {
        center = new Point(x, y);
        this.r = r;
        this.c = c;
        this.viangle = viangle;
        this.alpha = alpha;
        listSector = new ArrayList<Sensor>();
        vex1 = new Point(center, viangle + alpha, r);
        vex2 = new Point(center, viangle - alpha, r);
        this.master = mt;
    }

    public double getX() {
        return center.x;
    }

    public double getY() {
        return center.y;
    }

    public double getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public DirectionalSensor getMaster() {
        return master;
    }

    public boolean checkOverlapRightBound(double width) {
        if (viangle >= alpha && viangle <= 2*Math.PI - alpha) {
            double xmax = Math.max(center.x, Math.max(vex1.x, vex2.x));
            if (xmax > width)
                return true;
            else
                return false;
        } else
            return r > width - center.x;
    }

    public boolean checkOverlapLeftBound() {
        if (viangle <= Math.PI - alpha || viangle >= Math.PI + alpha) {
            double xmin = Math.min(center.x, Math.min(vex1.x, vex2.x));
            if (xmin < 0)
                return true;
            else
                return false;
        } else
            return center.x < r;
    }

    public double distanceToLeftBound() {
        if (viangle <= Math.PI - alpha || viangle >= Math.PI + alpha) {
            double xmin = Math.min(center.x, Math.min(vex1.x, vex2.x));
            return xmin;
        } else
            return center.x - r;
    }

    public boolean isInside(Point p) {
        if (p == null)
            return false;
        double dx = p.x - center.x;
        double dy = p.y - center.y;
        double tvh = dx * Math.cos(viangle) + dy * Math.sin(viangle);
        double dd = Math.sqrt(dx * dx + dy * dy);
        // return ((dd > s.r) || (tvh < dd * Math.cos(s.angle))) ? 0 : 1;
        if ((dd > r) || (tvh < dd * Math.cos(alpha)))
            return false;
        return true;
    }

    public boolean checkOverlap(Sector s) {
        if (center.distance(s.center) >= s.r + r || s.master == master)
            return false;
        if (isInside(s.center) || isInside(s.vex1) || isInside(s.vex2) || s.isInside(center) || s.isInside(vex1)
                || s.isInside(vex2))
            return true;
        if (Intersect.doIntersect2Line(center, vex1, s.center, s.vex1)
                || Intersect.doIntersect2Line(center, vex2, s.center, s.vex2)
                || Intersect.doIntersect2Line(center, vex1, s.center, s.vex2)
                || Intersect.doIntersect2Line(center, vex2, s.center, s.vex1))
            return true;
        if (isInside(center.projectionToSegment(s.center, s.vex1))
                || isInside(center.projectionToSegment(s.center, s.vex2))
                || s.isInside(s.center.projectionToSegment(center, vex1))
                || s.isInside(s.center.projectionToSegment(center, vex2)))
            return true;
        double dx = s.center.x - center.x;
        double dy = s.center.y - center.y;
        double tvh1 = dx * Math.cos(viangle) + dy * Math.sin(viangle);
        double tvh2 = -dx * Math.cos(s.viangle) - dy * Math.sin(s.viangle);
        double dd = Math.sqrt(dx * dx + dy * dy);
        if ((tvh1 > dd * Math.cos(alpha)) && (tvh2 > dd * Math.cos(s.alpha)))
            return true;
        // TODO: Check if sensor S1 with vector direction d1 overlaps sensor S2 with
        // vector direction d2
        double d = (center.x - s.getX()) * (center.x - s.getX()) + (center.y - s.getY()) * (center.y - s.getY());
        if (d > (r + s.getR()) * (r + s.getR())) {
            return false;
        }
        return true;
    }
}
