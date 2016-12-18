package Lab01.F;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class maze {

    private static final String INPUT_FILE_NAME = "input.txt";
    private static final String OUTPUT_FILE_NAME = "output.txt";

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        StringTokenizer stringTokenizer = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        Amaze amaze = new Amaze(n, m);

        for (int i = 0; i < n; i++) {
            String row = in.readLine();
            for (int j = 0; j < m; j++) {
                char current = row.charAt(j);
                switch (current) {
                    case 'S':
                        amaze.setStart(i, j);
                        break;
                    case 'T':
                        amaze.setFinish(i, j);
                        break;
                    case '#':
                        amaze.setWall(i, j);
                        break;
                }
            }
        }

        char[] path = amaze.calculateRoot();

        PrintWriter out = new PrintWriter(OUTPUT_FILE_NAME);

        out.println((path.length == 0) ? -1 : path.length);
        for (char c : path) {
            out.print(c);
        }

        in.close();
        out.close();
    }

    static class Amaze {
        private int n, m;
        private Pair<Integer, Integer> start;
        private Pair<Integer, Integer> finish;
        private int[][] field;
        private Queue<Pair<Integer, Integer>> queue;

        public Amaze(int n, int m) {
            this.n = n;
            this.m = m;
            this.field = new int[n][m];
            this.queue = new LinkedList<>();
        }

        public void setStart(int n, int m) {
            this.start = new Pair<>(n, m);
        }

        public void setFinish(int n, int m) {
            this.finish = new Pair<>(n, m);
        }

        public void setWall(int n, int m) {
            this.field[n][m] = Integer.MIN_VALUE;
        }

        private boolean isWall(Pair<Integer, Integer> position) {
            return this.field[position.getKey()][position.getValue()] == Integer.MIN_VALUE;
        }

        private boolean isStart(Pair<Integer, Integer> position) {
            return this.start.getKey().equals(position.getKey()) && this.start.getValue().equals(position.getValue());
        }

        private boolean isUnvisited(Pair<Integer, Integer> position) {
            return this.field[position.getKey()][position.getValue()] == 0;
        }

        private boolean isShortest(Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
            return this.field[first.getKey()][first.getValue()] + 1 < this.field[second.getKey()][second.getValue()];
        }

        private boolean isClosest(Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
            return this.field[first.getKey()][first.getValue()] + 1 == this.field[second.getKey()][second.getValue()];
        }

        private Pair<Integer, Integer> getLeftPosition(Pair<Integer, Integer> position) {
            return position.getValue() > 0 ? new Pair<>(position.getKey(), position.getValue() - 1) : null;
        }

        private Pair<Integer, Integer> getRightPosition(Pair<Integer, Integer> position) {
            return position.getValue() < (this.m - 1) ? new Pair<>(position.getKey(), position.getValue() + 1) : null;
        }

        private Pair<Integer, Integer> getTopPosition(Pair<Integer, Integer> position) {
            return position.getKey() > 0 ? new Pair<>(position.getKey() - 1, position.getValue()) : null;
        }

        private Pair<Integer, Integer> getBottomPosition(Pair<Integer, Integer> position) {
            return position.getKey() < (this.n - 1) ? new Pair<>(position.getKey() + 1, position.getValue()) : null;
        }

        private List<Pair<Integer, Integer>> getClosurePositions(Pair<Integer, Integer> position) {
            List<Pair<Integer, Integer>> result = new ArrayList<>(4);
            Pair<Integer, Integer> left = getLeftPosition(position);
            Pair<Integer, Integer> right = getRightPosition(position);
            Pair<Integer, Integer> top = getTopPosition(position);
            Pair<Integer, Integer> bottom = getBottomPosition(position);
            if (left != null && !isWall(left)) result.add(left);
            if (right != null && !isWall(right)) result.add(right);
            if (top != null && !isWall(top)) result.add(top);
            if (bottom != null && !isWall(bottom)) result.add(bottom);
            return result;
        }

        public char[] calculateRoot() {
            wave(start);
            Pair<Integer, Integer> lastStep = finish;
            char[] result = new char[field[finish.getKey()][finish.getValue()]];
            for (int i = result.length - 1; i >= 0; i--) {
                Pair<Directions, Pair<Integer, Integer>> nextStep = getNextStep(lastStep);
                result[i] = nextStep.getKey().toString().charAt(0);
                lastStep = nextStep.getValue();
            }
            return result;
        }

        private Pair<Directions, Pair<Integer, Integer>> getNextStep(Pair<Integer, Integer> position) {
            Pair<Integer, Integer> left = getLeftPosition(position);
            Pair<Integer, Integer> right = getRightPosition(position);
            Pair<Integer, Integer> top = getTopPosition(position);
            Pair<Integer, Integer> bottom = getBottomPosition(position);
            if (left != null && isClosest(left, position)) {
                return new Pair<>(Directions.RIGHT, left);
            }
            if (right != null && isClosest(right, position)) {
                return new Pair<>(Directions.LEFT, right);
            }
            if (top != null && isClosest(top, position)) {
                return new Pair<>(Directions.BOTTOM, top);
            }
            if (bottom != null && isClosest(bottom, position)) {
                return new Pair<>(Directions.TOP, bottom);
            } else {
                return null;
            }
        }

        private void wave(Pair<Integer, Integer> position) {
            queue.add(position);
            while (!queue.isEmpty()) {
                position = queue.poll();
                List<Pair<Integer, Integer>> closurePositions = getClosurePositions(position);
                for (Pair<Integer, Integer> newPosition : closurePositions) {
                    if (!isStart(newPosition) && (isUnvisited(newPosition) || isShortest(position, newPosition))) {
                        field[newPosition.getKey()][newPosition.getValue()] = field[position.getKey()][position.getValue()] + 1;
                        queue.add(newPosition);
                    }
                }
            }
        }

        static enum Directions {
            LEFT("L"),
            RIGHT("R"),
            TOP("U"),
            BOTTOM("D");

            private final String text;

            Directions(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }

}
