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

        for (int i =0; i <directions.length; i++){
            addSector(x, y, r, c, halfAngle, directions[i]);
        }
    }

    public double getHalfAngle() {
        return halfAngle;
    }

    public void addSector(double x, double y, double r, int c, double alpha, double viangle){
        Sector sector = new Sector(x, y, r, alpha, viangle, c,this);
        sectors.add(sector);
    }

}
