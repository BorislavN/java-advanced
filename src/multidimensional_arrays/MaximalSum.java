package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MaximalSum {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] data = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        int[][] matrix = new int[data[0]][data[1]];

        for (int row = 0; row < matrix.length; row++) {
            matrix[row] = Arrays.stream(reader.readLine().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        System.out.println(getSum(matrix));
    }

    private static String getSum(int[][] matrix) {
        int max = Integer.MIN_VALUE;
        int startX = -1;
        int startY = -1;

        for (int row = 0; row < matrix.length - 2; row++) {
            for (int col = 0; col < matrix[row].length - 2; col++) {
                int sum = sumRow(matrix, row, col) + sumRow(matrix, row + 1, col) + sumRow(matrix, row + 2, col);

                if (sum > max) {
                    max = sum;

                    startX = row;
                    startY = col;
                }
            }
        }

        StringBuilder output = new StringBuilder();

        output.append("Sum = ").append(max);
        output.append(System.lineSeparator());

        for (int row = startX; row <= startX + 2; row++) {
            for (int col = startY; col <= startY + 2; col++) {
                output.append(matrix[row][col]).append(" ");
            }
            output.append(System.lineSeparator());
        }

        return output.toString();
    }

    private static int sumRow(int[][] matrix, int row, int col) {
        return matrix[row][col] + matrix[row][col + 1] + matrix[row][col + 2];
    }
}