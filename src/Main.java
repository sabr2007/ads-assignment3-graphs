public class Main {
    public static void main(String[] args) {

        System.out.println("manual demo graph:");
        Graph demo = new Graph();

        for (int i = 0; i < 6; i++) {
            demo.addVertex(new Vertex(i));
        }

        demo.addEdge(0, 1);
        demo.addEdge(0, 2);
        demo.addEdge(1, 3);
        demo.addEdge(2, 4);
        demo.addEdge(3, 5);

        System.out.println("graph structure:");
        demo.printGraph();

        demo.bfs(0);
        demo.dfs(0);

        System.out.println("\nweighted demo graph:");
        Graph weighted = new Graph();
        for (int i = 0; i < 6; i++) {
            weighted.addVertex(new Vertex(i));
        }

        weighted.addEdge(0, 1, 4);
        weighted.addEdge(0, 2, 1);
        weighted.addEdge(2, 1, 2);
        weighted.addEdge(1, 3, 1);
        weighted.addEdge(2, 3, 5);
        weighted.addEdge(3, 4, 3);
        weighted.addEdge(4, 5, 2);

        weighted.printWeightedGraph();
        weighted.dijkstra(0);

        Experiment exp = new Experiment();
        exp.runMultipleTests();
        exp.printResults();
    }
}