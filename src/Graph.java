import java.util.*;

public class Graph {
    private Map<Integer, Vertex> vertices;
    private Map<Integer, List<Integer>> adjList;

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
        if (!vertices.containsKey(from) || !vertices.containsKey(to)) {
            throw new IllegalArgumentException("vertex not found");
        }
        adjList.get(from).add(to);
        adjList.get(to).add(from);
    }

    public void printGraph() {
        for (Integer id : adjList.keySet()) {
            System.out.println(id + " -> " + adjList.get(id));
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

            for (Integer neighbor : adjList.get(current)) {
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

        for (Integer neighbor : adjList.get(current)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited);
            }
        }
    }
}