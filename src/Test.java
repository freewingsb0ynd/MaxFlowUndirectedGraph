import algorithm.Dinitz;
import algorithm.EdmondKarp;
import algorithm.PreflowPush;
import entity.graph.Network;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int s = scanner.nextInt();
        int t = scanner.nextInt();

        Network network = new Network();
        for(int i = 0; i < n; ++i) network.newVertex();
        network.setSource(network.getVertex(s - 1));
        network.setSink(network.getVertex(t - 1));

        for(int i = 0; i < m; ++i)
        {
            int u = scanner.nextInt(), v = scanner.nextInt(), w = scanner.nextInt();
            Network.addEdge(network.getVertex(u - 1), network.getVertex(v - 1), w);
        }

//        System.out.println(EdmondKarp.getMaximumFlow(network)); network.resetFlow();
//        System.out.println(Dinitz.getMaximumFlow(network)); network.resetFlow();
//        System.out.println(PreflowPush.getMaximumFlow(network)); network.resetFlow();

        System.out.println(PreflowPush.getMaximumFlow(network));
        network.trackFlowPaths();

    }
}
