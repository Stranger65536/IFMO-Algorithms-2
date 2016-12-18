package Lab01.B;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * @author vladislav.trofimov@emc.com
 */
public class isor {

    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        int n = Integer.parseInt(in.readLine());
        boolean[][] matrix = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());
            for (int j = 0; j < n; j++) {
                matrix[i][j] = st.nextToken().equals("1");
            }
        }

        boolean res = true;

        for (int i = 0; i < n; i++) {
            if (matrix[i][i]) {
                res = false;
                break;
            }
            for (int j = 0; j < i + 1; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    res = false;
                    break;
                }
            }
        }

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println(res ? "YES" : "NO");

        in.close();
        out.close();
    }
}
