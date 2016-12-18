package Lab01.D;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class components {

    private static final String INPUT_FILE_NAME = "components.in";
    private static final String OUTPUT_FILE_NAME = "components.out";

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

        Pair<Integer, int[]> res = graph.getNormalizedComponents();

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println(res.getKey());
        for (int number : res.getValue()) {
            out.print(number + " ");
        }

        in.close();
        out.close();
    }

    static class Graph {
        private int n;
        private boolean used[];
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.n = n;
            this.g = new ArrayList<>(n);
            this.used = new boolean[n];
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
            }
        }

        private void dfs(int vertex, ArrayList<Integer> components) {
            used[vertex] = true;
            components.add(vertex);
            g.get(vertex).stream().filter(relatedVertex -> !used[relatedVertex]).forEach(relatedVertex -> dfs(relatedVertex, components));
        }

        private ArrayList<ArrayList<Integer>> getAllComponents() {
            ArrayList<ArrayList<Integer>> components = new ArrayList<>();
            Arrays.fill(used, false);
            for (int vertex = 0; vertex < n; vertex++) {
                if (!used[vertex]) {
                    ArrayList<Integer> component = new ArrayList<>();
                    dfs(vertex, component);
                    components.add(component);
                }
            }
            return components;
        }

        public void addEdge(int a, int b) {
            a--; b--;
            g.get(a).add(b);
            g.get(b).add(a);
        }

        public Pair<Integer, int[]> getNormalizedComponents() {
            int[] result = new int[n];
            ArrayList<ArrayList<Integer>> components = getAllComponents();
            for (int i = 0; i < components.size(); i++) {
                ArrayList<Integer> component = components.get(i);
                for (int vertex : component) {
                    result[vertex] = i + 1;
                }
            }
            return new Pair<>(components.size(), result);
        }
    }

}