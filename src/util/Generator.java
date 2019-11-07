package util;

import entity.model.DirectionalSensor;
import entity.model.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
//generate random n directional sensor, each contain m max angle direction by ratio p%: 1 direction, 1-p %: m maxdirection
    public List<DirectionalSensor> directionalizeOmniDirectionalSensors(List<Sensor> initialSensors){
        List<DirectionalSensor> directionalSensors = new ArrayList<>();
        for (Sensor sensor: initialSensors){

        }
        return directionalSensors;
    }

    public static double randomDouble(double rangeMin, double rangeMax){
        double random = new Random().nextDouble();
        return rangeMin + (random * (rangeMax - rangeMin));

    }

}
