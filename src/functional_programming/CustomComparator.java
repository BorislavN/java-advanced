package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class CustomComparator {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer[] numbers = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);

        Comparator<Integer> evenOrOddComparator = Comparator.comparingInt(v -> Math.abs((v % 2)));

        Arrays.sort(numbers, evenOrOddComparator.thenComparing(Comparator.naturalOrder()));

        String result = Arrays.stream(numbers).map(Object::toString).collect(Collectors.joining(" "));

        System.out.println(result);
    }
}