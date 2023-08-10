package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class PoisonousPlants {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int lastSurvivors = Integer.parseInt(reader.readLine());
        int days = 0;

        Deque<Long> plants = new ArrayDeque<>();

        Arrays.stream(reader.readLine().split("\\s+"))
                .mapToLong(Long::parseLong)
                .forEach(plants::push);

        while (true) {
            Deque<Long> survivors = new ArrayDeque<>();

            while (!plants.isEmpty()) {
                long current = plants.pop();

                if (plants.peek() == null || plants.peek() >= current) {
                    survivors.offer(current);
                }
            }

            if (lastSurvivors == survivors.size()) {
                break;
            }

            plants.addAll(survivors);
            lastSurvivors = plants.size();
            days++;
        }

        System.out.println(days);
    }
}