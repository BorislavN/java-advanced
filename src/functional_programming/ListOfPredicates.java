package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListOfPredicates {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());

        Function<Integer, Predicate<Integer>> createPredicate = (divisor) -> ((value) -> value % divisor == 0);

        List<Predicate<Integer>> predicates = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .distinct()
                .mapToObj(createPredicate::apply)
                .collect(Collectors.toList());


        for (int i = 1; i <= n; i++) {
            boolean isValid = true;

            for (Predicate<Integer> predicate : predicates) {
                if (!predicate.test(i)) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                System.out.print(i + " ");
            }
        }
    }
}