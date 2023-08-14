package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatrixPalindromes {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] data = reader.readLine().split("\\s+");

        System.out.println(generateMatrix(Integer.parseInt(data[0]), Integer.parseInt(data[1])));
    }

    private static String generateMatrix(int rows, int cols) {
        StringBuilder output = new StringBuilder();

        String[][] matrix = new String[rows][cols];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = String.format("%c%c%c", 97 + row, 97 + row + col, 97 + row);
                output.append(matrix[row][col]).append(" ");
            }
            output.append(System.lineSeparator());
        }

        return output.toString();
    }
}