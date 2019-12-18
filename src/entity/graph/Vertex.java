package entity.graph;

import java.util.ArrayList;

public class Vertex {
    private ArrayList<Edge> adjacencies;
    private int id;

    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

    private Vertex duplicateVertex;

    public Vertex() {
        adjacencies = new ArrayList<Edge>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCurrentIndex(){
        return adjacencies.size();
    }

    public ArrayList<Edge> getAdjacencies(){
        return this.adjacencies;
    }

    public Edge getEdge(int index){
        return this.adjacencies.get(index);
    }

    public void addAdjacency(Vertex v, int capacity, int correspondingIndex){
        Edge e = new Edge(this,v,capacity, correspondingIndex);
        adjacencies.add(e);
    }

    public void resetFlow()
    {
        for(Edge edge: adjacencies)
            edge.setFlow(0);
    }

}
