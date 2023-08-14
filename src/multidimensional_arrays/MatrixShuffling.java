package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatrixShuffling {
    private static String[][] matrix;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int rows = Integer.parseInt(reader.readLine().split("\\s+")[0]);

        matrix = new String[rows][];

        for (int row = 0; row < rows; row++) {
            matrix[row] = reader.readLine().split("\\s+");
        }

        String input;

        while (!"END".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            if ("swap".equals(data[0]) && data.length == 5) {
                int firstX = Integer.parseInt(data[1]);
                int firstY = Integer.parseInt(data[2]);

                int secondX = Integer.parseInt(data[3]);
                int secondY = Integer.parseInt(data[4]);

                if (isValid(firstX, firstY) && isValid(secondX, secondY)) {
                    String temp = matrix[firstX][firstY];

                    matrix[firstX][firstY] = matrix[secondX][secondY];
                    matrix[secondX][secondY] = temp;

                    System.out.println(matrixToString());

                    continue;
                }
            }

            System.out.println("Invalid input!");
        }
    }

    private static boolean isValid(int x, int y) {
        return (x >= 0 && x < matrix.length) && (y >= 0 && y < matrix[x].length);
    }

    private static String matrixToString() {
        StringBuilder output = new StringBuilder();

        for (String[] row : matrix) {
            output.append(String.join(" ", row));
            output.append(System.lineSeparator());
        }

        return output.toString().trim();
    }
}