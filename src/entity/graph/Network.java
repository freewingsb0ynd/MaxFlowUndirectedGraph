package entity.graph;

public class Network {
    //private Vertex[] vertices;
    private Vertex s;
    private Vertex t;
    private int maximumFlow;

    public Network(Vertex s, Vertex t) {
        this.s = s;
        this.t = t;
        maximumFlow = 0;
    }

    public Vertex getS() {
        return s;
    }

    public Vertex getT() {
        return t;
    }

    public int getMaximumFlow() {
        return maximumFlow;
    }

    public void setMaximumFlow(int maximumFlow) {
        this.maximumFlow = maximumFlow;
    }

    public static void AddEdge(Vertex u, Vertex v, int capacity){
        u.addAdjaction(v, capacity, v.getCurrentIndex());
        v.addAdjaction(u, capacity, u.getCurrentIndex() - 1);
    }
}
