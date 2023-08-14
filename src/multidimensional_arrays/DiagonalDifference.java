package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class DiagonalDifference {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int lines = Integer.parseInt(reader.readLine());

        int[][] matrix = new int[lines][];

        for (int i = 0; i < lines; i++) {
            matrix[i] = Arrays.stream(reader.readLine().split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        System.out.println(findDifference(matrix));
    }

    private static int findDifference(int[][] matrix) {
        int sumA = 0;
        int sumB = 0;

        for (int row = 0; row < matrix.length; row++) {
            sumA += matrix[row][row];

            sumB += matrix[row][matrix.length - 1 - row];
        }

        return Math.abs(sumA - sumB);
    }
}