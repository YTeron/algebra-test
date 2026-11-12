package allAlgebra.grafKP;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class KruskalVisualization extends JPanel {

    static class Edge implements Comparable<Edge> {
        int u, v, weight;
        public Edge(int u, int v, int weight) { this.u = u; this.v = v; this.weight = weight; }
        @Override
        public int compareTo(Edge other) { return Integer.compare(this.weight, other.weight); }
    }

    static int n = 9;
    static java.util.List<Edge> edges = new ArrayList<>();

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
        edges.add(new Edge(1, 2, 5));
        edges.add(new Edge(1, 3, 3));
        edges.add(new Edge(3, 4, 2));
        edges.add(new Edge(3, 5, 3));
        edges.add(new Edge(3, 6, 2));
        edges.add(new Edge(6, 7, 3));
        edges.add(new Edge(6, 5, 6));
        edges.add(new Edge(5, 8, 4));
        edges.add(new Edge(8, 9, 4));

        runKruskal();

        JFrame frame = new JFrame("Визуализация алгоритма Краскала");
        KruskalVisualization panel = new KruskalVisualization();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void runKruskal() {
        Collections.sort(edges);

        Map<Integer, Integer> initialMap = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            initialMap.put(i, i);
        }
        Krascal dsu = new Krascal(initialMap);

        for (Edge edge : edges) {
            boolean added = dsu.union(edge.u, edge.v);
            String key = Math.min(edge.u, edge.v) + "-" + Math.max(edge.u, edge.v);

            if (added) {
                mstEdges.add(key);
                mstVertices.add(edge.u);
                mstVertices.add(edge.v);
            } else {
                skippedEdges.add(key);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        for (Edge e : edges) {
            drawEdge(g2, e.u, e.v, e.weight, Color.LIGHT_GRAY);
        }

        g2.setStroke(new BasicStroke(4));
        for (String edgeKey : skippedEdges) {
            String[] parts = edgeKey.split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = getWeight(u, v);
            drawEdge(g2, u, v, weight, Color.RED);
        }

        g2.setStroke(new BasicStroke(4));
        for (String edgeKey : mstEdges) {
            String[] parts = edgeKey.split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int weight = getWeight(u, v);
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
        g2.drawString("Красная линия - Отброшенное ребро (цикл, вес 6)", 20, 40);
    }

    private static int getWeight(int u, int v) {
        for (Edge e : edges) {
            if ((e.u == u && e.v == v) || (e.u == v && e.v == u)) return e.weight;
        }
        return 0;
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