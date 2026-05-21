# Assignment 4 Graph Traversal and Representation System

**Author:** Sabyrzhan Valiullov
**Group:** BDA-2504
**University:** Astana IT University
**Repo:** ads-assignment3-graphs

---

## Project Overview

Java project that builds an undirected unweighted graph using adjacency list representation and runs two traversal algorithms on it. Times measured with System.nanoTime() across V=10, 30, 100.

**Algorithms picked:** Breadth-First Search (BFS), Depth-First Search (DFS).
**Graph type:** undirected, unweighted.
**Representation:** adjacency list (HashMap of vertex id to list of neighbor ids).

---

## Class Descriptions

### Vertex

Wraps a single integer id. Constructor, getter, toString() that returns "V(id)" so the console output stays readable.

### Edge

Holds source and destination as Vertex objects (not raw ints). Constructor, getters, toString(). 

### Graph

Core class. Two maps inside:
- vertices maps id to the Vertex object
- adjList maps id to a list of neighbor ids

Since the graph is undirected, addEdge(from, to) adds the connection in both directions puts to into adjList[from] AND from into adjList[to]. That symmetry is what makes it undirected.


### Experiment

Generates graphs of given size, runs traversals 1000 times, averages the time. Generation strategy:
1. Add V vertices with ids 0 to V-1
2. Connect them in a chain 0-1-2-...-(V-1) to guarantee connectivity
3. Add random extra edges until total edges = 2*V

Random uses a fixed seed (7) so results are reproducible.

---

## Algorithm Descriptions

### BFS (Breadth-First Search)

Visits vertices level by level. Starts at the source, hits all its direct neighbors, then their neighbors, and so on. Like ripples from a stone in water.

**How it works:**
1. Mark start as visited, push it into a queue
2. While queue is not empty: pop the front vertex, look at its neighbors
3. For each unvisited neighbor: mark visited, push to back of queue
4. Stop when queue is empty

Uses ArrayDeque as the queue, faster than LinkedList because it's array-backed and cache-friendly.

**Use cases:** shortest path in unweighted graphs, level-order traversal, finding connected components.
**Time:** O(V + E). Each vertex enters the queue once, each edge is examined once.
**Space:** O(V) for the visited set and the queue.

### DFS (Depth-First Search)

Goes as deep as possible down one branch before backtracking. Implemented recursively the JVM call stack is the stack data structure, just implicit.

**How it works:**
1. Mark current vertex as visited
2. For each unvisited neighbor: recursively call DFS on it
3. When all neighbors are done, return back up

**Use cases:** cycle detection, topological sort, finding any path between two vertices, connected components.
**Time:** O(V + E). Same as BFS each vertex visited once, each edge examined once.
**Space:** O(V) for visited set, plus recursion stack up to O(V) in the worst case.

---

## Experimental Results

All times in nanoseconds, averaged over 1000 iterations per measurement. Edge count = 2*V for all tests.

| Vertices | Edges | BFS (ns) | DFS (ns) |
|----------|-------|----------|----------|
| 10       | 20    | 8 239    | 6 522    |
| 30       | 60    | 18 405   | 18 572   |
| 100      | 200   | 74 444   | 87 110   |

### Demo graph output (V=6, manual edges)

graph structure:
0 -> [1, 2]
1 -> [0, 3]
2 -> [0, 4]
3 -> [1, 5]
4 -> [2]
5 -> [3]
BFS order: 0 1 2 3 4 5
DFS order: 0 1 3 5 2 4

You can clearly see the difference. BFS goes 0, then everyone at distance 1 (1, 2), then distance 2 (3, 4), then distance 3 (5). DFS goes deep first 0 to 1 to 3 to 5 to dead end, backtrack all the way up, then 2 to 4.

---

## Analysis Questions

**How does graph size affect BFS and DFS performance?**
Both grow roughly linearly with V. From V=10 to V=100 (10× increase), BFS time grew ~9.0× and DFS grew ~13.3×. That's close to linear, which matches O(V+E) since E was kept proportional to V (E = 2V), the total V+E also grew ~10×.

**Which traversal is faster in your experiments?**
Mixed and that's the interesting part. DFS is faster on V=10 (6522 vs 8239 ns), basically tied on V=30 (18405 vs 18572 ns), and slower on V=100 (87110 vs 74444 ns). The crossover happens somewhere between V=30 and V=100. On small graphs recursion is cheaper than the queue + visited-set bookkeeping that BFS does. On bigger graphs the recursion stack frames start adding up and BFS's flat iterative loop pulls ahead.

**Do results match the expected complexity O(V + E)?**
Yes for scaling. Both algorithms grow roughly linearly with V+E across the three sizes, just like theory predicts. What Big-O does NOT predict is the constant factor that's where the BFS/DFS crossover lives. Big-O says they're identical, real measurements say "depends on V".

**How does graph structure affect traversal order?**
Heavy. With my generator (chain backbone + random extra edges, seed 7), BFS visits roughly in chain order (0, 1, 2, 3...) because the chain dominates and BFS explores level by level. DFS dives down the first random edge it sees, so the order is much less predictable. On the demo graph BFS gave 0 1 2 3 4 5, DFS gave 0 1 3 5 2 4 same graph, completely different orders.

**When is BFS preferred over DFS?**
When you need the shortest path in an unweighted graph BFS guarantees it because it explores by distance. Also when the graph is wide and shallow (lots of branches but not very deep), where DFS recursion would be wasteful. And when the graph is huge and deep recursion could blow the stack BFS keeps everything on the heap.

**What are the limitations of DFS?**
Three big ones. First, no shortest-path guarantee in unweighted graphs DFS finds a path, not the shortest one. Second, recursion depth on a graph that's basically a long chain (like V=10000 connected as 0-1-2-...-9999), the recursion would hit StackOverflowError. Iterative DFS with an explicit Deque fixes this but loses the clean recursive code. Third, DFS order is implementation-dependent which neighbor you visit first depends on how adjList stores them, so the same graph can produce different DFS orders.

---

## Reflection

The experiment looked simple on paper "run BFS and DFS, time them" but most of the time went into making the timer actually measure the algorithm. First runs had System.out.print inside the traversal methods, called 1000 times per measurement. Numbers were 5× higher than they should have been and showed weird non-monotonic patterns (V=30 sometimes slower than V=100, DFS suddenly losing to BFS by 2×). Disabling System.out via setOut during the timing loop fixed that immediately.

Then JIT warmup. The first sized tested (V=10) was always slower than V=30, which makes no sense for O(V+E) until you remember the JVM runs interpreted bytecode for the first ~hundred invocations before the JIT compiler kicks in. Adding 200 warmup iterations before each timing run made the numbers monotonically increase with V like theory expects. Same JIT trick I had to figure out in Assignment 2 with Binary Search.

The actual algorithmic insight is that Big-O is the same for BFS and DFS but real performance crosses over depending on V. On small graphs DFS wins because recursion is cheaper than queue management. On bigger graphs BFS wins because stack frames cost more than ArrayDeque pops. None of that is in the textbook complexity it only shows up when you actually run the code and look honestly at what the numbers say.

---

## Bonus Task: Dijkstra's Algorithm

Extension of the same project that adds edge weights and computes single-source shortest paths.

### What changed in the existing classes

**Edge**
Added a weight field plus a constructor `Edge(Vertex source, Vertex destination, int weight)`. The old `Edge(source, destination)` constructor is kept and forwards to the new one with weight=1, so nothing in the rest of the code breaks.

**Graph**
Adjacency list type went from `Map<Integer, List<Integer>>` (neighbor ids) to `Map<Integer, List<Edge>>` (full edge objects). `addEdge(from, to)` still works as before and stores edges with weight=1. New overload `addEdge(from, to, weight)` stores weighted edges. Since the graph is undirected, both directions get an Edge with the same weight.

BFS and DFS still work without changes to their logic they just iterate `Edge` objects now and pull the destination id from each edge. Tested on the original demo graph and got the same BFS/DFS order as before (0 1 2 3 4 5 / 0 1 3 5 2 4) so the refactor did not break anything.

### Dijkstra implementation

Method signature: `void dijkstra(int start)`. No priority queue, just two arrays and simple loops as the assignment suggested.

How it works:
1. dist[] of size n, filled with Integer.MAX_VALUE, then dist[start] = 0
2. visited[] of size n, all false
3. Repeat n times:
   - Scan the dist array to find the unvisited vertex u with the smallest current dist
   - If no such vertex exists (everything reachable is already visited), break
   - Mark u visited
   - For every edge out of u, relax: if dist[u] + weight < dist[v] then dist[v] = dist[u] + weight
4. Print the distance array

The inner "find the min" loop is the part a priority queue would normally speed up. Without it the complexity is O(V²) instead of O((V+E) log V), which is fine for the small graphs here and matches what the task description asks for.

One assumption: vertex ids are 0..n-1 so they can be used as array indices. The whole project already uses sequential ids (Main builds 0..5, Experiment builds 0..V-1), so this fits the existing convention.

### Weighted demo graph

Same six vertices as before, but now with edge weights:

| from | to | weight |
|------|----|--------|
| 0    | 1  | 4      |
| 0    | 2  | 1      |
| 2    | 1  | 2      |
| 1    | 3  | 1      |
| 2    | 3  | 5      |
| 3    | 4  | 3      |
| 4    | 5  | 2      |

Console output for `dijkstra(0)`:

weighted demo graph:
0 -> [1(w=4), 2(w=1)]
1 -> [0(w=4), 2(w=2), 3(w=1)]
2 -> [0(w=1), 1(w=2), 3(w=5)]
3 -> [1(w=1), 2(w=5), 4(w=3)]
4 -> [3(w=3), 5(w=2)]
5 -> [4(w=2)]
Dijkstra from 0:
  to 0: 0
  to 1: 3
  to 2: 1
  to 3: 4
  to 4: 7
  to 5: 9

Quick sanity check on vertex 1. Direct edge 0->1 has weight 4. But going 0->2->1 costs 1+2=3, which is shorter. Dijkstra finds that 3, not the direct 4. For vertex 5 the only way in is through 4, and the cheapest path to 4 is 0->2->1->3->4 = 1+2+1+3 = 7, plus 2 to reach 5 = 9. Matches the output.

### Notes on the bonus

Implementing Dijkstra after BFS made the difference between weighted and unweighted shortest paths concrete. BFS finds shortest paths in terms of edge count, which is exactly Dijkstra with all weights equal to 1 (and indeed if I call dijkstra on the original unweighted demo graph where every edge defaults to weight 1, the distances match the BFS levels). The "always pick the closest unvisited vertex" greedy step is what BFS does too, just with a queue because all distances are equal so FIFO order is enough.

The O(V²) version without a priority queue is also a good reminder that Big-O is not the whole story for small inputs. For V=6 the linear scan inside the loop is faster than allocating and maintaining a heap.