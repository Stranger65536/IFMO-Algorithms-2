package Lab01.C;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * @author vladislav.trofimov@emc.com
 */
public class ispar {

    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        StringTokenizer stringTokenizer = new StringTokenizer(in.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken()), m = Integer.parseInt(stringTokenizer.nextToken());
        boolean[][] matrix = new boolean[n][n];
        boolean res = false;

        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            if (matrix[start - 1][end - 1] || matrix[end - 1][start - 1]) {
                res = true;
                break;
            }
            matrix[start - 1][end - 1] = true;
            matrix[end - 1][start - 1] = true;
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println(res ? "YES" : "NO");

        in.close();
        out.close();
    }
}
