package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Robotics {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String input = reader.readLine();

        int[] timeData = Arrays.stream(reader.readLine().split(":+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        LocalTime time = LocalTime.of(timeData[0], timeData[1], timeData[2]);

        List<Robot> robots = Arrays.stream(input.split(";+"))
                .map(e -> e.split("-+"))
                .map(e -> new Robot(e[0], Integer.parseInt(e[1]), 0))
                .collect(Collectors.toList());

        Deque<String> items = new ArrayDeque<>();

        while (!"End".equals(input = reader.readLine())) {
            items.offer(input);
        }

        while (!items.isEmpty() && !robots.isEmpty()) {
            time = time.plusSeconds(1);

            String item = items.poll();

            if (!findAvailable(robots, time, item)) {
                items.offer(item);
            }
        }
    }

    private static boolean findAvailable(List<Robot> robots, LocalTime time, String item) {
        boolean wasAccepted = false;

        for (Robot worker : robots) {
            if (!wasAccepted && worker.getWorkLeft() <= 0) {
                System.out.printf("%s - %s [%s]%n"
                        , worker.getName()
                        , item
                        , time.format(DateTimeFormatter.ISO_TIME).substring(0, 8));

                worker.setWorkLeft(worker.getEfficiency());

                wasAccepted = true;
            }

            worker.work();
        }

        return wasAccepted;
    }

    private static class Robot {
        private String name;
        private int efficiency;

        private int workLeft;

        public Robot(String name, int efficiency, int workLeft) {
            this.name = name;
            this.efficiency = efficiency;

            this.workLeft = workLeft;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getEfficiency() {
            return this.efficiency;
        }

        public void setEfficiency(int efficiency) {
            this.efficiency = efficiency;
        }

        public int getWorkLeft() {
            return this.workLeft;
        }

        public void setWorkLeft(int workLeft) {
            this.workLeft = workLeft;
        }

        public void work() {
            this.workLeft--;
        }
    }
}