package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ThePartyReservationFilterModule {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] guests = reader.readLine().split("\\s+");
        Map<String, Predicate<String>> filters = new HashMap<>();

        String input;

        while (!"Print".equals(input = reader.readLine())) {
            String[] command = input.split(";");
            String key = command[1].concat(command[2]);

            if ("Add filter".equals(command[0])) {
                Predicate<String> predicate;

                switch (command[1]) {
                    case "Starts with":
                        predicate = (name) -> name.startsWith(command[2]);
                        break;
                    case "Ends with":
                        predicate = (name) -> name.endsWith(command[2]);
                        break;
                    case "Length":
                        predicate = (name) -> name.length() == Integer.parseInt(command[2]);
                        break;
                    case "Contains":
                        predicate = (name) -> name.contains(command[2]);
                        break;
                    default:
                        continue;
                }

                filters.put(key, predicate);
            }

            if ("Remove filter".equals(command[0])) {
                filters.remove(key);
            }
        }

        StringBuilder output = new StringBuilder();

        for (String guest : guests) {
            boolean isValid = true;

            for (Predicate<String> filter : filters.values()) {
                if (filter.test(guest)) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                output.append(guest).append(" ");
            }
        }

        System.out.println(output);
    }
}