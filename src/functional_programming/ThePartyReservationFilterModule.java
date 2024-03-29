package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ThePartyReservationFilterModule {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] guests = reader.readLine().split("\\s+");
        Map<String, Predicate<String>> filters = new HashMap<>();

        Function<String, Predicate<String>> startsWithPredicate = (pattern) -> (name) -> name.startsWith(pattern);
        Function<String, Predicate<String>> endsWithPredicate = (pattern) -> (name) -> name.endsWith(pattern);
        Function<Integer, Predicate<String>> lengthPredicate = (number) -> (name) -> name.length() == number;
        Function<String, Predicate<String>> containsPredicate = (pattern) -> (name) -> name.contains(pattern);

        String input;

        while (!"Print".equals(input = reader.readLine())) {
            String[] command = input.split(";");
            String key = command[1].concat(command[2]);

            Predicate<String> predicate;

            switch (command[1]) {
                case "Starts with":
                    predicate = startsWithPredicate.apply(command[2]);
                    break;
                case "Ends with":
                    predicate = endsWithPredicate.apply(command[2]);
                    break;
                case "Length":
                    predicate = lengthPredicate.apply(Integer.parseInt(command[2]));
                    break;
                case "Contains":
                    predicate = containsPredicate.apply(command[2]);
                    break;
                default:
                    continue;
            }


            if ("Add filter".equals(command[0])) {
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