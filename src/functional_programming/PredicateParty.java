package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateParty {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<String> guests = Arrays.stream(reader.readLine().split("\\s+")).collect(Collectors.toList());

        String input;

        while (!"Party!".equals(input = reader.readLine())) {
            String[] data = input.split("\\s+");

            Predicate<String> nameMatcher;

            switch (data[1]) {
                case "StartsWith":
                    nameMatcher = (name) -> name.startsWith(data[2]);
                    break;
                case "EndsWith":
                    nameMatcher = (name) -> name.endsWith(data[2]);
                    break;
                case "Length":
                    nameMatcher = (name) -> name.length() == Integer.parseInt(data[2]);
                    break;
                default:
                    continue;
            }

            List<String> sublist = guests.stream().filter(nameMatcher).collect(Collectors.toList());

            if ("Double".equals(data[0])) {
                guests.addAll(sublist);
            }

            if ("Remove".equals(data[0])) {
                guests.removeAll(sublist);
            }
        }

        if (guests.isEmpty()) {
            System.out.println("Nobody is going to the party!");
            return;
        }

        Collections.sort(guests);

        System.out.printf("%s are going to the party!%n", String.join(", ", guests));
    }
}