package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//Inspired from DFS
//The code uses recursion and a set to contain the found elements
public class TheMatrix {
    private static char[][] matrix;
    private static char fillChar;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] dimensions = splitToIntArray(reader.readLine());
        matrix = new char[dimensions[0]][dimensions[1]];

        for (int row = 0; row < matrix.length; row++) {
            matrix[row] = String.join("", reader.readLine().split("\\s+"))
                    .toCharArray();
        }

        fillChar = reader.readLine().charAt(0);

        int[] coordinates = splitToIntArray(reader.readLine());

        Set<String> common = new HashSet<>();
        searchForCommon(common, coordinates[0], coordinates[1]);

        StringBuilder output = new StringBuilder();

        for (char[] row : matrix) {
            output.append(row).append(System.lineSeparator());
        }

        System.out.println(output.toString().trim());
    }

    private static void searchForCommon(Set<String> common, int row, int col) {
        if (isInRange(row, col)) {
            char element = matrix[row][col];
            common.add(getKey(row, col));

            matrix[row][col] = fillChar;

//            System.out.printf("Visited: matrix[%d][%d]%n", row, col);
//            Used to show the steps the algorithm takes

            move(common, element, row - 1, col);//up
            move(common, element, row + 1, col);//down
            move(common, element, row, col - 1);//left
            move(common, element, row, col + 1);//right
        }
    }

    private static void move(Set<String> common, char element, int row, int col) {
        if (isInRange(row, col) && element == matrix[row][col]) {
            String key = getKey(row, col);

            if (!common.contains(key)) {
                searchForCommon(common, row, col);
            }
        }
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    private static String getKey(int row, int col) {
        return String.format("%d %d", row, col);
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }
}