package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

//A proper BFS implementation
//The speed of the algorithm is comparable to the original solution
//They both use a dedicated collection to store the coordinates of the visited cells
//The DFS solution is fastest
public class TheMatrixBFS {
    private static char[][] matrix;
    private static boolean[][] visited;
    private static char fillChar;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] dimensions = splitToIntArray(reader.readLine());

        matrix = new char[dimensions[0]][dimensions[1]];
        visited = new boolean[dimensions[0]][dimensions[1]];

        for (int row = 0; row < matrix.length; row++) {
            matrix[row] = String.join("", reader.readLine().split("\\s+"))
                    .toCharArray();
        }

        fillChar = reader.readLine().charAt(0);
        int[] coordinates = splitToIntArray(reader.readLine());

        searchForCommon(coordinates[0], coordinates[1]);

        StringBuilder output = new StringBuilder();

        for (char[] row : matrix) {
            output.append(row).append(System.lineSeparator());
        }

        System.out.println(output.toString().trim());
    }

    private static void searchForCommon(int row, int col) {
        Deque<String> queue = new ArrayDeque<>();
        char element = matrix[row][col];

        queue.offer(getKey(row, col));
        visited[row][col] = true;

        while (!queue.isEmpty()) {
            int[] data = splitToIntArray(queue.poll());
            matrix[data[0]][data[1]] = fillChar;

//            System.out.printf("BFS visited: matrix[%d][%d]%n", data[0], data[1]);
//            Used to show the steps the algorithm takes

            move(queue, element, data[0] - 1, data[1]);//up
            move(queue, element, data[0] + 1, data[1]);//down
            move(queue, element, data[0], data[1] - 1);//left
            move(queue, element, data[0], data[1] + 1);//right
        }
    }

    private static void move(Deque<String> queue, char element, int row, int col) {
        if (isInRange(row, col) && element == matrix[row][col]) {
            if (!visited[row][col]) {
                queue.offer(getKey(row, col));
                visited[row][col] = true;
            }
        }
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }

    private static String getKey(int row, int col) {
        return String.format("%d %d", row, col);
    }
}