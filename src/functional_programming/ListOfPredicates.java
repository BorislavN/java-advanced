package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListOfPredicates {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        List<Integer> divisors = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::parseInt)
                .distinct()
                .collect(Collectors.toList());

        divisors.sort(Comparator.reverseOrder());

        Function<Integer, Predicate<Integer>> createPredicate = (divisor) -> ((value) -> value % divisor == 0);

        List<Predicate<Integer>> predicates = divisors.stream()
                .map(createPredicate)
                .collect(Collectors.toList());

        Predicate<Integer> testElement = (e) -> {
            for (Predicate<Integer> predicate : predicates) {
                if (!predicate.test(e)) {
                    return false;
                }
            }

            return true;
        };

        StringBuilder output = new StringBuilder();

        IntStream.range(1, n + 1).filter(testElement::test).forEach(e -> output.append(e).append(" "));

        System.out.println(output);
    }
}