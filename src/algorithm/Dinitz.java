package algorithm;

import entity.graph.Edge;
import entity.graph.Network;
import entity.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Dinitz {
    private static ArrayList<Integer> levels;

    private static int time;
    private static ArrayList<Integer> passed;

    private static boolean findPath(Network network)
    {
        levels = new ArrayList<>();
        for(int i = 0; i < network.numberOfVertices(); ++i) levels.add(0);

        Queue<Vertex> queue = new LinkedList<>();

        Vertex source = network.getSource();
        Vertex sink = network.getSink();

        levels.set(sink.getId(), 1);
        queue.add(sink);

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();
            ArrayList<Edge> adjacencies = u.getAdjacencies();
            for (Edge edge : adjacencies) {
                Vertex v = edge.getV();
                if (levels.get(v.getId()) == 0 && edge.getReversedEdge().getResidualFlow() > 0) {
                    levels.set(v.getId(), levels.get(u.getId()) + 1);
                    if (v == source) return true;
                    queue.add(v);
                }
            }
        }
        return false;
    }

    private static int residualFlow(Vertex u, int limit, Vertex sink)
    {
        if (u == sink) return limit;

        int delta = 0;
        ArrayList<Edge> adjacencies = u.getAdjacencies();
        for(Edge edge : adjacencies)
        {
            Vertex v = edge.getV();
            if (passed.get(v.getId()) != time && levels.get(v.getId()) == levels.get(u.getId()) - 1 && edge.getResidualFlow() > 0)
            {
                int blockingFlow = residualFlow(v, Math.min(limit, edge.getResidualFlow()), sink);
                delta += blockingFlow;
                edge.incFlow(blockingFlow);
                edge.getReversedEdge().incFlow(-blockingFlow);
                limit -= blockingFlow;
                if (limit == 0) return delta;
            }
        }

        passed.set(u.getId(), time);

        return delta;
    }

    public static int getMaximumFlow(Network network)
    {
        int maximumFlow = 0;

        Vertex source = network.getSource();
        Vertex sink = network.getSink();

        time = 0;

        passed = new ArrayList<>();
        for(int i = 0; i < network.numberOfVertices(); ++i)
            passed.add(0);

        while (findPath(network)) {
            time++;
            maximumFlow += residualFlow(source, Integer.MAX_VALUE, sink);
        }

        return maximumFlow;
    }

}
