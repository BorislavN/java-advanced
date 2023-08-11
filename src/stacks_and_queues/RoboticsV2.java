package stacks_and_queues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class RoboticsV2 {
    private static int minRemainingTime = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String input = reader.readLine();

        int[] timeData = Arrays.stream(reader.readLine().split(":"))
                .mapToInt(Integer::parseInt)
                .toArray();

        int startTimeSeconds = LocalTime.of(timeData[0], timeData[1], timeData[2])
                .toSecondOfDay();

        String[] robots = input.split(";");
        int[] efficiency = new int[robots.length];
        int[] workLeft = new int[robots.length];

        for (int index = 0; index < robots.length; index++) {
            String[] data = robots[index].split("-");

            robots[index] = data[0];
            efficiency[index] = Integer.parseInt(data[1]);
        }

        Deque<String> items = new ArrayDeque<>();

        while (!"End".equals(input = reader.readLine())) {
            items.offer(input);
        }

        while (!items.isEmpty() && robots.length > 0) {
            //Skips the times the item queue stay looping, waiting for a free robot
            if (minRemainingTime > 0) {
                int value = minRemainingTime - minRemainingTime % items.size();

                startTimeSeconds += value;
                workLeft = Arrays.stream(workLeft).map(time -> time - value).toArray();
            }

            startTimeSeconds++;

            String item = items.poll();

            boolean wasAccepted = false;
            minRemainingTime = Integer.MAX_VALUE;

            for (int index = 0; index < robots.length; index++) {
                if (!wasAccepted && workLeft[index] <= 0) {
                    System.out.printf("%s - %s [%s]%n"
                            , robots[index]
                            , item
                            , LocalTime.ofSecondOfDay(startTimeSeconds % 86400)
                                    .format(DateTimeFormatter.ISO_TIME)
                                    .substring(0, 8));

                    workLeft[index] = efficiency[index];

                    wasAccepted = true;
                }

                workLeft[index] = --workLeft[index];

                if (workLeft[index] < minRemainingTime) {
                    minRemainingTime = workLeft[index];
                }
            }

            if (!wasAccepted) {
                items.offer(item);
            }
        }
    }
}