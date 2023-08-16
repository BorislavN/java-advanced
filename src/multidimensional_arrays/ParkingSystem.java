package multidimensional_arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ParkingSystem {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] dimensions = splitToIntArray(reader.readLine());
        boolean[][] parkingLot = new boolean[dimensions[0]][dimensions[1]];

        String input;

        while (!"stop".equals(input = reader.readLine())) {
            int[] data = splitToIntArray(input);

            int steps = 1 + Math.abs(data[0] - data[1]);

            int spotIndex = parkingLot[data[1]][data[2]] ? findASpot(data[1], data[2], parkingLot) : data[2];

            if (spotIndex != -1) {
                parkingLot[data[1]][spotIndex] = true;
                steps += spotIndex;
            }

            System.out.println(spotIndex != -1 ? steps : String.format("Row %d full", data[1]));
        }
    }

    private static int findASpot(int row, int col, boolean[][] parkingLot) {
        int tempLeft = col;
        int tempRight = col;

        while (isInRange(parkingLot[row].length, tempLeft) || isInRange(parkingLot[row].length, tempRight)) {
            if (isInRange(parkingLot[row].length, --tempLeft)) {
                if (!parkingLot[row][tempLeft]) {
                    return tempLeft;
                }
            }

            if (isInRange(parkingLot[row].length, ++tempRight)) {
                if (!parkingLot[row][tempRight]) {
                    return tempRight;
                }
            }
        }

        return -1;
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }

    private static boolean isInRange(int size, int spot) {
        return spot >= 1 && spot < size;
    }
}