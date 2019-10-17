package entity.graph;

import java.util.Vector;

public class Network {
    public Vertex[] vertices;
    private Vertex s;
    private Vertex t;
    private int n;

    public Network(Vertex s, Vertex t, int n) {
        this.s = s;
        this.t = t;
        this.n = n;
        this.vertices = new Vertex[this.n];
    }

    public int getN() {
        return n;
    }

    public Vertex getS() {
        return s;
    }

    public Vertex getT() {
        return t;
    }

    public static void AddEdge(Vertex u, Vertex v, int capacity){
        u.addAdjaction(v, capacity, v.getCurrentIndex());
        v.addAdjaction(u, capacity, u.getCurrentIndex() - 1);
    }

    public void printNetwork(){
        System.out.println("===NETWORK===");
        System.out.println("No. of sensors: " + n);
        for (int i = 0; i < n; i++){
            String vertexInfo = "id= "+i+"; adjacents: ";
            Vertex v = vertices[i];
            Vector<Edge> edges = v.getAdjacents();
            for (int k = 0; k < edges.size(); k++){
                vertexInfo += "(adj: " + edges.get(k).getV().getId()+ ", cap: " + edges.get(k).getCapacity()+", corIndex: " + edges.get(k).getCorrespondingIndex() + "); ";
            }
            System.out.println(vertexInfo);
        }
        System.out.println("=============");
    }
}
