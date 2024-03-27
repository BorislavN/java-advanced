package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;

public class CustomMinFunction {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Integer[] data = Arrays.stream(reader.readLine().split("\\s+"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);

        Function<Integer[], Integer> min = (numbers) -> {
            int result = Integer.MAX_VALUE;

            for (Integer number : numbers) {
                if (number < result) {
                    result = number;
                }
            }

            return result;
        };

        System.out.println(min.apply(data));
    }
}