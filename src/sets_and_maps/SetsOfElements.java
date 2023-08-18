package sets_and_maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetsOfElements {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int[] data = splitToIntArray(reader.readLine());
        int lines = data[0] + data[1];

        Set<Integer> setOne = new LinkedHashSet<>();
        Set<Integer> setTwo = new LinkedHashSet<>();

        while (lines-- > 0) {
            if (lines >= data[1]) {
                setOne.add(Integer.parseInt(reader.readLine()));
            } else {
                setTwo.add(Integer.parseInt(reader.readLine()));
            }
        }

        StringBuilder output = new StringBuilder();

        setOne.forEach(el -> {
            if (setTwo.contains(el)) {
                output.append(el).append(" ");
            }
        });

        System.out.println(output);
    }

    private static int[] splitToIntArray(String input) {
        return Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
    }
}