package entity.graph;

import java.util.Vector;

public class Vertex {
    private Vector<Edge> adjaccents;

    public Vertex() {
        adjaccents = new Vector<Edge>();
    }

    public int getCurrentIndex(){
        return adjaccents.size();
    }

    public Vector<Edge> getAdjaccents(){
        return this.adjaccents;
    }

    public Edge getEdge(int index){
        return this.adjaccents.get(index);
    }

    public void addAdjaction(Vertex v, int capacity, int correspondingIndex){
        Edge e = new Edge(this,v,capacity, correspondingIndex);
        adjaccents.add(e);
    }
}
