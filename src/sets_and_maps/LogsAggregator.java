package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LogsAggregator {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Set<String>> logs = new TreeMap<>();
        Map<String, Integer> totalDurations = new HashMap<>();

        int lines = Integer.parseInt(reader.readLine());

        while (lines-- > 0) {
            String[] data = reader.readLine().split("\\s+");

            String ip = data[0];
            String user = data[1];
            int duration = Integer.parseInt(data[2]);

            logs.putIfAbsent(user, new TreeSet<>());
            logs.get(user).add(ip);

            totalDurations.putIfAbsent(user, 0);
            totalDurations.put(user, totalDurations.get(user) + duration);
        }

        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Set<String>> entry : logs.entrySet()) {
            output.append(String.format("%s: %d %s%n"
                    , entry.getKey()
                    , totalDurations.get(entry.getKey())
                    , entry.getValue().toString()));
        }

        System.out.println(output);
    }
}