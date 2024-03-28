package functional_programming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Function;

public class FindTheSmallestElement {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] numbers = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        Function<int[], Integer> findSmallest = (array) -> {
            int smallestIndex = 0;

            for (int index = 0; index < array.length; index++) {
                if (array[index] <= array[smallestIndex]) {
                    smallestIndex = index;
                }
            }

            return smallestIndex;
        };

        System.out.println(findSmallest.apply(numbers));
    }
}