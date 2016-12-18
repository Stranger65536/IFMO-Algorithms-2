package Lab02.A;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class topsort {

    private static final String INPUT_FILE_NAME = "topsort.in";
    private static final String OUTPUT_FILE_NAME = "topsort.out";

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

        ArrayList<Integer> result = graph.getTopSort();

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        if (result != null) {
            for (int vertex : result) {
                out.print(vertex + " ");
            }
        } else {
            out.println("-1");
        }

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private Colors colors[];
        private boolean hasCycle;
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

        public ArrayList<Integer> getTopSort() {
            ArrayList<Integer> result = new ArrayList<>();
            for (int vertex = 0; vertex < n; vertex++) {
                if (colors[vertex] == Colors.WHITE && dfs(vertex, result)) {
                    break;
                }
            }
            if (!hasCycle) {
                Collections.reverse(result);
                return result;
            }
            return null;
        }

        private boolean dfs(int vertex, ArrayList<Integer> result) {
            colors[vertex] = Colors.GREY;
            for (int relatedVertex : g.get(vertex)) {
                if (colors[relatedVertex] == Colors.WHITE) {
                    if (dfs(relatedVertex, result)) {
                        return true;
                    }
                } else if (colors[relatedVertex] == Colors.GREY) {
                    hasCycle = true;
                    return true;
                }
            }
            colors[vertex] = Colors.BLACK;
            result.add(vertex + 1);
            return false;
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