package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TheMatrix {
    private static String[][] matrix;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] dimensions = splitToIntArray(reader.readLine());
        matrix = new String[dimensions[0]][dimensions[1]];

        for (int row = 0; row < matrix.length; row++) {
            matrix[row] = reader.readLine().split("\\s+");
        }

        String fillChar = reader.readLine();
        int[] coordinates = splitToIntArray(reader.readLine());

        Set<String> common = new HashSet<>();

        searchForCommon(common, coordinates[0], coordinates[1]);

        for (String element : common) {
            int[] data = splitToIntArray(element);
            matrix[data[0]][data[1]] = fillChar;
        }

        StringBuilder output = new StringBuilder();

        for (String[] row : matrix) {
            output.append(String.join("", row));
            output.append(System.lineSeparator());
        }

        System.out.println(output.toString().trim());
    }

    private static void searchForCommon(Set<String> common, int row, int col) {
        if (isInRange(row, col)) {
            common.add(getKey(row, col));

            move(common, matrix[row][col], row - 1, col);//up
            move(common, matrix[row][col], row + 1, col);//down
            move(common, matrix[row][col], row, col - 1);//left
            move(common, matrix[row][col], row, col + 1);//right
        }
    }

    private static void move(Set<String> common, String element, int row, int col) {
        if (isInRange(row, col) && element.equals(matrix[row][col])) {
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