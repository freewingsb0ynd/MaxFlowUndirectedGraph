package algorithm;

import entity.graph.Edge;
import entity.graph.Network;
import entity.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Dinitz {
    private static Queue<Vertex> queue;
    private static ArrayList<Integer> levels;
    private static ArrayList<Integer> residualFlow;

    private static boolean findPath(Network network)
    {
        levels = new ArrayList<>();
        for(int i = 0; i < network.numberOfVertices(); ++i) levels.add(0);

        queue = new LinkedList<>();

        Vertex source = network.getS();
        Vertex sink = network.getT();

        levels.set(network.numberOfVertices()-1, 1);
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

    private static int additionFlow(Vertex u, int residualFlow, Vertex sink)
    {
        if (u == sink) {
            //System.out.println("-->" + residualFlow);
            return residualFlow;
        }
        else
        {
            int delta = 0;
            ArrayList<Edge> adjancents = u.getAdjacents();
            for(Edge edge : adjancents)
            {
                Vertex v = edge.getV();
                if (levels.get(v.getId()) == levels.get(u.getId()) - 1 && edge.getResidualFlow() > 0)
                {
                    int blockingFlow = additionFlow(v, Math.min(residualFlow, edge.getResidualFlow()), sink);
                    delta += blockingFlow;
                    edge.incFlow(blockingFlow);
                    edge.reverseEdge().incFlow(-blockingFlow);
                    residualFlow -= blockingFlow;
                    //System.out.println((u.getId()+1) + " " + (v.getId()+1) + " " + blockingFlow);
                    if (residualFlow == 0) return delta;
                }
            }
            return delta;
        }
    }

    public static int maximumFlow(Network network)
    {
        int flow = 0;

        Vertex source = network.getS();
        Vertex sink = network.getT();

        while (findPath(network)) {
            flow += additionFlow(source, Integer.MAX_VALUE, sink);
            //System.out.println(flow);
        }

        return flow;
    }

}
