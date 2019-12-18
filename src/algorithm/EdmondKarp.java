package algorithm;

import entity.graph.Edge;
import entity.graph.Network;
import entity.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class EdmondKarp {
    private static ArrayList<Edge> traces;

    private static boolean findPath(Network network)
    {
        traces = new ArrayList<>();
        for(int i = 0; i < network.numberOfVertices(); ++i)
            traces.add(null);

        Queue<Vertex> queue = new LinkedList<>();

        Vertex source = network.getSource();
        Vertex sink = network.getSink();

        queue.add(source);

        while (!queue.isEmpty())
        {
            Vertex u = queue.poll();
            ArrayList<Edge> adjacencies = u.getAdjacencies();
            for(Edge edge: adjacencies)
            {
                Vertex v = edge.getV();
                if (traces.get(v.getId()) == null && edge.getResidualFlow() > 0)
                {
                    traces.set(v.getId(), edge);
                    if (v == sink)
                        return true;
                    queue.add(v);
                }
            }
        }
        return false;
    }

    private static int residualFlow(Network network)
    {
        int delta = Integer.MAX_VALUE;

        Vertex source = network.getSource();
        Vertex sink = network.getSink();

        Vertex current = sink;
        do {
            Edge edge = traces.get(current.getId());
            delta = Math.min(delta, edge.getResidualFlow());
            current = edge.getU();
        }
        while (current != source);

        current = sink;
        do {
            Edge edge = traces.get(current.getId());
            edge.incFlow(delta);
            edge.getReversedEdge().incFlow(-delta);
            current = edge.getU();
        }
        while (current != source);

        return delta;
    }

    public static int getMaximumFlow(Network network)
    {
        int maximumFlow = 0;

        while (findPath(network))
            maximumFlow += residualFlow(network);

        return maximumFlow;
    }

}
