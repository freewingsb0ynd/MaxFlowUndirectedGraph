package algorithm;

import entity.graph.Edge;
import entity.graph.Network;
import entity.graph.Vertex;
import util.Helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PreflowPush {
    private static Network network;
    private static int n;
    private static Vertex source, sink;

    private static ArrayList<Integer> currentIndexes;
    private static ArrayList<Integer> preflows;
    private static ArrayList<Integer> heights;
    private static ArrayList<Integer> distributions;

    private static Queue<Vertex> queue = new LinkedList<>();
    private static ArrayList<Boolean> inQueue;

    private static void pushToQueue(Vertex vertex)
    {
        if (inQueue.get(vertex.getId())) return;
        queue.add(vertex);
        inQueue.set(vertex.getId(), true);
    }

    private static Vertex popFromQueue()
    {
        Vertex vertex = queue.poll();
        assert vertex != null;
        inQueue.set(vertex.getId(), false);
        return vertex;
    }

    private static void pushFlowOnEdge(Edge edge)
    {
        int delta = Math.min(preflows.get(edge.getU().getId()), edge.getResidualFlow());

        edge.incFlow(delta);
        edge.getReversedEdge().incFlow(-delta);

        Helper.increaseAtIndex(preflows, edge.getU().getId(), -delta);
        Helper.increaseAtIndex(preflows, edge.getV().getId(), delta);
    }

    private static void setHeight(Vertex vertex, int height)
    {
        Helper.increaseAtIndex(distributions, heights.get(vertex.getId()), -1);
        heights.set(vertex.getId(), height);
        Helper.increaseAtIndex(distributions, height, 1);
    }

    private static void performGapHeuristic(int gap)
    {
        if (0 < gap && gap < n && distributions.get(gap) == 0)
            for(Vertex vertex : network.getVertices())
                if (vertex != source && gap < heights.get(vertex.getId()) && heights.get(vertex.getId()) <= n)
                {
                    setHeight(vertex, n + 1);
                    currentIndexes.set(vertex.getId(), 0);
                }
    }

    private static void lift(Vertex vertex)
    {
        int minHeight = Integer.MAX_VALUE;
        for(Edge edge : vertex.getAdjacencies())
            if (edge.getResidualFlow() > 0) minHeight = Math.min(minHeight, heights.get(edge.getV().getId()));

        int gap = heights.get(vertex.getId());
        setHeight(vertex, minHeight + 1);
        performGapHeuristic(gap);
    }

    private static void init(Network inputNetwork) {
        network = inputNetwork;
        n = network.numberOfVertices();
        source = network.getSource();
        sink = network.getSink();

        currentIndexes = new ArrayList<>();
        preflows = new ArrayList<>();
        heights = new ArrayList<>();
        distributions = new ArrayList<>();
        inQueue = new ArrayList<>();
        for(int i = 0; i < n; ++i)
        {
            currentIndexes.add(0);
            preflows.add(0);
            heights.add(1);
            distributions.add(0);
            inQueue.add(false);
        }

        heights.set(source.getId(), n);
        heights.set(sink.getId(), 0);

        distributions.set(0, 1);
        distributions.set(1, n - 2);
        distributions.add(1);

        for(int i = 0; i < n; ++i)
            distributions.add(0);

        for(Edge edge : source.getAdjacencies())
        {
            int delta = edge.getCapacity();

            edge.setFlow(delta);
            edge.getReversedEdge().setFlow(-delta);

            Vertex vertex = edge.getV();

            Helper.increaseAtIndex(preflows, source.getId(), -delta);
            Helper.increaseAtIndex(preflows, vertex.getId(), delta);

            if (vertex != source && vertex != sink && preflows.get(vertex.getId()) > 0) pushToQueue(vertex);
        }
    }

    private static void preflowPush()
    {
        while (!queue.isEmpty())
        {
            Vertex u = popFromQueue();
            ArrayList<Edge> adjacencies = u.getAdjacencies();
            for(int i = currentIndexes.get(u.getId()); i < adjacencies.size(); ++i)
            {
                Edge edge = adjacencies.get(i);
                Vertex v = edge.getV();
                int delta = edge.getResidualFlow();
                if (delta > 0 && heights.get(u.getId()) > heights.get(v.getId()))
                {
                    pushFlowOnEdge(edge);
                    if (v != source && v != sink && preflows.get(v.getId()) > 0) pushToQueue(v);
                    if (preflows.get(u.getId()) == 0)
                    {
                        currentIndexes.set(u.getId(), i);
                        break;
                    }
                }
            }
            if (preflows.get(u.getId()) > 0)
            {
                lift(u);
                currentIndexes.set(u.getId(), 0);
                pushToQueue(u);
            }
        }
    }

    public static int getMaximumFlow(Network network)
    {
        init(network);

        preflowPush();

        return preflows.get(sink.getId());
    }

}
