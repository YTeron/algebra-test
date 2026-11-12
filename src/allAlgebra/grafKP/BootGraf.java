package allAlgebra.grafKP;

import java.util.*;

public class BootGraf {
    public static void boot(){
        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, 5),
                new Edge(1, 3, 3),
                new Edge(3, 4, 2),
                new Edge(3, 5, 3),
                new Edge(3, 6, 2),
                new Edge(6, 7, 3),
                new Edge(6, 5, 6),
                new Edge(5, 8,  4),
                new Edge(8, 9,  4)
        );
        Collections.sort(edges);

        Map<Integer, Integer> initialMap = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            initialMap.put(i, i);
        }
        Krascal dsu = new Krascal(initialMap);

        List<Edge> mst = new ArrayList<>();
        int totalWeight = 0;


        System.out.println("Шаги алгоритма:");
        for (Edge edge : edges) {
            boolean added = dsu.union(edge.u, edge.v);

            if (added) {
                mst.add(edge);
                totalWeight += edge.weight;
                System.out.println("Добавлено ребро: " + edge.u + " - " + edge.v + " (вес " + edge.weight + ")");
            } else {
                System.out.println("ПРОПУЩЕНО ребро: " + edge.u + " - " + edge.v + " (вес " + edge.weight + ") - образует цикл!");
            }
        }

        System.out.println("\nKraskal");
        System.out.println("Общий вес: " + totalWeight);
        System.out.println("Количество ребер: " + mst.size() );
        int n = 10;
        List<List<Prim.Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        addEdge(graph, 1, 2, 5);
        addEdge(graph, 1, 3, 3);
        addEdge(graph, 3, 4, 2);
        addEdge(graph, 3, 5, 3);
        addEdge(graph, 3, 6, 2);
        addEdge(graph, 6, 7, 3);
        addEdge(graph, 6, 5, 6);
        addEdge(graph, 5, 8, 4);
        addEdge(graph, 8, 9, 4);


        Prim.findMST(graph, 1);
    }

    private static void addEdge(List<List<Prim.Edge>> graph, int u, int v, int weight) {
        graph.get(u).add(new Prim.Edge(v, weight));
        graph.get(v).add(new Prim.Edge(u, weight));
    }
}
