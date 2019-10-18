package entity.graph;

import java.util.ArrayList;
import java.util.Vector;

public class Vertex {
    private ArrayList<Edge> adjacents;
    private int id;

    public Vertex() {
        adjacents = new ArrayList<Edge>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCurrentIndex(){
        return adjacents.size();
    }

    public ArrayList<Edge> getAdjacents(){
        return this.adjacents;
    }

    public Edge getEdge(int index){
        return this.adjacents.get(index);
    }

    public void addAdjacent(Vertex v, int capacity, int correspondingIndex){
        Edge e = new Edge(this,v,capacity, correspondingIndex);
        adjacents.add(e);
    }

    public void resetFlow()
    {
        for(Edge e: adjacents)
            e.setFlow(0);
    }

}
