package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;

public class BasicQueueOperations {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] data = splitToArray(reader.readLine());
        int[] values = splitToArray(reader.readLine());
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        boolean hasTargetElement = false;

        int start = data[1];
        int toTake = start + Math.max(0, (data[0] - data[1]));

        for (int i = start; i < Math.min(toTake, values.length); i++) {
            if (data[2] == values[i]) {
                hasTargetElement = true;

                break;
            }

            queue.offer(values[i]);
        }

        System.out.println(hasTargetElement ? "true" : queue.stream().min(Integer::compareTo).orElse(0));
    }

    private static int[] splitToArray(String data) {
        return Arrays.stream(data.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }
}