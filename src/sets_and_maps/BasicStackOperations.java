package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;

public class BasicStackOperations {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] data = splitToArray(reader.readLine());
        int[] values = splitToArray(reader.readLine());
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        boolean hasTargetElement = false;
        int difference = Math.max(0, (data[0] - data[1]));

        for (int i = 0; i < difference; i++) {
            if (data[2] == values[i]) {
                hasTargetElement = true;

                break;
            }

            stack.push(values[i]);
        }

        System.out.println(hasTargetElement ? "true" : stack.stream().min(Integer::compareTo).orElse(0));
    }

    private static int[] splitToArray(String data) {
        return Arrays.stream(data.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }
}