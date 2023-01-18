package seamcarving.seamfinding;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.ShortestPathSolver;
import seamcarving.Picture;
import seamcarving.SeamCarver;
import seamcarving.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generative adjacency list graph single-source {@link ShortestPathSolver} implementation of the {@link SeamFinder}
 * interface.
 *
 * @see Graph
 * @see ShortestPathSolver
 * @see SeamFinder
 * @see SeamCarver
 */
public class GenerativeSeamFinder implements SeamFinder {
    /**
     * The constructor for the {@link ShortestPathSolver} implementation.
     */
    private final ShortestPathSolver.Constructor<Node> sps;

    /**
     * Constructs an instance with the given {@link ShortestPathSolver} implementation.
     *
     * @param sps the {@link ShortestPathSolver} implementation.
     */
    public GenerativeSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1); // Skip the source and sink nodes
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node node : seam) {
            // All remaining nodes must be Pixels
            PixelGraph.Pixel pixel = (PixelGraph.Pixel) node;
            result.add(pixel.y);
        }
        return result;
    }

    /**
     * Generative adjacency list graph of {@link Pixel} vertices and {@link EnergyFunction}-weighted edges. Rather than
     * materialize all vertices and edges upfront in the constructor, generates vertices and edges as needed when
     * {@link #neighbors(Node)} is called by a client.
     *
     * @see Pixel
     * @see EnergyFunction
     */
    private static class PixelGraph implements Graph<Node> {
        /**
         * The {@link Picture} for {@link #neighbors(Node)}.
         */
        private final Picture picture;
        /**
         * The {@link EnergyFunction} for {@link #neighbors(Node)}.
         */
        private final EnergyFunction f;
        /**
         * Source {@link Node} for the adjacency list graph.
         */
        private final Node source = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>();
                for (int i = 0; i < picture.height(); i++) {
                    Pixel to = new Pixel(0, i);
                    result.add(new Edge<>(this, to, f.apply(picture, 0, i)));
                }
                return result;
                //throw new UnsupportedOperationException("Not implemented yet");
            }
        };
        /**
         * Sink {@link Node} for the adjacency list graph.
         */
        private final Node sink = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of(); // Sink has no neighbors
                //throw new UnsupportedOperationException("Not implemented yet");
            }
        };

        /**
         * Constructs a generative adjacency list graph. All work is deferred to implementations of
         * {@link Node#neighbors(Picture, EnergyFunction)}.
         *
         * @param picture the input picture.
         * @param f       the input energy function.
         */
        private PixelGraph(Picture picture, EnergyFunction f) {
            this.picture = picture;
            this.f = f;
        }

        @Override
        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        /**
         * A pixel in the {@link PixelGraph} representation of the {@link Picture} with {@link EnergyFunction}-weighted
         * edges to neighbors.
         *
         * @see PixelGraph
         * @see Picture
         * @see EnergyFunction
         */
        public class Pixel implements Node {
            private final int x;
            private final int y;

            /**
             * Constructs a pixel representing the (<i>x</i>, <i>y</i>) indices in the picture.
             *
             * @param x horizontal index into the picture.
             * @param y vertical index into the picture.
             */
            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>();
                if (x == picture.width() - 1) {
                    result.add(new Edge<>(this, sink, 0));            //Last Nodes neighbor is Sink with energy cost 0
                    return result;
                } else if (y == picture.height() - 1) {                           //Node at very bottom case
                    Pixel n2 = new Pixel(x + 1, y);
                    Pixel n3 = new Pixel(x + 1, y - 1 );
                    result.add(new Edge<>(this, n2, f.apply(picture, x + 1, y)));
                    result.add(new Edge<>(this, n3, f.apply(picture, x + 1, y - 1)));
                    return result;
                } else if (y == 0) {                                               //Node at very top Case
                    Pixel n1 = new Pixel(x + 1, y + 1);
                    Pixel n2 = new Pixel(x + 1, y);
                    result.add(new Edge<>(this, n1, f.apply(picture, x + 1, y + 1)));
                    result.add(new Edge<>(this, n2, f.apply(picture, x + 1, y)));
                    return result;
                } else {                                                            //Node at normal case
                    Pixel n1 = new Pixel(x + 1, y + 1);
                    Pixel n2 = new Pixel(x + 1, y);
                    Pixel n3 = new Pixel(x + 1, y - 1);
                    result.add(new Edge<>(this, n1, f.apply(picture, x + 1, y + 1)));
                    result.add(new Edge<>(this, n2, f.apply(picture, x + 1, y)));
                    result.add(new Edge<>(this, n3, f.apply(picture, x + 1, y - 1)));
                    return result;
                }
                //throw new UnsupportedOperationException("Not implemented yet");
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
