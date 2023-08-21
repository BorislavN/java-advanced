package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class UserLogs {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Map<String, Integer>> register = new TreeMap<>();
        String input;

        while (!"end".equals(input = reader.readLine())) {
            String[] data = Arrays.stream(input.split("\\s+"))
                    .map(e -> e.split("=")[1])
                    .toArray(String[]::new);

            register.putIfAbsent(data[2], new LinkedHashMap<>());
            register.get(data[2]).putIfAbsent(data[0], 0);
            register.get(data[2]).put(data[0], register.get(data[2]).get(data[0]) + 1);
        }

        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Map<String, Integer>> entry : register.entrySet()) {
            output.append(entry.getKey())
                    .append(":")
                    .append(System.lineSeparator());

            output.append(entry.getValue()
                    .entrySet().stream()
                    .map(v -> String.format("%s => %d", v.getKey(), v.getValue()))
                    .collect(Collectors.joining(", "))
            );

            output.append(".").append(System.lineSeparator());
        }

        System.out.println(output);
    }
}