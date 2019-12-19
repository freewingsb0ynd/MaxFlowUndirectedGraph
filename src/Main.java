import algorithm.Dinitz;
import algorithm.EdmondKarp;
import algorithm.PreflowPush;
import entity.graph.Network;
import entity.graph.Vertex;
import entity.model.Sensor;
import util.Timer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        processFile(new File("./inp/04/04.inp"));
//        System.exit(0);
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
                if (files != null)
                    for (File f : files) {
                        if(f.getName().endsWith(".inp")){
                            inputFiles.add(f);
                        }
                    }
            }
        }
        System.out.println("No. of file .inp: " + inputFiles.size());
        for (File input: inputFiles) {
            processFile(input);
        }
    }

    private static void processFile(File input){
        List<Sensor> sensors = new ArrayList<>();

        try {
            FileReader fileReader= new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            int numberOfSensors = Integer.parseInt(line);
            line = bufferedReader.readLine();
            for (int i = 0; i < numberOfSensors; i++){
                String[] items = line.split(" ");
                Sensor s = new Sensor(Double.parseDouble(items[0]),Double.parseDouble(items[1]),Double.parseDouble(items[2]),Integer.parseInt(items[3]));
                sensors.add(s);
                line = bufferedReader.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Checked " + input.getName() + ": " + sensors.size());

        Network network = buildFirstNetwork(sensors), network2;

        Timer timer = new Timer();

        timer.start();
        System.out.print("\tEdmond-Karp:\t1st network: " + EdmondKarp.getMaximumFlow(network));
        network2 = buildSecondNetwork(network, sensors);
        System.out.println("\t\t2nd network: " + EdmondKarp.getMaximumFlow(network2) + "\t\ttime: " + timer.getTime() + " ms");

        timer.start();
        System.out.print("\tDinitz:\t\t\t1st network: " + Dinitz.getMaximumFlow(network));
        network2 = buildSecondNetwork(network, sensors);
        System.out.println("\t\t2nd network: " + Dinitz.getMaximumFlow(network2) + "\t\ttime: " + timer.getTime() + " ms");

        timer.start();
        System.out.print("\tPreflow-Push:\t1st network: " + PreflowPush.getMaximumFlow(network));
        network2 = buildSecondNetwork(network, sensors);
        System.out.println("\t\t2nd network: " + PreflowPush.getMaximumFlow(network2) + "\t\ttime: " + timer.getTime() + " ms");

    }

    private static Network buildFirstNetwork(List<Sensor> sensors) {
        Network network = new Network();

        network.setSource(network.newVertex());

        for(int i = 0; i < sensors.size(); i++) network.newVertex();

        network.setSink(network.newVertex());

        for (int i=0; i< sensors.size(); i++){
            Sensor sensor = sensors.get(i);
            if(sensor.checkOverlapLeftBound()) Network.addEdge(network.getSource(), network.getVertex(i+1), sensor.getC());
            if(sensor.checkOverlapRightBound(300)) Network.addEdge(network.getSink(), network.getVertex(i+1), sensor.getC());
            for (int j = i + 1; j < sensors.size(); j++){
                Sensor sensor2 = sensors.get(j);
                if(sensor.checkOverlap(sensor2))
                    Network.addEdge(network.getVertex(i+1), network.getVertex(j+1), Math.min(sensor.getC(), sensor2.getC()));
            }
        }
        return network;
    }

    private static Network buildSecondNetwork(Network network1, List<Sensor> sensors)
    {
        network1.trackFlowPaths();

        Network network2 = new Network();

        int n = network1.numberOfVertices();

        for(int i = 0; i < n; ++i)
            network2.newVertex();

        network2.setSource(network2.getVertex(0));
        network2.setSink(network2.getVertex(n-1));

        List<Integer> passedTime = network1.getPassedTime();

        for(int i = 1; i < n - 1; ++i)
            if (passedTime.get(i) > 1)
            {
                Vertex vertex = network2.newVertex();
                network2.getVertex(i).setDuplicateVertex(vertex);
                Network.addDirectedEdge(network2.getVertex(i), vertex, sensors.get(i-1).getC());
            }

        List<Integer> flows = network1.getFlows();
        List<ArrayList<Vertex>> flowPaths = network1.getFlowPaths();

        for (int i = 0; i < flows.size(); ++i)
        {
            int flow = flows.get(i);
            List<Vertex> path = flowPaths.get(i);

            Vertex lastVertex = network2.getSource();
            for(int j = 1; j < path.size(); ++j)
            {
                Vertex u = path.get(j);
                if (passedTime.get(u.getId()) != 1)
                {
                    Network.addDirectedEdge(lastVertex, network2.getVertex(u.getId()), flow);
                    if (u != network1.getSink())
                        lastVertex = network2.getVertex(u.getId()).getDuplicateVertex();
                }
            }
        }
        return network2;
    }

}