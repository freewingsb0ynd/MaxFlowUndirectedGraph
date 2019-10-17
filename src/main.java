import entity.graph.Network;
import entity.graph.Vertex;
import entity.model.Sensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        //System.out.println("hello");
        File logFolder = new File("./inp");
        File[] dirs = logFolder.listFiles();
        List<File> inputFiles = new ArrayList<>();


        if (dirs == null) {
            return;
        }
        for (File dir : dirs) {
            if (dir.isDirectory()){
                //System.out.println("Processing folder: " + dir.getName());
                File[] files = dir.listFiles();
                for (File f : files) {
                    //System.out.println("Processing file: " + f.getName());
                    if(f.getName().endsWith(".inp")){
                        inputFiles.add(f);
                        //System.out.println("Added file: " + f.getName());
                    }
                }
            }
        }
        System.out.println("No. of file .inp: " + inputFiles.size());
        for (File input: inputFiles) {

        }

        processFile(inputFiles.get(0));
    }

    private static void processFile(File input){
        List<Sensor> sensors = new ArrayList<>();
        try {
            System.out.println("Processing: " + input.getName());
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int numberOfSensors = Integer.parseInt(line);
            line = bufferedReader.readLine();
            for (int i = 0; i < numberOfSensors; i++){
                String items[] = line.split(" ");
                Sensor s = new Sensor(Double.parseDouble(items[0]),Double.parseDouble(items[1]),Double.parseDouble(items[2]),Integer.parseInt(items[3]));
                sensors.add(s);
                line = bufferedReader.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Processed done, sensors: " + sensors.size());

        Vertex[] tempVertices = new Vertex[sensors.size()+2];
        Vertex s = new Vertex();
        tempVertices[0] = s;
        for (int i=0; i< sensors.size(); i++){
            Vertex realVertex = new Vertex();
            tempVertices[i+1] = realVertex;
        }
        Vertex t = new Vertex();
        tempVertices[sensors.size()+1] = t;

        Network network = new Network(s,t,tempVertices.length);
        network.vertices = tempVertices;

        for (int i=0; i< sensors.size(); i++){
            Sensor sensor = sensors.get(i);
            if(sensor.checkOverlapLeftBound()) Network.AddEdge(s, network.vertices[i+1], sensor.getC());
            if(sensor.checkOverlapRightBound(300)) Network.AddEdge(t, network.vertices[i+1], sensor.getC());
            for (int j = i + 1; j< sensors.size(); j++){
                Sensor sensor2 = sensors.get(j);
                if(sensor.checkOverlap(sensor2)) Network.AddEdge(network.vertices[i+1], network.vertices[j+1], Math.min(sensor.getC(), sensor2.getC()));
            }
        }
        network.printNetwork();

        //System.out.println("Sensors 15: r=" + sensors.get(15).getR());
    }

}