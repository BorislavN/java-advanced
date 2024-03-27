package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Predicate;

public class PredicateForNames {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int value = Integer.parseInt(reader.readLine());

        Predicate<String> condition = (name) -> name.length() <= value;

        Arrays.stream(reader.readLine().split("\\s+"))
                .filter(condition)
                .forEach(System.out::println);
    }
}