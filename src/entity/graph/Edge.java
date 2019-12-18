package entity.graph;

public class Edge {
    private Vertex u;
    private Vertex v;
    private int capacity;
    private int flow;
    private int correspondingIndex;
//    private int passed;

    public Edge(Vertex u, Vertex v, int capacity, int correspondingIndex) {
        this.u = u;
        this.v = v;
        this.capacity = capacity;
        this.correspondingIndex = correspondingIndex;
//        this.passed = 0;
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

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getResidualFlow()
    {
        return capacity - flow;
    }

    public void incFlow(int delta)
    {
        flow += delta;
    }

    public Edge getReversedEdge()
    {
        return v.getEdge(correspondingIndex);
    }

    public int getCorrespondingIndex() {
        return correspondingIndex;
    }

//    public void setPassed(int passed) {
//        this.passed = passed;
//    }
//
//    public int getPassed() {
//        return passed;
//    }
}
