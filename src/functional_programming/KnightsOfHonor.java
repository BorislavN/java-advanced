package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class KnightsOfHonor {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        UnaryOperator<String> appender = "Sir "::concat;

        Arrays.stream(reader.readLine().split("\\s+"))
                .map(appender)
                .forEach(System.out::println);
    }
}