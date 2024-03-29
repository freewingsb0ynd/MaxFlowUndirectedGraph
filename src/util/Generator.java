package util;

import entity.model.DirectionalSensor;
import entity.model.Sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generator {
    // generate random n directional sensor, each contain m max angle direction by ratio p%: 1 direction, 1-p %: m maxdirection
    //
    public static List<DirectionalSensor> directionalizeOmniDirectionalSensors(List<Sensor> initialSensors, double percentSingleDirectionSensor, int maxDirections, double sectorAngle){
        List<DirectionalSensor> directionalSensors = new ArrayList<>();
        int[] directionsEachSensor = new int[initialSensors.size()];
        double[] cuts = new double[maxDirections];
        cuts[0] = 1.0;
        for (int i = 1; i<maxDirections; i++) {
            cuts[i] = 1 + i*(1/percentSingleDirectionSensor-1)/(maxDirections-1);
            System.out.println("cut " + i+ ": " + cuts[i]);
        }
        //System.out.println("ranMax " + 1/percentSingleDirectionSensor);
        int singleDirectionSensors = 0;
        for (int i = 0; i<directionsEachSensor.length; i++){
            double rd = randomDouble(0, 1/percentSingleDirectionSensor);
            for(int j = maxDirections; j > 0; j--){
                //System.out.println("j now: " + j);
                if(rd > cuts[j-1] ) {
                    directionsEachSensor[i] = j+1;
                    break;
                } else if(j == 1) directionsEachSensor[i] = 1;
            }
            //System.out.println("rd " + i+ "-th: " + rd + ",  \tdirections " + i+ "-th sensor: " + directionsEachSensor[i]);
            if (directionsEachSensor[i] == 1) singleDirectionSensors++;
        }
        double newP = 1.0 * singleDirectionSensors / directionsEachSensor.length;
        System.out.println("singleDirectionSensors: " + singleDirectionSensors + ",  \tnew p: " + newP);
        for (int i = 0; i < initialSensors.size(); i++){
            Sensor initSensor = initialSensors.get(i);
            double[] startAngles = randomSectorStartAngles(directionsEachSensor[i], sectorAngle,false);
        }
        return directionalSensors;
    }

    public static double[] randomSectorStartAngles(int sectors, double sectorAngle, boolean isOverlapMode){
        double[] arr = new double[sectors];
        double[] rand = new double[sectors];
        if(isOverlapMode){
            for (int i = 0; i < sectors; i++){
                rand[i] = randomDouble(0, 2 * Math.PI);
            }
            arr = Arrays.stream(rand).sorted().toArray(); //sorted with natural order
        } else {
            for (int i = 0; i < sectors; i++){
                rand[i] = randomDouble(0, 2 * Math.PI- sectors * sectorAngle);
            }
            rand = Arrays.stream(rand).sorted().toArray(); //sorted with natural order
            for (int i = 0; i < sectors; i++){
                arr[i] = rand[i] + sectorAngle * i;
            }
        }
        return arr;
    }

    public static double randomDouble(double rangeMin, double rangeMax){
        double random = new Random().nextDouble();
        return rangeMin + (random * (rangeMax - rangeMin));
    }

}
