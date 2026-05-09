import java.util.*;

public class Experiment {
    private long[] bfsTimes = new long[3];
    private long[] dfsTimes = new long[3];
    private int[] sizes = {10, 30, 100};

    public Graph generateGraph(int numVertices, int numEdges) {
        Graph g = new Graph();

        for (int i = 0; i < numVertices; i++) {
            g.addVertex(new Vertex(i));
        }


        for (int i = 0; i < numVertices - 1; i++) {
            g.addEdge(i, i + 1);
        }


        Random rand = new Random(42);
        int edgesAdded = numVertices - 1;
        while (edgesAdded < numEdges) {
            int from = rand.nextInt(numVertices);
            int to = rand.nextInt(numVertices);
            if (from != to) {
                g.addEdge(from, to);
                edgesAdded++;
            }
        }

        return g;
    }

    public void runTraversals(Graph g, int sizeIndex) {
        int iterations = 1000;


        long bfsStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            g.bfs(0);
        }
        long bfsEnd = System.nanoTime();
        bfsTimes[sizeIndex] = (bfsEnd - bfsStart) / iterations;


        long dfsStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            g.dfs(0);
        }
        long dfsEnd = System.nanoTime();
        dfsTimes[sizeIndex] = (dfsEnd - dfsStart) / iterations;
    }

    public void runMultipleTests() {
        for (int i = 0; i < sizes.length; i++) {
            int v = sizes[i];
            Graph g = generateGraph(v, 2 * v); // E ≈ 2V
            runTraversals(g, i);
        }
    }

    public void printResults() {
        System.out.println("\n=== Performance Results ===");
        System.out.printf("%-10s %-15s %-15s%n", "Vertices", "BFS (ns)", "DFS (ns)");
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("%-10d %-15d %-15d%n", sizes[i], bfsTimes[i], dfsTimes[i]);
        }
    }
}