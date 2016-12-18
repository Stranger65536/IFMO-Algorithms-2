package Lab02.E;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class hamiltonian {

    private static final String INPUT_FILE_NAME = "hamiltonian.in";
    private static final String OUTPUT_FILE_NAME = "hamiltonian.out";

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
        out.println(graph.hasHamiltonian() ? "YES" : "NO");

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private Colors colors[];
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.g = new ArrayList<>(n);
            this.colors = new Colors[n];
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
                this.colors[i] = Colors.WHITE;
            }
        }

        public boolean hasHamiltonian() {
            ArrayList<Integer> result = new ArrayList<>(n);
            for (int vertex = 0; vertex < n; vertex++) {
                if (colors[vertex] == Colors.WHITE) {
                    dfs(vertex, result);
                }
            }
            for (int i = result.size() - 1; i > 0; i--) {
                int from = result.get(i);
                int to = result.get(i - 1);
                if (!g.get(from).contains(to)) {
                    return false;
                }
            }
            return true;
        }

        private void dfs(int vertex, ArrayList<Integer> result) {
            colors[vertex] = Colors.GREY;
            //noinspection Convert2streamapi
            for (int relatedVertex : g.get(vertex)) {
                if (colors[relatedVertex] == Colors.WHITE) {
                    dfs(relatedVertex, result);
                }
            }
            colors[vertex] = Colors.BLACK;
            result.add(vertex);
        }

        public void addEdge(int a, int b) {
            a--;
            b--;
            g.get(a).add(b);
        }

        static enum Colors {
            WHITE,
            GREY,
            BLACK
        }
    }

}
