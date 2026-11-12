package allAlgebra.grafKP;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    static class Edge implements Comparable<Edge> {
        int to;
        int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    public static void findMST(List<List<Edge>> graph, int startVertex) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        int totalWeight = 0;
        List<String> mstEdges = new ArrayList<>();

        pq.add(new Edge(startVertex, 0));

        System.out.println("Шаги алгоритма Прима:");

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;

            if (visited[u]) {
                continue;
            }

            visited[u] = true;
            totalWeight += current.weight;

            if (current.weight != 0) {
                System.out.println("Добавлена вершина " + u + " (вес ребра: " + current.weight + ")");
                mstEdges.add("Вершина " + u + " (вес " + current.weight + ")");
            }

            for (Edge edge : graph.get(u)) {
                if (!visited[edge.to]) {
                    pq.add(edge);
                }
            }
        }

        System.out.println("\nPrim");
        System.out.println("Общий вес: " + totalWeight);
        System.out.println("Количество добавленных вершин: " + mstEdges.size());
    }
}
