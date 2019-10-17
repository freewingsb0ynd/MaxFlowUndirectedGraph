package entity.graph;

public class Edge {
    private Vertex u;
    private Vertex v;
    private int capacity;
    private int flow;
    private int correspondingIndex;

    public Edge(Vertex u, Vertex v, int capacity, int correspondingIndex) {
        this.u = u;
        this.v = v;
        this.capacity = capacity;
        this.correspondingIndex = correspondingIndex;
    }

    public Vertex getU() {
        return u;
    }

    public Vertex getV() {
        return v;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getCorrespondingIndex() {
        return correspondingIndex;
    }

    public void setCorrespondingIndex(int correspondingIndex) {
        this.correspondingIndex = correspondingIndex;
    }
}
