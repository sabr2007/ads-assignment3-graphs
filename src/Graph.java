import java.util.*;

public class Graph {
    private Map<Integer, Vertex> vertices;
    private Map<Integer, List<Edge>> adjList;

    public Graph() {
        this.vertices = new HashMap<>();
        this.adjList = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        int id = v.getId();
        if (vertices.containsKey(id)) {
            return;
        }
        vertices.put(id, v);
        adjList.put(id, new ArrayList<>());
    }

    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    public void addEdge(int from, int to, int weight) {
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            throw new IllegalArgumentException("vertex not found");
        }
        Vertex fromV = vertices.get(from);
        Vertex toV = vertices.get(to);
        adjList.get(from).add(new Edge(fromV, toV, weight));
        adjList.get(to).add(new Edge(toV, fromV, weight));
    }

    public void printGraph() {
        for (Integer id : adjList.keySet()) {
            List<Integer> neighbors = new ArrayList<>();
            for (Edge e : adjList.get(id)) {
                neighbors.add(e.getDestination().getId());
            }
            System.out.println(id + " -> " + neighbors);
        }
    }

    public void printWeightedGraph() {
        for (Integer id : adjList.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(" -> [");
            List<Edge> edges = adjList.get(id);
            for (int i = 0; i < edges.size(); i++) {
                Edge e = edges.get(i);
                sb.append(e.getDestination().getId()).append("(w=").append(e.getWeight()).append(")");
                if (i < edges.size() - 1) sb.append(", ");
            }
            sb.append("]");
            System.out.println(sb);
        }
    }


    public void bfs(int start) {
        if (!vertices.containsKey(start)) {
            System.out.println("vertex not found");
            return;
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();

        visited.add(start);
        queue.add(start);

        System.out.print("BFS order: ");

        while (!queue.isEmpty()) {
            int current = queue.poll();
            System.out.print(current + " ");

            for (Edge e : adjList.get(current)) {
                int neighbor = e.getDestination().getId();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }

    public void dfs(int start) {
        if (!vertices.containsKey(start)) {
            System.out.println("vertex not found");
            return;
        }

        Set<Integer> visited = new HashSet<>();
        System.out.print("DFS order: ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(int current, Set<Integer> visited) {
        visited.add(current);
        System.out.print(current + " ");

        for (Edge e : adjList.get(current)) {
            int neighbor = e.getDestination().getId();
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited);
            }
        }
    }
}
