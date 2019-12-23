package entity.model;

import java.util.ArrayList;

public class DirectionalSensor extends Sensor {
    private double halfAngle;
    //private double[] directions;
    public ArrayList<Sector> sectors;
    public DirectionalSensor(double x, double y, double r, int c, double halfAngle, double[] directions) {
        super(x, y, r, c);
        //this.directions = directions;
        this.halfAngle = halfAngle;
        sectors = new ArrayList<>();
        for (double direction : directions) {
            addSector(x, y, r, c, halfAngle, direction);
        }
    }

    public void addSector(double x, double y, double r, int c, double alpha, double viangle){
        Sector sector = new Sector(x, y, r, alpha, viangle, c,this);
        sectors.add(sector);
    }

}
