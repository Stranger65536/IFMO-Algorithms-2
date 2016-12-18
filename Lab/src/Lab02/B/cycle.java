package Lab02.B;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class cycle {

    private static final String INPUT_FILE_NAME = "cycle.in";
    private static final String OUTPUT_FILE_NAME = "cycle.out";

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

        ArrayList<Integer> result = graph.getCycle();

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        if (result != null) {
            out.println("YES");
            for (int vertex : result) {
                out.print(vertex + " ");
            }
        } else {
            out.println("NO");
        }

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private Colors colors[];
        private int path[];
        private Integer cycleStart;
        private Integer cycleEnd;
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.path = new int[n];
            this.g = new ArrayList<>(n);
            this.colors = new Colors[n];
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
                this.colors[i] = Colors.WHITE;
            }
        }

        public ArrayList<Integer> getCycle() {
            for (int vertex = 0; vertex < n; vertex++) {
                if (dfs(vertex)) {
                    break;
                }
            }
            if (cycleStart != null) {
                ArrayList<Integer> result = new ArrayList<>(n);
                result.add(cycleStart + 1);
                for (int vertex = cycleEnd; vertex != cycleStart; vertex = path[vertex]) {
                    result.add(vertex + 1);
                }
                Collections.reverse(result);
                return result;
            }
            return null;
        }

        private boolean dfs(int vertex) {
            colors[vertex] = Colors.GREY;
            for (int relatedVertex : g.get(vertex)) {
                if (colors[relatedVertex] == Colors.WHITE) {
                    path[relatedVertex] = vertex;
                    if (dfs(relatedVertex)) {
                        return true;
                    }
                } else if (colors[relatedVertex] == Colors.GREY) {
                    cycleEnd = vertex;
                    cycleStart = relatedVertex;
                    return true;
                }
            }
            colors[vertex] = Colors.BLACK;
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