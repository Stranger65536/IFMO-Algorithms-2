package Lab02.C;

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
public class bipartite {

    private static final String INPUT_FILE_NAME = "bipartite.in";
    private static final String OUTPUT_FILE_NAME = "bipartite.out";

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
        out.println(graph.isBipartite() ? "YES" : "NO");

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private int[] colors;
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.colors = new int[n];
            this.g = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
            }
        }

        public void addEdge(int a, int b) {
            a--;b--;
            g.get(a).add(b);
            g.get(b).add(a);
        }

        public boolean isBipartite() {
            for (int vertex = 0; vertex < n; vertex++) {
                if (colors[vertex] == 0) {
                    colors[vertex] = 1;
                    if (!dfs(vertex)) {
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean dfs(int vertex) {
            for (int relatedVertex : g.get(vertex)) {
                if (colors[relatedVertex] == 0) {
                    colors[relatedVertex] = 3 - colors[vertex];
                    if(!dfs(relatedVertex)){
                        return false;
                    }
                } else if (colors[relatedVertex] == colors[vertex]) {
                    return false;
                }
            }
            return true;
        }
    }

}
