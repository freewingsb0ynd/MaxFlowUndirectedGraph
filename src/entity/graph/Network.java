package entity.graph;

import java.util.ArrayList;
import java.util.Vector;

public class Network {
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();;
    private Vertex s;
    private Vertex t;

    public int size() {
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
        vertex.setId(size());
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

    public void printNetwork(){
        System.out.println("===NETWORK===");
        int n = size();
        System.out.println("No. of sensors: " + n);
        for (int i = 0; i < n; i++){
            String vertexInfo = "id= "+i+"; adjacents: ";
            Vertex v = vertices.get(i);
            ArrayList<Edge> edges = v.getAdjacents();
            for (int k = 0; k < edges.size(); k++){
                vertexInfo += "(adj: " + edges.get(k).getV().getId()+ ", cap: " + edges.get(k).getCapacity()+", corIndex: " + edges.get(k).getCorrespondingIndex() + "); ";
            }
            System.out.println(vertexInfo);
        }
        System.out.println("=============");
    }
}
