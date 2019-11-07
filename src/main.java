import algorithm.Dinitz;
import algorithm.EdmondKarp;
import entity.graph.Network;
import entity.graph.Vertex;
import entity.model.Sensor;
import util.Generator;

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
            processFile(input);
        }

        //processFile(inputFiles.get(11));
        //processFile(inputFiles.get(0));

    }

    private static void processFile(File input){
        List<Sensor> sensors = new ArrayList<>();

//        int expectedResult = 0;
        try {
//            System.out.println("Processing: " + input.getName());
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
//            expectedResult = Integer.parseInt(line);
        }catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("Processed done, sensors: " + sensors.size());

        //Generator.directionalizeOmniDirectionalSensors(sensors, 0.8, 4);

        Network network = buildFirstNetwork(sensors);

//        network.printNetwork();

        //EdmondKarp.maximumFlow(network);


//        Network network2 = new Network();
//        for(int i = 0; i < 6; ++i) network2.newVertex();
//        network2.setS(network2.getVertex(0));
//        network2.setT(network2.getVertex(5));
//        network2.addDirectedEdge(network2.getVertex(0), network2.getVertex(1), 5);
//        network2.addDirectedEdge(network2.getVertex(0), network2.getVertex(2), 5);
//        network2.addDirectedEdge(network2.getVertex(1), network2.getVertex(3), 6);
//        network2.addDirectedEdge(network2.getVertex(1), network2.getVertex(4), 3);
//        network2.addDirectedEdge(network2.getVertex(2), network2.getVertex(3), 3);
//        network2.addDirectedEdge(network2.getVertex(2), network2.getVertex(4), 1);
//        network2.addDirectedEdge(network2.getVertex(3), network2.getVertex(5), 6);
//        network2.addDirectedEdge(network2.getVertex(4), network2.getVertex(5), 6);
//
//        System.out.println(Dinitz.maximumFlow(network2));
//
        System.out.println("Checked " + input.getName() + ": " + sensors.size() + " sensors");
        double start = System.nanoTime();
        EdmondKarp.maximumFlow(network);
        double time1E = (System.nanoTime() - start)/1000;

        network.resetFlow();
        start = System.nanoTime();
        Dinitz.maximumFlow(network);
        double time1D = (System.nanoTime() - start)/1000;

        network.resetFlow();
        start = System.nanoTime();
        Network network2 = buildSecondNetwork(network, sensors);
        double timeBuildSecondNetwork = (System.nanoTime() - start)/1000;

        start = System.nanoTime();
        EdmondKarp.maximumFlow(network2);
        double time2E = (System.nanoTime() - start)/1000;

        network2.resetFlow();
        start = System.nanoTime();
        Dinitz.maximumFlow(network2);
        double time2D = (System.nanoTime() - start)/1000;

//        System.out.println("\tEdmond-Karp: " + EdmondKarp.maximumFlow(network));
//        System.out.println("-> Time:" + ((System.nanoTime() - start)/1000));
//        network.resetFlow();
//        start = System.nanoTime();
//        System.out.println("\tDinitz: " + Dinitz.maximumFlow(network));
//        System.out.println("-> Time:" + ((System.nanoTime() - start)/1000));
        System.out.println("\tEdmond-Karp Time: \t" + (time1E + timeBuildSecondNetwork + time2E));
        System.out.println("\tDinitz Time:\t\t" + (time1D + timeBuildSecondNetwork + time2D));
    }

    private static Network buildFirstNetwork(List<Sensor> sensors) {
        Network network = new Network();

        network.setS(network.newVertex());

        for(int i = 0; i < sensors.size(); i++) network.newVertex();

        network.setT(network.newVertex());

        for (int i=0; i< sensors.size(); i++){
            Sensor sensor = sensors.get(i);
            if(sensor.checkOverlapLeftBound()) Network.addEdge(network.getS(), network.getVertex(i+1), sensor.getC());
            if(sensor.checkOverlapRightBound(300)) Network.addEdge(network.getT(), network.getVertex(i+1), sensor.getC());
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

        for(int i = 0; i < network1.numberOfVertices(); ++i)
            network2.newVertex();

        network2.setS(network2.getVertex(0));
        network2.setT(network2.getVertex(n-1));

        List<Integer> passedTime = network1.getPassedTime();

        for(int i = 1; i < n - 1; ++i)
            if (passedTime.get(i) > 1)
            {
                Vertex vertex = network2.newVertex();
                network2.getVertex(i).setDuplicateVertex(vertex);
                Network.addDirectedEdge(network2.getVertex(i), vertex, sensors.get(i).getC());
            }

        List<Integer> flows = network1.getFlows();
        List<ArrayList<Vertex>> flowPaths = network1.getFlowPaths();

        for (int i = 0; i < flows.size(); ++i)
        {
            int flow = flows.get(i);
            List<Vertex> path = flowPaths.get(i);

            Vertex lastVertex = network2.getS();
            for(int j = 1; j < path.size(); ++j)
            {
                Vertex u = path.get(j);
                if (passedTime.get(j) != 0)
                {
                    Network.addDirectedEdge(lastVertex, network2.getVertex(u.getId()), flow);
                    if (u != network1.getT())
                        lastVertex = network2.getVertex(u.getId()).getDuplicateVertex();
                }
            }
        }
        return network2;
    }

}