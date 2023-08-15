package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crossfire {
    private static List<List<Integer>> field;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] sizes = splitToIntArray(reader.readLine());

        int start = 1;
        field = new ArrayList<>();

        for (int row = 0; row < sizes[0]; row++) {
            field.add(new ArrayList<>());

            for (int col = 0; col < sizes[1]; col++) {
                field.get(row).add(start++);
            }
        }

        String input;

        while (!"Nuke it from orbit".equals(input = reader.readLine())) {
            int[] data = splitToIntArray(input);

            clearVertical(data[0], data[1], data[2]);
            clearHorizontal(data[0], data[1], data[2]);
        }

        StringBuilder output = new StringBuilder();

        for (List<Integer> row : field) {
            for (Integer element : row) {
                output.append(element).append(" ");
            }

            output.append(System.lineSeparator());
        }

        System.out.println(output.toString().trim());
    }

    private static void clearHorizontal(int row, int start, int radius) {
        if (isInRange(row)) {
            for (int col = getMax(start, radius); col < getMin(field.get(row).size(), start, radius); col++) {
                field.get(row).remove(col);

                radius--;
                col--;
            }

            if (field.get(row).isEmpty()) {
                field.remove(row);
            }
        }
    }

    private static void clearVertical(int x, int y, int radius) {
        for (int rows = getMax(x, radius); rows < getMin(field.size(), x, radius); rows++) {
            if (isInRange(rows, y) && rows != x) {
                field.get(rows).remove(y);
            }
        }
    }

    private static boolean isInRange(int row) {
        return row >= 0 && row < field.size();
    }

    private static boolean isInRange(int row, int col) {
        return isInRange(row) && col >= 0 && col < field.get(row).size();
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static int getMax(int start, int radius) {
        return Math.max(0, start - radius);
    }

    private static int getMin(int length, int start, int radius) {
        return (int) Math.min(length, (long) start + radius + 1);
    }
}