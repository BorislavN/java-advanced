package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//A proper DFS recursive implementation
//Way faster than my original solution with the HashSet
public class TheMatrixDFS {
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
        if (isInRange(row, col)) {
            char current = matrix[row][col];

//            System.out.printf("DFS visited: matrix[%d][%d]%n", row, col);
//            Used to show the steps the algorithm takes

            visited[row][col] = true;
            matrix[row][col] = fillChar;

            move(current, row - 1, col);//up
            move(current, row + 1, col);//down
            move(current, row, col - 1);//left
            move(current, row, col + 1);//right
        }
    }

    private static void move(char element, int row, int col) {
        if (isInRange(row, col) && element == matrix[row][col]) {
            if (!visited[row][col]) {
                searchForCommon(row, col);
            }
        }
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }
}