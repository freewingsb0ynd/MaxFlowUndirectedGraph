package entity.model;

public class DirectionalSensor extends Sensor {
    private double halfAngle;
    private double[] directions;
    public DirectionalSensor(double x, double y, double r, int c, double halfAngle, double[] directions) {
        super(x, y, r, c);
        this.directions = directions;
        this.halfAngle = halfAngle;
    }

    public double getHalfAngle() {
        return halfAngle;
    }

    public double[] getDirections() {
        return directions;
    }



}
