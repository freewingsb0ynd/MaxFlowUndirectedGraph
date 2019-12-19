package entity.graph;

import util.Helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Network {
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private Vertex source;
    private Vertex sink;

    private ArrayList<Integer> passedTime;
    private ArrayList<Integer> flows;
    private ArrayList<ArrayList<Vertex>> flowPaths;
    private ArrayList<Edge> traces;

    public List<Integer> getPassedTime() {
        return passedTime;
    }

    public List<Integer> getFlows() {
        return flows;
    }

    public List<ArrayList<Vertex>> getFlowPaths() {
        return flowPaths;
    }

    public int numberOfVertices() {
        return vertices.size();
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSink(Vertex sink) {
        this.sink = sink;
    }

    public Vertex getSink() {
        return sink;
    }

    public Vertex newVertex()
    {
        Vertex vertex = new Vertex();
        vertex.setId(numberOfVertices());
        vertices.add(vertex);
        return vertex;
    }

    public Vertex getVertex(int i)
    {
        return vertices.get(i);
    }

    public ArrayList<Vertex> getVertices()
    {
        return vertices;
    }

    public static void addEdge(Vertex u, Vertex v, int capacity){
        u.addAdjacency(v, capacity, v.getCurrentIndex());
        v.addAdjacency(u, capacity, u.getCurrentIndex() - 1);
    }

    public static void addDirectedEdge(Vertex u, Vertex v, int capacity){
        u.addAdjacency(v, capacity, v.getCurrentIndex());
        v.addAdjacency(u, 0, u.getCurrentIndex() - 1);
    }

    public void resetFlow()
    {
        for(Vertex vertex: vertices)
            vertex.resetFlow();
    }

    private boolean findFlowPath()
    {
        traces = new ArrayList<>();
        for(int i = 0; i < vertices.size(); ++i) traces.add(null);

        Queue<Vertex> queue = new LinkedList<>();

        queue.add(source);

        while (!queue.isEmpty())
        {
            Vertex u = queue.poll();
            ArrayList<Edge> adjacencies = u.getAdjacencies();
            for(Edge edge: adjacencies)
            {
                Vertex v = edge.getV();
                if (traces.get(v.getId()) == null && edge.getFlow() > 0)
                {
                    traces.set(v.getId(), edge);
                    if (v == sink)
                        return true;
                    queue.add(v);
                }
            }
        }
        return false;
    }

    private int getFlowPath(Vertex current, int delta)
    {
        if (current == source)
        {
            Helper.getLast(flowPaths).add(current);
            return delta;
        }
        Helper.increaseAtIndex(passedTime, current.getId(), 1);
        Edge edge = traces.get(current.getId());
        delta = getFlowPath(edge.getU(), Math.min(delta, edge.getFlow()));
        Helper.getLast(flowPaths).add(current);
        edge.incFlow(-delta);
        edge.getReversedEdge().incFlow(delta);
        return delta;
    }

    public void trackFlowPaths()
    {
        passedTime = new ArrayList<>();
        for(int i = 0; i < vertices.size(); ++i) passedTime.add(0);

        flows = new ArrayList<>();
        flowPaths = new ArrayList<>();

        while (findFlowPath())
        {
            flowPaths.add(new ArrayList<>());
            flows.add(getFlowPath(sink, Integer.MAX_VALUE));
        }

        passedTime.set(source.getId(), -1);
        passedTime.set(sink.getId(), -1);

//        System.out.println();
//        for(int i = 1; i <= flowPaths.size(); ++i)
//        {
//            System.out.print(i + ":\t" + flows.get(i-1) + "\t\t");
//            for(Vertex v : flowPaths.get(i-1)) System.out.print(v.getId() + "\t");
//            System.out.println();
//        }

        resetFlow();
    }

    public void printNetwork(){
        System.out.println("===NETWORK===");
        int n = numberOfVertices();
        System.out.println("No. of sensors: " + n);
        for (int i = 0; i < n; i++){
            String vertexInfo = "id= "+i+"; adjacencies: ";
            Vertex v = vertices.get(i);
            ArrayList<Edge> edges = v.getAdjacencies();
            for (Edge edge : edges) {
                vertexInfo = vertexInfo.concat("(adj: " + edge.getV().getId() + ", cap: " + edge.getCapacity() + ", corIndex: " + edge.getCorrespondingIndex() + "); ");
            }
            System.out.println(vertexInfo);
        }
        System.out.println("=============");
    }

}
