package Lab03.A;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vladislav.trofimov@emc.com
 */
public class nodepow {

    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";

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

        List<Integer> result = graph.getNodePows();

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        for (int size : result) {
            out.print(size + " ");
        }

        in.close();
        out.close();
    }

    static class Graph {
        private ArrayList<Set<Integer>> g;

        public Graph(int n) {
            this.g = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                this.g.add(new HashSet<>());
            }
        }

        public void addEdge(int a, int b) {
            a--;
            b--;
            g.get(a).add(b);
            g.get(b).add(a);
        }

        public List<Integer> getNodePows() {
            return g.stream().map(Set<Integer>::size).collect(Collectors.toCollection(LinkedList::new));
        }

    }

}
