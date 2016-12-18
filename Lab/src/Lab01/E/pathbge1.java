package Lab01.E;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class pathbge1 {

    private static final String INPUT_FILE_NAME = "pathbge1.in";
    private static final String OUTPUT_FILE_NAME = "pathbge1.out";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        StringTokenizer stringTokenizer = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());
        Graph graph = new Graph(n);

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph.addEdge(a, b);
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        for (int distance : graph.getShortestPaths(0)) {
            out.print(distance + " ");
        }

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private int[] distances;
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.distances = new int[n];
            this.g = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
            }
        }

        public void addEdge(int a, int b) {
            a--; b--;
            g.get(a).add(b);
            g.get(b).add(a);
        }

        private int[] getShortestPaths(int startVertex) {
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[startVertex] = 0;
            PriorityQueue<Long> q = new PriorityQueue<>();
            q.add((long) startVertex);
            while (!q.isEmpty()) {
                long currentVertexExtended = q.remove();
                int currentVertex = (int) currentVertexExtended;
                if (currentVertexExtended >>> 32 != distances[currentVertex]) {
                    continue;
                }
                for (int relatedVertex : g.get(currentVertex)) {
                    int distance = distances[currentVertex] + 1;
                    if (distances[relatedVertex] > distance) {
                        distances[relatedVertex] = distance;
                        q.add(((long) distance << 32) + relatedVertex);
                    }
                }
            }
            return distances;
        }

    }

}