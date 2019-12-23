import algorithm.Dinitz;
import algorithm.EdmondKarp;
import algorithm.PreflowPush;
import entity.graph.Network;
import entity.graph.Vertex;
import entity.model.DirectionalSensor;
import entity.model.Sector;
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
//        processFileDirectionalSensors(new File("./data/S1/250_4_40.INP"));
//        System.exit(0);
        File logFolder = new File("./data");
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
                        if(f.getName().endsWith(".INP")){
                            inputFiles.add(f);
                        }
                    }
            }
        }
        System.out.println("Number of file .INP: " + inputFiles.size());
        for (File input: inputFiles) {
            processFileDirectionalSensors(input);
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
        System.out.println("\t\t2nd network: " + EdmondKarp.getMaximumFlow(network2) + "\t\ttime: " + timer.getTimeInMilliseconds() + " ms");

        timer.start();
        System.out.print("\tDinitz:\t\t\t1st network: " + Dinitz.getMaximumFlow(network));
        network2 = buildSecondNetwork(network, sensors);
        System.out.println("\t\t2nd network: " + Dinitz.getMaximumFlow(network2) + "\t\ttime: " + timer.getTimeInMilliseconds() + " ms");

        timer.start();
        System.out.print("\tPreflow-Push:\t1st network: " + Dinitz.getMaximumFlow(network));
        network2 = buildSecondNetwork(network, sensors);
        System.out.println("\t\t2nd network: " + PreflowPush.getMaximumFlow(network2) + "\t\ttime: " + timer.getTimeInMilliseconds() + " ms");

        //Check if method trackFlowPaths() achieves enough maximum flow
        int maximumFlow, sum;

        maximumFlow = EdmondKarp.getMaximumFlow(network);
        network.trackFlowPaths();
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;

        maximumFlow = Dinitz.getMaximumFlow(network);
        network.trackFlowPaths();
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;

        maximumFlow = PreflowPush.getMaximumFlow(network);
        network.trackFlowPaths();
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;

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
        network1.resetFlow();
        return network2;
    }

    private static Network buildFirstNetworkDirectionalSensor(List<DirectionalSensor> sensors) {
        List<Sector> allSectors = new ArrayList<>();
        for(DirectionalSensor sensor : sensors) {
            allSectors.addAll(sensor.sectors);
        }
        System.out.println("Added " + allSectors.size() + " sectors");

        Network network = new Network();
        network.setSource(network.newVertex());
        for(Sector sector: allSectors) network.newVertex().setSensor(sector.getMaster());
        network.setSink(network.newVertex());

        for (int i=0; i< allSectors.size(); i++){
            Sector sector = allSectors.get(i);
            if(sector.checkOverlapLeftBound()) Network.addEdge(network.getSource(), network.getVertex(i+1), sector.getC());
            if(sector.checkOverlapRightBound(300)) Network.addEdge(network.getSink(), network.getVertex(i+1), sector.getC());
            for (int j = i + 1; j < allSectors.size(); j++){
                Sector sector2 = allSectors.get(j);
                if(sector.checkOverlap(sector2))
                    Network.addEdge(network.getVertex(i+1), network.getVertex(j+1), Math.min(sector.getC(), sector2.getC()));
            }
        }
        return network;
    }


    private static void processFileDirectionalSensors(File input){
        Sensor.setNumberOfSensors(0);
        List<DirectionalSensor> sensors = new ArrayList<>();
        int W = 300, H = 150;

        try {
            System.out.println("Processing: " + input.getName());
            String[] items, items2;
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            items = line.split(" ");
            W = Integer.parseInt(items[0]);
            H = Integer.parseInt(items[1]);
            int numberOfSensors = Integer.parseInt(items[2]);
            line = bufferedReader.readLine();
            for (int i = 0; i < numberOfSensors; i++) {
                items = line.split(" ");
                line = bufferedReader.readLine();
                items2 = line.split(" ");
                double[] listVi = new double[Integer.parseInt(items[5])];
                for (int j = 0; j < listVi.length; j++)
                    listVi[j] = Double.parseDouble(items2[j]);
                DirectionalSensor s = new DirectionalSensor(Double.parseDouble(items[0]), Double.parseDouble(items[1]),
                        Double.parseDouble(items[2]), Integer.parseInt(items[3]), Double.parseDouble(items[4]), listVi);
                sensors.add(s);
                line = bufferedReader.readLine();
            }
            // expectedResult = Integer.parseInt(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Readed " + input.getName() + ": " + sensors.size() + " directional sensors");

        Network network = buildFirstNetworkDirectionalSensor(sensors), network2;

        Timer timer = new Timer();
        int sum, maximumFlow;
//
        timer.start();
        System.out.print("\tEdmond-Karp:\t1st network: " + (maximumFlow = EdmondKarp.getMaximumFlow(network)) + "\t\ttime #1:" + timer.getTimeInMilliseconds() + " ms");
        network2 = buildSecondNetworkDirectionalSensors(network, sensors);
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;
        System.out.println("\t\t2nd network: " + EdmondKarp.getMaximumFlow(network2) + "\t\ttime: " + timer.getTimeInMilliseconds() + " ms");

        timer.start();
        System.out.print("\tDinitz:\t\t\t1st network: " + (maximumFlow = Dinitz.getMaximumFlow(network)) + "\t\ttime #1:" + timer.getTimeInMilliseconds() + " ms");
        network2 = buildSecondNetworkDirectionalSensors(network, sensors);
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;
        System.out.println("\t\t2nd network: " + Dinitz.getMaximumFlow(network2) + "\t\ttime: " + timer.getTimeInMilliseconds() + " ms");

        timer.start();
        System.out.print("\tPreflow-Push:\t1st network: " + (maximumFlow = PreflowPush.getMaximumFlow(network)) + "\t\ttime #1:" + timer.getTimeInMilliseconds() + " ms");
        network2 = buildSecondNetworkDirectionalSensors(network, sensors);
        sum = 0;
        for(int flow: network.getFlows()) sum += flow;
        assert sum == maximumFlow;
        System.out.println("\t\t2nd network: " + Dinitz.getMaximumFlow(network2) + "\t\ttime #2: " + timer.getTimeInMilliseconds() + " ms");

//        timer.start();
//        System.out.print("\tDinitz:\t\t\t1st network: " + Dinitz.getMaximumFlow(network));
//        //network.resetFlow();
//        network2 = buildSecondNetworkDirectionalSensors(network, sensors);
//        System.out.println("\t\t2nd network: " + Dinitz.getMaximumFlow(network2) + "\t\ttime: " + timer.getTime() + " ms");

//        timer.start();
//        System.out.print("\tPreflow-Push:\t1st network: " + PreflowPush.getMaximumFlow(network));
//        network.resetFlow();
//        network2 = buildSecondNetworkDirectionalSensors(network, sensors);
//        System.out.println("\t\t2nd network: " + PreflowPush.getMaximumFlow(network2) + "\t\ttime: " + timer.getTime() + " ms");
    }

//    private static Network buildSecondNetworkDirectionalSensors(Network network1, List<DirectionalSensor> sensors)
//    {
//        network1.trackFlowPaths();
//
//        Network network2 = new Network();
//
//        int n = network1.numberOfVertices();
//
//        for(int i = 0; i < n; ++i)
//            network2.newVertex();
//
//        network2.setSource(network2.getVertex(0));
//        network2.setSink(network2.getVertex(n-1));
//
//        List<Integer> passedTime = network1.getPassedTime();
//
//        int[] passedTimeSensor = new int[sensors.size()+1];
//        int processingSensor = 0;
//        int startingI = 1;
//        String s0 = "";
//        for(int i = 1; i < n - 1; ++i){
//            //System.out.println("processingSensor: " + processingSensor);
//
//            DirectionalSensor sensor = sensors.get(processingSensor);
//            if(i == startingI) passedTimeSensor[processingSensor+1] = 0;
//
//            passedTimeSensor[processingSensor+1] += passedTime.get(i);
//            s0 = s0 + passedTime.get(i).toString() + " ";
//
//            if(i == startingI + sensor.sectors.size() - 1){         //last index of segment
//                System.out.println("processingSensor: " + processingSensor + "; " + s0 + "--> " + passedTimeSensor[processingSensor+1]);
//                s0 = "";
//                processingSensor++;
//                startingI = i+1;
//            }
//        }
//        for(int i = 1; i < sensors.size(); ++i)
//            if (passedTimeSensor[i] > 1)
//            {
//                Vertex vertex = network2.newVertex();
//                network2.getVertex(i).setDuplicateVertex(vertex);
//                Network.addDirectedEdge(network2.getVertex(i), vertex, sensors.get(i-1).getC());
//            }
//
//        List<Integer> flows = network1.getFlows();
//        List<ArrayList<Vertex>> flowPaths = network1.getFlowPaths();
//
//        for (int i = 0; i < flows.size(); ++i)
//        {
//            int flow = flows.get(i);
//            List<Vertex> path = flowPaths.get(i);
//
//            Vertex lastVertex = network2.getSource();
//            for(int j = 1; j < path.size(); ++j)
//            {
//                Vertex u = path.get(j);
//                if (passedTime.get(u.getId()) != 1)
//                {
//                    Network.addDirectedEdge(lastVertex, network2.getVertex(u.getId()), flow);
//                    if (u != network1.getSink())
//                        lastVertex = network2.getVertex(u.getId()).getDuplicateVertex();
//                }
//            }
//        }
//        return network2;
//    }

    private static Network buildSecondNetworkDirectionalSensors(Network network1, List<DirectionalSensor> sensors)
    {
        network1.trackFlowPaths();

        Network network2 = new Network();

        int n = sensors.size() + 2;

        for(int i = 0; i < n; ++i)
            network2.newVertex();

        network2.setSource(network2.getVertex(0));
        network2.setSink(network2.getVertex(n - 1));

        ArrayList<Integer> vertexPassedTime = network1.getPassedTime();

        ArrayList<Integer> sensorPassedTime = new ArrayList<>();

        for(Sensor sensor: sensors)
        {
            int passedTime = 0;
            for(Vertex vertex: network1.getVertices())
                if (vertex.getSensor() == sensor)
                    passedTime += vertexPassedTime.get(vertex.getId());
            sensorPassedTime.add(passedTime);
        }

        for(int i = 0; i < sensors.size(); ++i)
            if (sensorPassedTime.get(i) > 1)
            {
                Vertex vertex = network2.newVertex();
                Sensor sensor = sensors.get(i);
                vertex.setSensor(sensor);
                network2.getVertex(i + 1).setDuplicateVertex(vertex);
                Network.addDirectedEdge(network2.getVertex(i + 1), vertex, sensor.getC());
            }

        ArrayList<Integer> flows = network1.getFlows();
        ArrayList<ArrayList<Vertex>> flowPaths = network1.getFlowPaths();

        for(int i = 0; i < flows.size(); ++i)
        {
            int flow = flows.get(i);
            ArrayList<Vertex> path = flowPaths.get(i);

            Vertex lastVertex = network2.getSource();
            for(int j = 1; j < path.size() - 1; ++j)
            {
                Sensor sensor = path.get(j).getSensor();
                if (sensorPassedTime.get(sensor.getId() - 1) != 1)
                {
                    Network.addDirectedEdge(lastVertex, network2.getVertex(sensor.getId()), flow);
                    lastVertex = network2.getVertex(sensor.getId()).getDuplicateVertex();
                }
            }
            Network.addDirectedEdge(lastVertex, network2.getSink(), flow);
        }

        network1.resetFlow();
        return network2;
    }

}