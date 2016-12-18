package Lab02.F;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author vladislav.trofimov@emc.com
 */
public class game {

    private static final String INPUT_FILE_NAME = "game.in";
    private static final String OUTPUT_FILE_NAME = "game.out";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        StringTokenizer stringTokenizer = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());
        int s = Integer.parseInt(stringTokenizer.nextToken());
        Graph graph = new Graph(n);

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph.addEdge(a, b);
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);
        out.println(graph.isFirstWin(s) ? "First player wins" : "Second player wins");

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private boolean[] used;
        private boolean[] wins;
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.g = new ArrayList<>(n);
            this.used = new boolean[n];
            this.wins = new boolean[n];
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
            }
        }

        public boolean isFirstWin(int start) {
            for (int vertex = 0; vertex < n; vertex++) {
                if (!used[vertex]) {
                    dfs(vertex);
                }
            }
            return wins[start - 1];
        }

        private void dfs(int vertex) {
            used[vertex] = true;
            boolean hasLostChild = false;
            if (g.get(vertex).isEmpty()) {
                wins[vertex] = false;
            } else {
                for (int relatedVertex : g.get(vertex)) {
                    if (!used[relatedVertex]) {
                        dfs(relatedVertex);
                    }
                    hasLostChild = (!wins[relatedVertex]) || hasLostChild;
                }
                if (hasLostChild) {
                    wins[vertex] = true;
                }
            }
        }

        public void addEdge(int a, int b) {
            a--;
            b--;
            g.get(a).add(b);
        }
    }

}
