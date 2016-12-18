package Lab03.C;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * @author vladislav.trofimov@emc.com
 */
public class spantree2 {

    private static final String INPUT_FILE_NAME = "spantree2.in";
    private static final String OUTPUT_FILE_NAME = "spantree2.out";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        StringTokenizer stringTokenizer = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken()), m = Integer.parseInt(stringTokenizer.nextToken());

        Graph graph = new Graph(n);

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            graph.addEdge(Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println(graph.getMSTWeight());

        in.close();
        out.close();
    }

    private static class Graph {
        private int n;
        private ArrayList<Node> g;
        private PriorityQueue<Pair> queue;

        public Graph(int n) {
            this.n = n;
            queue = new PriorityQueue<>();
            g = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                g.add(new Node());
            }
        }

        public void addEdge(int a, int b, int weight) {
            g.get(a).edges.add(g.get(b));
            g.get(a).weight.add(weight);
            g.get(b).edges.add(g.get(a));
            g.get(b).weight.add(weight);
        }

        public int getMSTWeight() {
            g.get(0).minWeight = 0;
            queue.add(new Pair(g.get(0), 0));
            while (!queue.isEmpty()) {
                Pair p = queue.poll();
                Node vertex = p.node;
                if (!vertex.used) {
                    vertex.used = true;
                    for (int relatedVertexIndex = 0; relatedVertexIndex < vertex.edges.size(); relatedVertexIndex++) {
                        Node relatedVertex = vertex.edges.get(relatedVertexIndex);
                        if (!relatedVertex.used && vertex.weight.get(relatedVertexIndex) < relatedVertex.minWeight) {
                            relatedVertex.minWeight = vertex.weight.get(relatedVertexIndex);
                            queue.add(new Pair(relatedVertex, relatedVertex.minWeight));
                        }
                    }
                }
            }
            double weight = 0;
            for (int vertex = 0; vertex < n; vertex++) {
                weight += g.get(vertex).minWeight;
            }
            return (int) weight;
        }

        private static class Node {
            public boolean used;
            public int minWeight;
            public ArrayList<Node> edges;
            public ArrayList<Integer> weight;

            public Node() {
                this.used = false;
                this.edges = new ArrayList<>();
                this.weight = new ArrayList<>();
                this.minWeight = Integer.MAX_VALUE;
            }
        }

        private static class Pair implements Comparable<Pair> {
            Node node;
            Integer minWeight;

            public Pair(Node n, int w) {
                minWeight = w;
                node = n;
            }

            @Override
            public int compareTo(@SuppressWarnings("NullableProblems") Pair p) {
                return minWeight.compareTo(p.minWeight);
            }
        }
    }

}