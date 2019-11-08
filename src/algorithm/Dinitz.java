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
            ArrayList<Edge> adjacents = u.getAdjacents();
            for (Edge edge : adjacents) {
                Vertex v = edge.getV();
                if (levels.get(v.getId()) == 0 && edge.reverseEdge().getResidualFlow() > 0) {
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
        if (u == sink) {
            return limit;
        }

        passed.set(u.getId(), time);

        int delta = 0;
        ArrayList<Edge> adjancents = u.getAdjacents();
        for(Edge edge : adjancents)
        {
            Vertex v = edge.getV();
            if (passed.get(v.getId()) != time && levels.get(v.getId()) == levels.get(u.getId()) - 1 && edge.getResidualFlow() > 0)
            {
                int blockingFlow = residualFlow(v, Math.min(limit, edge.getResidualFlow()), sink);
                delta += blockingFlow;
                edge.incFlow(blockingFlow);
                edge.reverseEdge().incFlow(-blockingFlow);
                limit -= blockingFlow;
                if (limit == 0) return delta;
            }
        }
        return delta;
    }

    public static int maximumFlow(Network network)
    {
        int flow = 0;

        Vertex source = network.getSource();
        Vertex sink = network.getSink();

        time = 0;

        passed = new ArrayList<>();
        for(int i = 0; i < network.numberOfVertices(); ++i)
            passed.add(0);

        while (findPath(network)) {
            time++;
            flow += residualFlow(source, Integer.MAX_VALUE, sink);
        }

        return flow;
    }

}
