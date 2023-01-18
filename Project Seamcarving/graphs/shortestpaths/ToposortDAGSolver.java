package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;
import minpq.DoubleMapMinPQ;
import minpq.ExtrinsicMinPQ;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        this.edgeTo = new HashMap<>();
        this.distTo = new HashMap<>();
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsPostOrder(graph, start, visited, result);
        edgeTo.put(start, null);            // Initialize start in edgeTo
        distTo.put(start, 0.0);             // Initialize start in distTo
        Collections.reverse(result);
        for (V vertex: result) {
            for (Edge<V> e : graph.neighbors(vertex)) {
                V to = e.to;
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(vertex) + e.weight;
                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                }
            }
        }
        // throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        List<Edge<V>> neighbors = graph.neighbors(start);
        visited.add(start);
        if (!neighbors.isEmpty()) {
            for (Edge<V> neighbor : neighbors) {
                if (!visited.contains(neighbor.to)) {
                    dfsPostOrder(graph, neighbor.to, visited, result);
                }
            }
            //Add Start after visiting all neighbors.
            result.add(start);
        }
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
