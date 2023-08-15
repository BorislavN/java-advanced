package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MatrixRotationV2 {
    private static List<char[]> matrix;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int degrees = Integer.parseInt(reader.readLine().replaceAll("\\D", ""));

        String input;
        int maxLength = 0;
        matrix = new ArrayList<>();


        while (!"END".equals(input = reader.readLine())) {
            matrix.add(input.toCharArray());

            maxLength = Math.max(input.length(), maxLength);
        }

        System.out.println(rotateMatrix(degrees, maxLength));
    }

    private static String rotateMatrix(int degrees, int maxLength) {
        StringBuilder output = new StringBuilder();
        degrees %= 360;


        switch (degrees) {
            case 0:
                for (char[] row : matrix) {
                    output.append(row);
                    output.append(System.lineSeparator());
                }

                break;
            case 90:
                for (int col = 0; col < maxLength; col++) {
                    for (int row = matrix.size() - 1; row >= 0; row--) {
                        output.append(isInRange(row, col) ? matrix.get(row)[col] : " ");
                    }
                    output.append(System.lineSeparator());
                }

                break;
            case 180:
                for (int row = matrix.size() - 1; row >= 0; row--) {
                    for (int col = maxLength - 1; col >= 0; col--) {
                        output.append(isInRange(row, col) ? matrix.get(row)[col] : " ");
                    }
                    output.append(System.lineSeparator());
                }

                break;
            case 270:
                for (int col = maxLength - 1; col >= 0; col--) {
                    for (int row = 0; row < matrix.size(); row++) {
                        output.append(isInRange(row, col) ? matrix.get(row)[col] : " ");
                    }
                    output.append(System.lineSeparator());
                }

                break;
        }

        return output.toString().stripTrailing();
    }

    private static boolean isInRange(int row, int col) {
        return row >= 0 && row < matrix.size() && col >= 0 && col < matrix.get(row).length;
    }
}