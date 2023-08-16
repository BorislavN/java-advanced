package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ReverseMatrixDiagonals {
    private static int[][] matrix;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int rows = Integer.parseInt(reader.readLine().split("\\s+")[0]);
        matrix = new int[rows][];

        for (int row = 0; row < rows; row++) {
            matrix[row] = Arrays.stream(reader.readLine().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        StringBuilder output = new StringBuilder();

        int rowIndex = matrix.length;
        int colIndex = matrix[rowIndex - 1].length;

        while (rowIndex-- > 0) {
            while (colIndex-- > 0) {
                int tempRow = rowIndex;
                int tempCol = colIndex;

                while (isInRange(tempRow, tempCol)) {
                    output.append(matrix[tempRow--][tempCol++]).append(" ");
                }

                output.append(System.lineSeparator());
            }
            colIndex = 1;
        }

        System.out.println(output.toString().trim());
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }
}