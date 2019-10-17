package entity.graph;

import java.util.Vector;

public class Vertex {
    private Vector<Edge> adjacents;

    private static int numberOfVertices = 0;
    private int id;

    public Vertex() {
        adjacents = new Vector<Edge>();
        this.id = numberOfVertices++;
    }

    public void clear(){
        numberOfVertices = 0;
    }

    public static int getNumberOfVertices() {
        return numberOfVertices;
    }

    public int getId() {
        return id;
    }

    public int getCurrentIndex(){
        return adjacents.size();
    }

    public Vector<Edge> getAdjacents(){
        return this.adjacents;
    }

    public Edge getEdge(int index){
        return this.adjacents.get(index);
    }

    public void addAdjaction(Vertex v, int capacity, int correspondingIndex){
        Edge e = new Edge(this,v,capacity, correspondingIndex);
        adjacents.add(e);
    }
}
