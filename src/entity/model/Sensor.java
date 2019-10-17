package entity.model;

public class Sensor {
    private double x;
    private double y;
    private double r;
    private int c;

    public Sensor(double x, double y, double r, int c) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.c = c;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public boolean checkOverlapRightBound(double width)
    {
        return r > width - x;
    }

    public boolean checkOverlapLeftBound()
    {
        return x < r;
    }

    public boolean checkOverlap(Sensor s)
    {
        //TODO: Check if sensor S1 with vector direction d1 overlaps sensor S2 with vector direction d2
        double d = (x-s.getX())*(x-s.getX())+(y-s.getY())*(y-s.getY());
        if(d>(r+s.getR())*(r+s.getR())){
            return false;
        }
        return true;
    }
}
