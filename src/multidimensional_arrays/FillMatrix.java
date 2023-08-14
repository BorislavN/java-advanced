package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FillMatrix {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] data = reader.readLine().split(", ");
        int size = Integer.parseInt(data[0]);

        System.out.println(printMatrix("A".equals(data[1]) ? fillA(size) : fillB(size)));
    }

    private static int[][] fillA(int size) {
        int[][] matrix = new int[size][size];

        int value = 1;

        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                matrix[row][col] = value++;
            }
        }

        return matrix;
    }

    private static int[][] fillB(int size) {
        int[][] matrix = new int[size][size];

        int value = 1;

        for (int col = 0; col < size; col++) {
            if (col % 2 == 0) {
                for (int row = 0; row < size; row ++) {
                    matrix[row][col] = value++;
                }
            } else {
                for (int row = size - 1; row >= 0; row--) {
                    matrix[row][col] = value++;
                }
            }
        }

        return matrix;
    }

    private static String printMatrix(int[][] matrix) {
        StringBuilder output = new StringBuilder();

        for (int[] row : matrix) {
            output.append(Arrays.toString(row).replaceAll("[\\[\\],]", ""));
            output.append(System.lineSeparator());
        }

        return output.toString();
    }
}