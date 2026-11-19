package belmon;

import java.util.Arrays;

public class BellmanFordVisualConsole {

    static final int INF = 1_000_000_000;
    static final int V = 5; // Вершины: A(0), B(1), C(2), D(3), E(4)
    static final char[] VERTICES = {'A', 'B', 'C', 'D', 'E'};

    public static void main(String[] args) {
        // 1. Создаем граф в коде через матрицу весов [из][в]
        int[][] graphMatrix = {
                // A    B    C    D    E
                {  0,   4,   INF, INF,  2 }, // A -> B(4), E(2)
                { INF,  0,   3,   INF, INF }, // B -> C(3)
                { INF, INF,  0,  -5,   INF }, // C -> D(-5)
                { INF, -2,   INF,  0,   INF }, // D -> B(-2)  => цикл B -> C -> D -> B имеет вес -4
                { INF, INF,  1,   2,    0 }  // E -> C(1), D(2)
        };

        int start = 0; // Стартовая вершина: A
        runBellmanFordVisual(graphMatrix, start);
    }

    public static void runBellmanFordVisual(int[][] matrix, int start) {
        int[] dist = new int[V];
        char[] prev = new char[V];
        Arrays.fill(dist, INF);
        Arrays.fill(prev, '-');
        dist[start] = 0;

        // Шаг 1: Граф через матрицу + графическая схема графа
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║ 1. ГРАФ В КОДЕ (МАТРИЦА ВЕСОВ И ВИЗУАЛЬНАЯ СХЕМА)            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        printMatrix(matrix);
        printGraphAscii(matrix);

        // Шаг 2: Итерации и таблица кратчайших путей
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║ 2. ИТЕРАЦИИ АЛГОРИТМА (СТАРТ: " + VERTICES[start] + ")                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        printTableHeader();
        printTableRow("Старт", dist, prev, "Начальные расстояния");

        for (int iter = 1; iter <= V - 1; iter++) {
            boolean updated = false;
            StringBuilder stepLog = new StringBuilder();

            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    int w = matrix[u][v];
                    if (w != INF && dist[u] != INF && dist[u] + w < dist[v]) {
                        int oldVal = dist[v];
                        dist[v] = dist[u] + w;
                        prev[v] = VERTICES[u];
                        updated = true;

                        if (stepLog.length() > 0) stepLog.append(", ");
                        stepLog.append(VERTICES[u]).append("->").append(VERTICES[v])
                                .append(" (").append(formatDist(oldVal)).append(" => ").append(dist[v]).append(")");
                    }
                }
            }

            String note = updated ? "Обновлено: " + stepLog : "Изменений нет";
            printTableRow("Итер. " + iter, dist, prev, note);

            if (!updated) {
                break;
            }
        }

        // Шаг 3: Анализ таблицы и проверка на цикл отрицательного веса
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║ 3. АНАЛИЗ ТАБЛИЦЫ (ПРОВЕРКА НА ОТРИЦАТЕЛЬНЫЙ ЦИКЛ)           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        boolean hasNegativeCycle = false;
        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                int w = matrix[u][v];
                if (w != INF && dist[u] != INF && dist[u] + w < dist[v]) {
                    hasNegativeCycle = true;
                    System.out.printf("  [!] Ребро %c -> %c (вес %+d): dist[%c](%d) + %d = %d < dist[%c](%d)%n",
                            VERTICES[u], VERTICES[v], w,
                            VERTICES[u], dist[u], w, dist[u] + w,
                            VERTICES[v], dist[v]);
                }
            }
        }

        if (!hasNegativeCycle) {
            System.out.println("  [OK] После (V - 1) итераций ни одно ребро не позволяет уменьшить dist[v].");
        }

        // Шаг 4: Итоговый вывод
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║ 4. ВЫВОД                                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        if (hasNegativeCycle) {
            System.out.println("  РЕЗУЛЬТАТ: Обнаружен цикл отрицательного веса (зацикленный отрицательный путь).");
            System.out.println("  Кратчайшие пути для вершин в цикле и достижимых из него не определены");
            System.out.println("  (при каждом обходе цикла вес пути уменьшается).");
        } else {
            System.out.println("  РЕЗУЛЬТАТ: Циклов отрицательного веса в достижимой части графа нет.");
            System.out.println("  Таблица содержит корректные кратчайшие расстояния от вершины " + VERTICES[start] + ".");
        }
    }

    private static void printMatrix(int[][] matrix) {
        System.out.println("\nМатрица весов (строка -> столбец, INF = нет ребра):");
        System.out.print("      ");
        for (char c : VERTICES) {
            System.out.printf("%6c", c);
        }
        System.out.println("\n      " + "------".repeat(V));

        for (int i = 0; i < V; i++) {
            System.out.printf("  %c │ ", VERTICES[i]);
            for (int j = 0; j < V; j++) {
                if (matrix[i][j] == INF) {
                    System.out.printf("%6s", "INF");
                } else {
                    System.out.printf("%6d", matrix[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static void printGraphAscii(int[][] matrix) {
        System.out.println("\nСхема графа и активные ребра:");
        System.out.println("        (+4)              (+3)");
        System.out.println("    (A) ──────> (B) ───────────> (C)");
        System.out.println("     │           ^                │");
        System.out.println(" (+2)│           │ (-2)           │ (-5)");
        System.out.println("     v           │                v");
        System.out.println("    (E) ──(+1)─┼──> (C)   (D) <──┘");
        System.out.println("     │           └────────────────┘");
        System.out.println("     └─────────────(+2)──────────> (D)");
        System.out.println("\nСписок всех рёбер из матрицы:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i != j && matrix[i][j] != INF) {
                    System.out.printf("  %c ──(%+d)──> %c%n", VERTICES[i], matrix[i][j], VERTICES[j]);
                }
            }
        }
    }

    private static void printTableHeader() {
        System.out.printf("%-10s", "Шаг");
        for (char c : VERTICES) {
            System.out.printf("%7c", c);
        }
        System.out.println("   │ Предки (A..E) │ Что произошло");
        System.out.println("─".repeat(10 + 7 * V + 3 + 15 + 3 + 32));
    }

    private static void printTableRow(String label, int[] dist, char[] prev, String note) {
        System.out.printf("%-10s", label);
        for (int d : dist) {
            System.out.printf("%7s", formatDist(d));
        }
        StringBuilder prevStr = new StringBuilder();
        for (char p : prev) {
            prevStr.append(p).append(" ");
        }
        System.out.printf("   │ %-13s │ %s%n", prevStr.toString().trim(), note);
    }

    private static String formatDist(int d) {
        return d >= INF / 2 ? "INF" : String.valueOf(d);
    }
}