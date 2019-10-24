package entity.graph;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Network {
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private Vertex s;
    private Vertex t;

    private ArrayList<Integer> passedTime;
    private ArrayList<Integer> flows;
    private ArrayList<ArrayList<Vertex>> flowPaths;

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

    public void setS(Vertex s) {
        this.s = s;
    }

    public Vertex getS() {
        return s;
    }

    public void setT(Vertex t) {
        this.t = t;
    }

    public Vertex getT() {
        return t;
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

    public static void addEdge(Vertex u, Vertex v, int capacity){
        u.addAdjacent(v, capacity, v.getCurrentIndex());
        v.addAdjacent(u, capacity, u.getCurrentIndex() - 1);
    }

    public static void addDirectedEdge(Vertex u, Vertex v, int capacity){
        u.addAdjacent(v, capacity, v.getCurrentIndex());
        v.addAdjacent(u, 0, u.getCurrentIndex() - 1);
    }

    public void resetFlow()
    {
        for(Vertex vertex: vertices)
            vertex.resetFlow();
    }

    private int getOneFlowPath(Vertex u, ArrayList<Vertex> currentPath, int flow)
    {
        passedTime.set(u.getId(), passedTime.get(u.getId()) + 1);
        currentPath.add(u);
        if (u == t)
            return flow;
        ArrayList<Edge> adjacents = u.getAdjacents();
        for(Edge edge: adjacents)
        {
            Vertex v = edge.getV();
            if (edge.getFlow() > 0)
            {
                int delta = getOneFlowPath(v, currentPath, min(flow, edge.getFlow()));
                edge.incFlow(-delta);
                return delta;
            }
        }
        return 0;
    }

    public void trackFlowPaths()
    {
        passedTime = new ArrayList<>();
        for(int i = 0; i < vertices.size(); ++i)
            passedTime.add(0);

        flows = new ArrayList<>();
        flowPaths = new ArrayList<>();

        while (true)
        {
            ArrayList<Vertex> path = new ArrayList<>();
            int delta = getOneFlowPath(s, path, Integer.MAX_VALUE);
            if (delta == 0) break;
            else
            {
                flows.add(delta);
                flowPaths.add(path);
            }
        }

        passedTime.set(s.getId(), -1);
        passedTime.set(t.getId(), -1);
    }

    public void printNetwork(){
        System.out.println("===NETWORK===");
        int n = numberOfVertices();
        System.out.println("No. of sensors: " + n);
        for (int i = 0; i < n; i++){
            String vertexInfo = "id= "+i+"; adjacents: ";
            Vertex v = vertices.get(i);
            ArrayList<Edge> edges = v.getAdjacents();
            for (Edge edge : edges) {
                vertexInfo += "(adj: " + edge.getV().getId() + ", cap: " + edge.getCapacity() + ", corIndex: " + edge.getCorrespondingIndex() + "); ";
            }
            System.out.println(vertexInfo);
        }
        System.out.println("=============");
    }

}
