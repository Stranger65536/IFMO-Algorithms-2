package Lab03.B;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author vladislav.trofimov@emc.com
 */
public class spantree {

    private static final String INPUT_FILE_NAME = "spantree.in";
    private static final String OUTPUT_FILE_NAME = "spantree.out";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        int n = Integer.parseInt(in.readLine());

        Graph graph = new Graph(n);

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            graph.addNode(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println(graph.getMSTWeight());

        in.close();
        out.close();
    }

    static class Graph {
        private ArrayList<Node> g;
        private int n;

        public Graph(int n) {
            this.n = n;
            g = new ArrayList<>(n);
        }

        public void addNode(int x, int y) {
            g.add(new Node(x, y));
        }

        public double getMSTWeight() {
            double weight = 0;
            g.get(0).min = 0;
            for (int vertex = 0; vertex < n; vertex++) {
                int leastCheapEdge = -1;
                for (int relatedVertex = 0; relatedVertex < n; relatedVertex++) {
                    if (!(g.get(relatedVertex).used) && (leastCheapEdge == -1 || (g.get(relatedVertex).min < g.get(leastCheapEdge).min))) {
                        leastCheapEdge = relatedVertex;
                    }
                }
                g.get(leastCheapEdge).used = true;
                weight += g.get(leastCheapEdge).min;
                double distance;
                for (int relatedVertex = 0; relatedVertex < n; relatedVertex++) {
                    distance = g.get(leastCheapEdge).getDistance(g.get(relatedVertex));
                    if (distance < g.get(relatedVertex).min) {
                        g.get(relatedVertex).min = distance;
                    }
                }
            }
            return weight;
        }

        private static class Node {
            public int x, y;
            public double min;
            public boolean used;

            public Node(int x, int y) {
                this.x = x;
                this.y = y;
                used = false;
                min = Double.MAX_VALUE;
            }

            public double getDistance(Node node) {
                return Math.sqrt((x - node.x) * (x - node.x) + (y - node.y) * (y - node.y));
            }
        }
    }

}
