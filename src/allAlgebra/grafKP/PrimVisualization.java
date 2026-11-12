package allAlgebra.grafKP;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;


public class PrimVisualization extends JPanel {

    static class Edge implements Comparable<Edge> {
        int to;
        int weight;
        public Edge(int to, int weight) { this.to = to; this.weight = weight; }
        @Override
        public int compareTo(Edge other) { return Integer.compare(this.weight, other.weight); }
    }

    static java.util.List<java.util.List<Edge>> graph = new ArrayList<>();
    static int n = 9;

    static int[][] coords = {
            {0, 0},
            {100, 100},
            {50, 200},
            {250, 150},
            {400, 150},
            {200, 300},
            {350, 250},
            {500, 250},
            {250, 400},
            {250, 500}
    };


    static Set<String> mstEdges = new HashSet<>();
    static Set<String> skippedEdges = new HashSet<>();
    static Set<Integer> mstVertices = new HashSet<>();

    public static void main(String[] args) {

        for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
        addEdge(1, 2, 5); addEdge(1, 3, 3); addEdge(3, 4, 2);
        addEdge(3, 5, 3); addEdge(3, 6, 2); addEdge(6, 7, 3);
        addEdge(6, 5, 6); addEdge(5, 8, 4); addEdge(8, 9, 4);

        runPrim(3);

        JFrame frame = new JFrame("Визуализация алгоритма Прима (Старт: 3)");
        PrimVisualization panel = new PrimVisualization();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void addEdge(int u, int v, int weight) {
        graph.get(u).add(new Edge(v, weight));
        graph.get(v).add(new Edge(u, weight));
    }

    private static void runPrim(int startVertex) {
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Map<Integer, Integer> parentMap = new HashMap<>();

        pq.add(new Edge(startVertex, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;

            if (visited[u]) {
                if (current.weight != 0) {
                    int parent = parentMap.get(u);
                    skippedEdges.add(Math.min(parent, u) + "-" + Math.max(parent, u));
                }
                continue;
            }

            visited[u] = true;
            mstVertices.add(u);

            if (current.weight != 0) {
                int parent = parentMap.get(u);
                mstEdges.add(Math.min(parent, u) + "-" + Math.max(parent, u));
            }

            for (Edge edge : graph.get(u)) {
                if (!visited[edge.to]) {
                    parentMap.put(edge.to, u);
                    pq.add(edge);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        for (int i = 1; i <= n; i++) {
            for (Edge e : graph.get(i)) {
                if (i < e.to) {
                    drawEdge(g2, i, e.to, e.weight, Color.LIGHT_GRAY);
                }
            }
        }

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(4));
        for (String edgeKey : skippedEdges) {
            String[] parts = edgeKey.split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = 0;
            for (Edge e : graph.get(u)) if (e.to == v) weight = e.weight;
            drawEdge(g2, u, v, weight, Color.RED);
        }

        g2.setStroke(new BasicStroke(4));
        for (String edgeKey : mstEdges) {
            String[] parts = edgeKey.split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = 0;
            for (Edge e : graph.get(u)) if (e.to == v) weight = e.weight;
            drawEdge(g2, u, v, weight, new Color(0, 150, 0));
        }

        for (int i = 1; i <= n; i++) {
            int x = coords[i][0];
            int y = coords[i][1];

            if (mstVertices.contains(i)) {
                g2.setColor(new Color(0, 150, 0));
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.fillOval(x - 15, y - 15, 30, 30);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - 15, y - 15, 30, 30);

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(String.valueOf(i), x - 5, y + 5);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Зеленые линии - Минимальное остовное дерево (вес 26)", 20, 20);
        g2.setColor(Color.RED);
        g2.drawString("серая линия - Отброшенное ребро (цикл, вес 6)", 20, 40);
    }

    private void drawEdge(Graphics2D g2, int u, int v, int weight, Color color) {
        g2.setColor(color);
        int x1 = coords[u][0], y1 = coords[u][1];
        int x2 = coords[v][0], y2 = coords[v][1];

        g2.drawLine(x1, y1, x2, y2);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRect(midX - 10, midY - 15, 20, 20);

        g2.setColor(Color.BLACK);
        g2.drawString(String.valueOf(weight), midX - 5, midY);
    }
}